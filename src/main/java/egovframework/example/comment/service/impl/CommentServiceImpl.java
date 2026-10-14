package egovframework.example.comment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.example.comment.service.CommentService;
import egovframework.example.comment.service.CommentVO;

@Service("commentService")
public class CommentServiceImpl extends EgovAbstractServiceImpl implements CommentService {

    @Resource(name="commentDAO")
    private CommentDAO commentDAO;

    // 댓글 목록 조회
    @Override
    public List<CommentVO> selectCommentList(CommentVO commentVO) throws Exception
        {return commentDAO.selectCommentList(commentVO);}

    // 댓글 등록
    @Override
    public void insertComment(CommentVO commentVO) throws Exception
        {commentDAO.insertComment(commentVO);}

    // 댓글 수정
    @Override
    public void updateComment(CommentVO commentVO) throws Exception
        {commentDAO.updateComment(commentVO);}
}