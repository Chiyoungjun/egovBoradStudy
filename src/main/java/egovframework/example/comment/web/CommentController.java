package egovframework.example.comment.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.example.comment.service.CommentService;
import egovframework.example.comment.service.CommentVO;

@Controller
public class CommentController {

    @Resource(name="commentService")
    private CommentService commentService;

    // 댓글 목록 조회 - 비로그인도 가능
    @RequestMapping("/comment/selectCommentList.do")
    public String selectCommentList(@ModelAttribute("commentVO") CommentVO commentVO,ModelMap model)throws Exception{
        List<CommentVO> commentList=commentService.selectCommentList(commentVO);
        model.addAttribute("commentList",commentList);
        return "comment/commentList";
    }

    // 댓글 등록 - 로그인 필요
    @RequestMapping(value="/comment/insertComment.do",method=RequestMethod.POST)
    @ResponseBody
    public String insertComment(@ModelAttribute("commentVO") CommentVO commentVO,HttpSession session)throws Exception{
        Long uiIdx=(Long)session.getAttribute("uiIdx");

        if(uiIdx==null)return "login";
        if(commentVO.getBiContent()==null||"".equals(commentVO.getBiContent().trim()))return "empty";

        commentVO.setUiIdx(uiIdx);
        commentService.insertComment(commentVO);

        return "success";
    }

    // 댓글 수정 - 로그인 및 작성자 확인
    @RequestMapping(value="/comment/updateComment.do",method=RequestMethod.POST)
    @ResponseBody
    public String updateComment(@ModelAttribute("commentVO") CommentVO commentVO,HttpSession session)throws Exception{
        Long uiIdx=(Long)session.getAttribute("uiIdx");

        if(uiIdx==null)return "login";
        if(commentVO.getBiContent()==null||"".equals(commentVO.getBiContent().trim()))return "empty";

        commentVO.setUiIdx(uiIdx);
        commentService.updateComment(commentVO);

        return "success";
    }
}