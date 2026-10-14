package egovframework.example.comment.service;

import java.util.List;

public interface CommentService {
    // 댓글 목록
    List<CommentVO> selectCommentList(CommentVO commentVO) throws Exception;
    // 댓글 등록
    void insertComment(CommentVO commentVO) throws Exception;
    // 댓글 수정
    void updateComment(CommentVO commentVO) throws Exception;

}