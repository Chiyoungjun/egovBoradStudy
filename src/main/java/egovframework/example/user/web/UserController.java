package egovframework.example.user.web;

import javax.annotation.Resource;
import javax.servlet.http.Cookie; //ID 저장 쿠키 사용
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; //로그인 사용자 Session 관리

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody; //문자열 결과를 응답값으로 직접 반환

import egovframework.example.user.service.UserService;
import egovframework.example.user.service.UserVO;

@Controller
public class UserController {

    @Resource(name="userService")
    private UserService userService;

    //로그인 화면 요청 시 게시판 메인으로 이동
    @RequestMapping("/user/loginView.do")
    public String loginView()throws Exception{
        return "redirect:/board/selectBoardList.do";
    }

    //로그인 처리 - AJAX POST
    @RequestMapping(value="/user/actionLoginAjax.do",method=RequestMethod.POST)
    @ResponseBody
    public String actionLoginAjax(@ModelAttribute("userVO") UserVO userVO,HttpServletRequest request,HttpServletResponse response)throws Exception{

        //ID/PWD 미입력 확인
        if(userVO.getUiId()==null||"".equals(userVO.getUiId().trim())||userVO.getUiPwd()==null||"".equals(userVO.getUiPwd().trim()))return "empty";

        //입력한 ID로 사용자 조회
        UserVO loginUser=userService.selectLoginUser(userVO);

        //ID 또는 비밀번호 확인
        if(loginUser==null)return "noId";
        if(!loginUser.getUiPwd().equals(userVO.getUiPwd()))return "wrongPwd";

        //로그인 성공 시 사용자 정보를 Session에 저장
        HttpSession session=request.getSession();
        session.setAttribute("uiIdx",loginUser.getUiIdx());
        session.setAttribute("uiName",loginUser.getUiName());
        session.setAttribute("uiLastlogin",loginUser.getUiLastlogin());

        //ID 저장 체크 시 Cookie를 30일간 저장
        if("Y".equals(userVO.getSaveId())){
            Cookie cookie=new Cookie("savedId",loginUser.getUiId());
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
        }else{
            //ID 저장 해제 시 기존 Cookie 삭제
            Cookie cookie=new Cookie("savedId","");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        //현재 로그인 시간을 DB에 갱신
        userService.updateLastLogin(loginUser);

        return "success";
    }

    //회원가입 처리 - AJAX POST
    @RequestMapping(value="/user/insertUserAjax.do",method=RequestMethod.POST)
    @ResponseBody
    public String insertUserAjax(@ModelAttribute("userVO") UserVO userVO)throws Exception{

        //ID/PWD 미입력 확인
        if(userVO.getUiId()==null||"".equals(userVO.getUiId().trim())||userVO.getUiPwd()==null||"".equals(userVO.getUiPwd().trim()))return "empty";

        //동일 ID 존재 여부 확인
        UserVO checkUser=userService.selectLoginUser(userVO);
        if(checkUser!=null)return "duplicate";

        //현재 구조에서는 이름을 ID와 동일하게 저장
        userVO.setUiName(userVO.getUiId());
        userService.insertUser(userVO);

        return "success";
    }

    //로그아웃 - Session 전체 만료 후 게시판으로 이동
    @RequestMapping("/user/logout.do")
    public String logout(HttpServletRequest request)throws Exception{
        HttpSession session=request.getSession(false);
        if(session!=null)session.invalidate();
        return "redirect:/board/selectBoardList.do";
    }
}