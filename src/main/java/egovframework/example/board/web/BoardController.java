//파일이 속한 패키지 -> 사용자의 요청을 받아 처리하는 web 패키지
package egovframework.example.board.web;

import java.io.InputStream; //업로드 파일 데이터를 읽기 위한 입력 스트림
import java.net.URLEncoder; //다운로드 파일명을 URL 형식으로 인코딩할 때 사용
import java.nio.charset.StandardCharsets; //UTF-8 문자 인코딩 사용
import java.nio.file.Files; //파일 생성, 복사, 삭제, 존재여부 확인 등에 사용
import java.nio.file.Path; //파일 경로를 객체로 표현
import java.nio.file.Paths; //문자열 경로를 Path 객체로 변환
import java.nio.file.StandardCopyOption; //파일 복사 시 덮어쓰기 옵션 사용
import java.util.List; //여러 개의 BoardVO를 담기 위해 사용

import javax.annotation.Resource; //Spring Bean을 이름으로 찾아 주입
import javax.servlet.http.HttpServletRequest; //사용자의 HTTP 요청정보 사용
import javax.servlet.http.HttpServletResponse; //사용자에게 HTTP 응답을 직접 보낼 때 사용
import javax.servlet.http.HttpSession; //로그인 사용자 Session 정보 사용

import org.egovframe.rte.fdl.property.EgovPropertyService; //전자정부 설정값 pageUnit,pageSize 등을 읽음
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo; //전자정부 페이징 계산 클래스
import org.springframework.stereotype.Controller; //이 클래스를 Controller로 등록
import org.springframework.ui.ModelMap; //Controller에서 JSP로 데이터 전달
import org.springframework.web.bind.annotation.ModelAttribute; //요청값을 VO에 자동으로 담아줌
import org.springframework.web.bind.annotation.RequestMapping; //URL과 Controller 메서드를 연결
import org.springframework.web.bind.annotation.RequestMethod; //GET,POST 요청방식 지정
import org.springframework.web.multipart.MultipartFile; //업로드된 파일 정보를 담는 객체

import egovframework.example.board.service.BoardSearchVO; //게시글 검색조건 VO
import egovframework.example.board.service.BoardService; //게시판 Service 인터페이스
import egovframework.example.board.service.BoardVO; //게시글 정보 VO

/* 이 클래스가 사용자의 게시판 요청을 처리하는 Controller라는 정의
 * 브라우저 요청 -> Controller -> Service -> ServiceImpl -> DAO -> Mapper 순으로 연결 */
@Controller
public class BoardController {

    /* @Service("boardService")로 등록된 BoardServiceImpl 객체를 주입
     * 변수 자료형은 BoardService 인터페이스를 사용 */
    @Resource(name="boardService")
    private BoardService boardService;

    //properties에 설정된 페이징 값을 사용하기 위해 주입
    @Resource(name="propertiesService")
    protected EgovPropertyService propertiesService;


    /* 게시판 목록 조회
     * @ModelAttribute -> URL/Form으로 넘어온 검색조건을 searchVO에 자동 저장
     * ModelMap -> 조회된 데이터를 JSP로 전달 */
    @RequestMapping("/board/selectBoardList.do")
    public String selectBoardList(@ModelAttribute("searchVO") BoardSearchVO searchVO,ModelMap model)throws Exception{

        //설정파일의 한 페이지 게시글 수와 페이지 번호 표시 개수를 searchVO에 저장
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));

        /* PaginationInfo -> 현재 페이지와 페이지 설정값을 받아
         * DB 조회에 필요한 시작위치와 게시글 개수 등을 계산 */
        PaginationInfo paginationInfo=new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());

        //계산된 페이징 값을 다시 searchVO에 저장 -> Mapper LIMIT/OFFSET 조회에 사용
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        //검색조건을 Service에 전달 -> 조회된 게시글 여러 개를 resultList에 저장
        List<BoardVO> resultList=boardService.selectBoardList(searchVO);
        model.addAttribute("resultList",resultList); //JSP에서 ${resultList}로 사용

        //전체 게시글 수 조회 -> 전체 페이지 수 계산에 사용
        int totCnt=boardService.selectBoardListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo",paginationInfo);

        /* 문자열 return은 ViewResolver를 통해 JSP로 이동
         * board/boardList -> board/boardList.jsp */
        return "board/boardList";
    }


    //게시글 상세조회 -> URL의 biIdx가 boardVO에 자동 저장
    @RequestMapping("/board/selectBoardDetail.do")
    public String selectBoardDetail(@ModelAttribute("boardVO") BoardVO boardVO,ModelMap model)throws Exception{

        BoardVO result=boardService.selectBoardDetail(boardVO);

        /* redirect: -> JSP를 바로 보여주는 것이 아니라
         * 브라우저가 해당 URL을 다시 요청하도록 함 */
        if(result==null)return "redirect:/board/selectBoardList.do";

        model.addAttribute("board",result); //조회 결과를 JSP에 board라는 이름으로 전달
        return "board/boardDetail";
    }


    //게시글 등록화면 이동
    @RequestMapping("/board/boardForm.do")
    public String boardForm(HttpSession session,ModelMap model)throws Exception{

        /* 로그인 시 Session에 저장한 uiIdx 조회
         * getAttribute()는 Object를 반환하기 때문에 Long으로 형변환 */
        Long uiIdx=(Long)session.getAttribute("uiIdx");
        if(uiIdx==null)return "redirect:/board/selectBoardList.do";

        //같은 boardForm.jsp에서 등록/수정을 구분하기 위한 값
        model.addAttribute("mode","insert");
        model.addAttribute("board",new BoardVO()); //등록이므로 비어있는 BoardVO 전달

        return "board/boardForm";
    }


    /* 게시글 등록
     * RequestMethod.POST -> POST 방식으로 들어온 요청만 처리 */
    @RequestMapping(value="/board/insertBoard.do",method=RequestMethod.POST)
    public String insertBoard(@ModelAttribute("boardVO") BoardVO boardVO,HttpSession session,HttpServletRequest request)throws Exception{
    	
    	/* 로그인 Session에 저장되어 있는 사용자 번호를 가져온다.
    	 * 게시글 작성자는 화면에서 전달받은 값이 아니라
    	 * 로그인한 사용자의 uiIdx를 사용한다. */
        Long uiIdx=(Long)session.getAttribute("uiIdx");
        if(uiIdx==null)return "redirect:/board/selectBoardList.do";

        //작성자 번호는 Form값이 아니라 로그인 Session 값을 사용
        boardVO.setUiIdx(uiIdx);
        boardService.insertBoard(boardVO);

        //첨부파일이 있을 경우 실제 파일 저장 후 DB 파일정보 수정
        if(hasUploadFile(boardVO)){
            saveBoardFile(boardVO,request);
            boardService.updateBoardFile(boardVO);
        }

        return "redirect:/board/selectBoardList.do";
    }


    //게시글 수정화면 이동
    @RequestMapping("/board/updateBoardView.do")
    public String updateBoardView(@ModelAttribute("boardVO") BoardVO boardVO,HttpSession session,ModelMap model)throws Exception{

        Long uiIdx=(Long)session.getAttribute("uiIdx");
        if(uiIdx==null)return "redirect:/board/selectBoardList.do";

        //DB의 실제 게시글을 조회하여 현재 로그인 사용자와 작성자가 같은지 확인
        BoardVO result=boardService.selectBoardDetail(boardVO);
        if(result==null||!uiIdx.equals(result.getUiIdx()))return "redirect:/board/selectBoardList.do";

        model.addAttribute("board",result);
        model.addAttribute("mode","update");

        return "board/boardForm";
    }


    //게시글 실제 수정 처리
    @RequestMapping(value="/board/updateBoard.do",method=RequestMethod.POST)
    public String updateBoard(@ModelAttribute("boardVO") BoardVO boardVO,HttpSession session,HttpServletRequest request)throws Exception{

        Long uiIdx=(Long)session.getAttribute("uiIdx");
        if(uiIdx==null)return "redirect:/board/selectBoardList.do";

        BoardVO savedBoard=boardService.selectBoardDetail(boardVO);

        //DB 기준 작성자와 현재 로그인 사용자가 다르면 수정 차단
        if(savedBoard==null||!uiIdx.equals(savedBoard.getUiIdx()))return "redirect:/board/selectBoardList.do";

        boardVO.setUiIdx(uiIdx);

        String oldFilepath=savedBoard.getBiFilepath(); //기존 파일 삭제를 위해 경로 저장
        boolean changeFile=hasUploadFile(boardVO); //새 파일 존재 여부 true/false

        if(changeFile)saveBoardFile(boardVO,request);

        //제목,내용,카테고리 수정
        boardService.updateBoard(boardVO);

        if(changeFile){
            boardService.updateBoardFile(boardVO); //새 파일명/경로 DB 수정

            //기존 파일과 새 파일이 다르면 기존 실제 파일 삭제
            if(oldFilepath!=null&&!oldFilepath.equals(boardVO.getBiFilepath()))
                Files.deleteIfExists(Paths.get(oldFilepath));
        }

        return "redirect:/board/selectBoardDetail.do?biIdx="+boardVO.getBiIdx();
    }


    /* 첨부파일 다운로드
     * String 대신 void인 이유 -> JSP를 반환하지 않고
     * HttpServletResponse에 파일 데이터를 직접 담아 브라우저로 전송 */
    @RequestMapping("/board/downloadBoardFile.do")
    public void downloadBoardFile(@ModelAttribute("boardVO") BoardVO boardVO,HttpServletResponse response)throws Exception{

        BoardVO result=boardService.selectBoardDetail(boardVO);

        //게시글 또는 파일정보가 없으면 HTTP 404 오류 반환
        if(result==null||result.getBiFilepath()==null||result.getBiFilename()==null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //DB에 문자열로 저장된 파일경로를 Path 객체로 변환
        Path filePath=Paths.get(result.getBiFilepath());

        //실제 서버에 파일이 없으면 404
        if(!Files.exists(filePath)||!Files.isRegularFile(filePath)){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //한글파일명 등이 깨지지 않도록 UTF-8 URL 인코딩
        String encodedFilename=URLEncoder.encode(result.getBiFilename(),StandardCharsets.UTF_8.name()).replace("+","%20");

        //브라우저에게 일반 파일 다운로드 응답임을 알려줌
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(Files.size(filePath));
        response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+encodedFilename);

        //서버의 실제 파일을 HTTP Response에 복사해서 사용자에게 전송
        Files.copy(filePath,response.getOutputStream());
        response.getOutputStream().flush();
    }


    /* Controller 내부에서만 사용하는 파일 존재여부 확인 메서드
     * boolean -> 파일이 있으면 true, 없으면 false 반환 */
    private boolean hasUploadFile(BoardVO boardVO){

        MultipartFile uploadFile=boardVO.getUploadFile();

        //null이 아니고, 파일내용과 파일명이 모두 존재하는지 검사
        return uploadFile!=null&&!uploadFile.isEmpty()
                &&uploadFile.getOriginalFilename()!=null
                &&!uploadFile.getOriginalFilename().trim().isEmpty();
    }


    /* 실제 업로드 파일을 서버 폴더에 저장하는 메서드
     * MultipartFile -> 사용자가 Form에서 업로드한 파일 객체 */
    private void saveBoardFile(BoardVO boardVO,HttpServletRequest request)throws Exception{

        MultipartFile uploadFile=boardVO.getUploadFile();
        String originalFilename=uploadFile.getOriginalFilename();

        //전체 경로가 넘어오더라도 실제 파일명만 남김
        originalFilename=originalFilename.replace("\\","/");
        originalFilename=originalFilename.substring(originalFilename.lastIndexOf("/")+1);

        //웹 프로젝트의 실제 resources/upload/board 경로 조회
        String rootPath=request.getServletContext().getRealPath("/resources/upload/board");
        if(rootPath==null)throw new IllegalStateException("게시판 파일 저장 경로를 확인할 수 없습니다.");

        //게시글 번호별 저장 폴더 생성 -> /board/10, /board/11 ...
        Path uploadDir=Paths.get(rootPath,String.valueOf(boardVO.getBiIdx()));
        Files.createDirectories(uploadDir);

        //폴더경로와 파일명을 합쳐 실제 저장경로 생성
        Path savePath=uploadDir.resolve(originalFilename).normalize();

        //../ 등을 이용해 지정된 업로드 폴더 밖으로 나가는 것을 방지
        if(!savePath.startsWith(uploadDir))throw new IllegalArgumentException("잘못된 파일명입니다.");

        /* try-with-resources
         * try() 안에서 만든 InputStream은 작업 종료 후 자동 close */
        try(InputStream inputStream=uploadFile.getInputStream()){
            //같은 파일이 있으면 덮어쓰면서 실제 서버 경로에 저장
            Files.copy(inputStream,savePath,StandardCopyOption.REPLACE_EXISTING);
        }

        //저장한 파일정보를 BoardVO에 넣음 -> 이후 updateBoardFile()에서 DB 저장
        boardVO.setBiFilename(originalFilename);
        boardVO.setBiFilepath(savePath.toString());
    }
}