/* BoardController와 BoardTreeController를 분리한 이유
 * -> BoardController는 일반 게시판의 목록/상세/등록/수정/파일처리를 담당
 * -> BoardTreeController는 zTree에서 사용하는 Tree 조회와 AJAX JSON 응답을 담당
 * 기능의 목적과 응답방식이 다르기 때문에 Controller를 분리하여 관리
 * BoardTreeVO / Service / ServiceImpl / DAO도 분리 이유
 * -> 일반 게시글 데이터와 Tree 노드 데이터의 구조와 조회방식이 다르기 때문
 * BoardVO -> 게시글 자체 정보
 * BoardTreeVO -> Tree 노드 정보 및 추천조건 정보
 * 각각 역할을 분리하면 기능 수정과 유지보수가 쉬움 */

//파일이 속한 패키지 -> Tree 관련 웹 요청을 처리하는 web 패키지
package egovframework.example.board.web;

import java.io.PrintWriter; //HttpServletResponse에 문자열(JSON)을 직접 출력할 때 사용
import java.util.List; //여러 개의 BoardTreeVO를 담기 위해 사용

import javax.annotation.Resource; //Spring Bean 주입
import javax.servlet.http.HttpServletResponse; //브라우저에 JSON 응답을 직접 전달

import org.json.JSONArray; //여러 개의 JSON 데이터를 배열 형태로 저장
import org.json.JSONObject; //JSON 데이터를 key:value 형태로 저장
import org.springframework.stereotype.Controller; //Controller로 Spring Bean 등록
import org.springframework.web.bind.annotation.ModelAttribute; //요청값을 BoardTreeVO에 자동 저장
import org.springframework.web.bind.annotation.RequestMapping; //URL과 Controller 메서드 연결

import com.fasterxml.jackson.databind.ObjectMapper; //Java 객체를 JSON 문자열로 변환

import egovframework.example.board.service.BoardTreeService; //Tree Service 인터페이스
import egovframework.example.board.service.BoardTreeVO; //Tree 노드 정보를 담는 VO

@Controller
public class BoardTreeController {

    //boardTreeService라는 이름으로 등록된 BoardTreeServiceImpl 객체 주입
    @Resource(name="boardTreeService")
    private BoardTreeService boardTreeService;

    /* 게시판 기본 Tree 조회
     * JSP 화면을 반환하는 것이 아니라 AJAX 요청에 JSON 데이터를 직접 응답하기 때문에
     * 반환타입이 String이 아닌 void이고 HttpServletResponse를 사용 */
    @RequestMapping("/tree/selectBoardTreeList.do")
    public void selectBoardTreeList(HttpServletResponse response)throws Exception{

        /* JSONObject -> 하나의 JSON 객체를 만드는 클래스
         * JSON은 key:value 형태로 데이터를 구성
         * 최종적으로 아래와 같은 구조를 만들기 위한 빈 JSON 객체 생성
         * {
         *   "treeList": [...],
         *   "result": true,
         *   "resultMsg": ""
         * }
         * jObject에 put()을 사용하면서 값을 하나씩 추가 */
        JSONObject jObject=new JSONObject();

        try{
            //Service에서 기본 Tree 노드 여러 개 조회
            List<BoardTreeVO> treeList=boardTreeService.selectBoardTreeList();

            /* ObjectMapper -> Java 객체를 JSON 형태로 변환
             * 현재 treeList는 Java의 List<BoardTreeVO> 객체이므로
             * JSONObject/JSONArray에서 바로 사용하기보다 JSON 문자열로 변환 */
            ObjectMapper objectMapper=new ObjectMapper();

            /* writeValueAsString()
             * -> Java 객체를 JSON 문자열로 변환
             * List<BoardTreeVO>
             * ↓
             * JSON 문자열 */
            String treeJson=objectMapper.writeValueAsString(treeList);

            /* JSONArray -> 여러 개의 JSON 객체를 배열 형태로 관리
             * ObjectMapper가 만든 JSON 문자열을 JSONArray로 변환
             * treeList가 여러 Tree 노드를 가지고 있으므로 JSONArray 사용 */
            JSONArray jsonArray=new JSONArray(treeJson);

            /* JSONObject.put("key",value)
             * -> JSON 객체에 데이터를 추가
             * 	-> JSONObject 안에 브라우저로 전달할 데이터를 key:value 형태로 추가
             	-> 이후 jObject.toString()으로 JSON 문자열로 변환되어 AJAX 응답으로 전달
 				-> JavaScript에서는 key 이름으로 해당 값을 꺼내서 사용 
             * treeList -> 실제 Tree 노드 목록
             * result -> 정상 처리 여부
             * resultMsg -> 처리 메시지 */
            jObject.put("treeList",jsonArray);
            jObject.put("result",true);
            jObject.put("resultMsg","");

        }catch(Exception e){
            //오류내용을 Eclipse Console에 출력
            e.printStackTrace();

            /* 오류가 발생했을 경우에도 같은 JSON 구조로 응답
             * treeList는 빈 배열
             * result는 false
             * resultMsg에는 발생한 오류메시지 저장 */
            jObject.put("treeList",new JSONArray());
            jObject.put("result",false);
            jObject.put("resultMsg",e.getMessage());
        }

        //브라우저에게 응답 데이터가 UTF-8 JSON 형식이라는 것을 알려줌
        response.setContentType("application/json;charset=UTF-8");

        /* PrintWriter -> HttpServletResponse에 문자열을 직접 출력
         * JSP로 이동하지 않고 JSON 문자열 자체를 AJAX 요청 결과로 전달 */
        PrintWriter printWriter=response.getWriter();

        //JSONObject를 문자열로 변환하여 브라우저에 전송
        printWriter.print(jObject.toString());

        //출력 버퍼에 남은 데이터를 즉시 전송
        printWriter.flush();

        //PrintWriter 사용 종료
        printWriter.close();
    }


    /* 추천수 Node 하위 게시글 조회
     * 기본 Tree 조회와 달리 어떤 Tree 노드를 선택했는지 조건이 필요하기 때문에
     * BoardTreeVO treeVO를 매개변수로 받음 */
    @RequestMapping("/tree/selectRecommendPostList.do")
    public void selectRecommendPostList(@ModelAttribute("treeVO") BoardTreeVO treeVO,HttpServletResponse response)throws Exception{

        //최종 AJAX 응답을 담을 JSON 객체 생성
        JSONObject jObject=new JSONObject();

        try{
            /* Tree 게시글 조회에 필요한 값이 모두 있는지 확인
             * parentTreeId -> 조회결과를 어느 Tree 부모 아래에 붙일지
             * categoryCode -> 어떤 카테고리인지
             * minRecommend -> 최소 추천수가 몇인지 */
            if(treeVO.getParentTreeId()==null
                    ||treeVO.getCategoryCode()==null
                    ||treeVO.getMinRecommend()==null){

                //조회조건이 부족하면 빈 목록과 실패결과 반환
                jObject.put("treeList",new JSONArray());
                jObject.put("result",false);
                jObject.put("resultMsg","Tree 조회조건이 올바르지 않습니다.");

            }else{
                //조건에 맞는 추천 게시글 Tree 노드 여러 건 조회
                List<BoardTreeVO> treeList=boardTreeService.selectRecommendPostList(treeVO);

                //List<BoardTreeVO> -> JSON 문자열 변환
                ObjectMapper objectMapper=new ObjectMapper();
                String treeJson=objectMapper.writeValueAsString(treeList);

                //JSON 문자열 -> JSON 배열 변환
                JSONArray jsonArray=new JSONArray(treeJson);

                //정상 조회 결과를 JSONObject에 저장
                jObject.put("treeList",jsonArray);
                jObject.put("result",true);
                jObject.put("resultMsg","");
            }

        }catch(Exception e){
            e.printStackTrace();

            //오류 발생 시에도 같은 JSON 형식 유지
            jObject.put("treeList",new JSONArray());
            jObject.put("result",false);
            jObject.put("resultMsg",e.getMessage());
        }

        //JSON UTF-8 응답 설정
        response.setContentType("application/json;charset=UTF-8");

        //만들어진 JSONObject를 브라우저 AJAX 요청에 직접 전송
        PrintWriter printWriter=response.getWriter();
        printWriter.print(jObject.toString());
        printWriter.flush();
        printWriter.close();
    }
}