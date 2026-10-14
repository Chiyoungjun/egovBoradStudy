package egovframework.example.comment.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.example.comment.service.CommentVO;

@Repository("commentDAO")
public class CommentDAO extends EgovAbstractMapper {

    // 댓글 목록 조회
    public List<CommentVO> selectCommentList(CommentVO commentVO) throws Exception
        {return selectList("commentDAO.selectCommentList",commentVO);}

    // 댓글 등록
    public void insertComment(CommentVO commentVO) throws Exception
        {insert("commentDAO.insertComment",commentVO);}

    // 댓글 수정
    public void updateComment(CommentVO commentVO) throws Exception
        {update("commentDAO.updateComment",commentVO);}
}