package egovframework.example.recommend.web;

import java.io.PrintWriter; //JSON 문자열을 HttpServletResponse로 직접 출력

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse; //브라우저에 JSON 응답 전달
import javax.servlet.http.HttpSession; //로그인 사용자 uiIdx 확인

import org.json.JSONObject; //key:value 형태의 JSON 응답 객체 생성
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute; //요청값을 RecommendVO에 자동 저장
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import egovframework.example.recommend.service.RecommendService;
import egovframework.example.recommend.service.RecommendVO;

@Controller
public class RecommendController {

    @Resource(name="recommendService")
    private RecommendService recommendService;

    //현재 게시글의 추천수와 로그인 사용자의 추천여부 조회
    @RequestMapping("/recommend/selectRecommendStatus.do")
    public void selectRecommendStatus(@ModelAttribute("recommendVO") RecommendVO recommendVO,HttpSession session,HttpServletResponse response)throws Exception{
        JSONObject jObject=new JSONObject();

        try{
            //게시글 번호가 없으면 조회 불가
            if(recommendVO.getBiIdx()==null){
                jObject.put("result",false);
                jObject.put("resultMsg","게시글 정보가 없습니다.");
            }else{
                //Session의 로그인 사용자 번호를 RecommendVO에 저장
                Long uiIdx=(Long)session.getAttribute("uiIdx");
                recommendVO.setUiIdx(uiIdx);

                //추천수와 추천여부 조회
                RecommendVO result=recommendService.selectRecommendStatus(recommendVO);

                //조회 결과를 JSON에 저장
                jObject.put("result",true);
                jObject.put("recommendCount",result.getRecommendCount());
                jObject.put("recommendedYn",result.getRecommendedYn());
                jObject.put("resultMsg","");
            }
        }catch(Exception e){
            e.printStackTrace();
            jObject.put("result",false);
            jObject.put("resultMsg",e.getMessage());
        }

        //완성된 JSON을 브라우저에 전송
        writeJson(response,jObject);
    }


    //추천 또는 추천취소 처리 - 로그인 필요
    @RequestMapping(value="/recommend/toggleRecommend.do",method=RequestMethod.POST)
    public void toggleRecommend(@ModelAttribute("recommendVO") RecommendVO recommendVO,HttpSession session,HttpServletResponse response)throws Exception{
        JSONObject jObject=new JSONObject();

        try{
            Long uiIdx=(Long)session.getAttribute("uiIdx");

            //비로그인 사용자
            if(uiIdx==null){
                jObject.put("result",false);
                jObject.put("resultCode","login");
                jObject.put("resultMsg","로그인이 필요한 서비스입니다.");

            //게시글 번호가 없는 경우
            }else if(recommendVO.getBiIdx()==null){
                jObject.put("result",false);
                jObject.put("resultCode","empty");
                jObject.put("resultMsg","게시글 정보가 없습니다.");

            }else{
                recommendVO.setUiIdx(uiIdx);

                //현재 상태에 따라 추천등록 또는 추천취소 처리
                RecommendVO result=recommendService.toggleRecommend(recommendVO);

                //처리 후 추천수와 추천상태를 JSON으로 반환
                jObject.put("result",true);
                jObject.put("resultCode","success");
                jObject.put("recommendCount",result.getRecommendCount());
                jObject.put("recommendedYn",result.getRecommendedYn());
                jObject.put("resultMsg","");
            }
        }catch(Exception e){
            e.printStackTrace();
            jObject.put("result",false);
            jObject.put("resultCode","error");
            jObject.put("resultMsg",e.getMessage());
        }

        writeJson(response,jObject);
    }


    /* 두 메서드에서 반복되는 JSON 응답 코드를 따로 분리
     * JSONObject를 문자열로 변환해서 브라우저에 직접 전송 */
    private void writeJson(HttpServletResponse response,JSONObject jObject)throws Exception{
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer=response.getWriter();
        writer.print(jObject.toString());
        writer.flush();
        writer.close();
    }
}