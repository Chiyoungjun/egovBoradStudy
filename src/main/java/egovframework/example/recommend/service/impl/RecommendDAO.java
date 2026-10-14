package egovframework.example.recommend.service.impl;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.example.recommend.service.RecommendVO;

@Repository("recommendDAO")
public class RecommendDAO extends EgovAbstractMapper {

    // 게시글 존재 여부 확인
    public int selectBoardPostCount(RecommendVO recommendVO)throws Exception{
        return selectOne("recommendDAO.selectBoardPostCount",recommendVO);
    }

    // 현재 사용자가 해당 게시글을 이미 추천했는지 확인
    public int selectRecommendCheck(RecommendVO recommendVO)throws Exception{
        return selectOne("recommendDAO.selectRecommendCheck",recommendVO);
    }

    // 해당 게시글의 전체 추천수 조회
    public int selectRecommendCount(RecommendVO recommendVO)throws Exception{
        return selectOne("recommendDAO.selectRecommendCount",recommendVO);
    }

    // 추천 등록
    public void insertRecommend(RecommendVO recommendVO)throws Exception{
        insert("recommendDAO.insertRecommend",recommendVO);
    }

    // 추천 취소
    public void deleteRecommend(RecommendVO recommendVO)throws Exception{
        delete("recommendDAO.deleteRecommend",recommendVO);
    }
}