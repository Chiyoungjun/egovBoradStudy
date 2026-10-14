package egovframework.example.recommend.service.impl;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; //여러 DB 작업을 하나의 작업단위로 처리

import egovframework.example.recommend.service.RecommendService;
import egovframework.example.recommend.service.RecommendVO;

@Service("recommendService")
public class RecommendServiceImpl extends EgovAbstractServiceImpl implements RecommendService {

    @Resource(name="recommendDAO")
    private RecommendDAO recommendDAO;

    /* 현재 게시글의 추천상태 조회
     * 게시글,댓글 ServiceImpl처럼 DAO 결과를 바로 return하지 않고 추천수와 사용자의 추천여부를 각각 조회한 후 RecommendVO에 결과를 담아 반환
     * RecommendVO가 요청조건(biIdx,uiIdx)과 결과값(recommendCount,recommendedYn)을 같이 가지고 있기 때문에 같은 VO를 수정하여 다시 return */
    @Override
    public RecommendVO selectRecommendStatus(RecommendVO recommendVO)throws Exception{

        //해당 게시글의 전체 추천수 조회
        int recommendCount=recommendDAO.selectRecommendCount(recommendVO);

        //조회한 추천수를 RecommendVO의 recommendCount에 저장
        recommendVO.setRecommendCount(recommendCount);

        /* uiIdx가 null이면 비로그인 사용자이므로 개인 추천여부를 조회할 수 없음
         * 추천하지 않은 상태를 의미하는 N으로 설정 */
        if(recommendVO.getUiIdx()==null){
            recommendVO.setRecommendedYn("N");
        }else{

            //로그인 사용자가 해당 게시글을 추천한 데이터가 존재하는지 조회
            int check=recommendDAO.selectRecommendCheck(recommendVO);

            /* 삼항연산자
             * check가 0보다 크면 추천기록이 존재하므로 Y
             * 존재하지 않으면 N을 저장
             * check>0 ? "Y" : "N" => 조건 ? 참일때값 : 거짓일때값 */
            recommendVO.setRecommendedYn(check>0?"Y":"N");
        }

        //추천수와 추천여부가 추가된 RecommendVO를 Controller로 반환
        return recommendVO;
    }


    /* 추천/추천취소 처리
     * 기존 ServiceImpl은 대부분 DAO 메서드 하나를 호출해서 끝나지만 이 메서드는 현재 상태를 확인한 뒤 추천등록 또는 추천취소 중 어떤 작업을 실행할지 ServiceImpl에서 판단
     * 즉 DAO는 DB작업만 담당하고 추천인지 추천취소인지 결정하는 업무규칙은 ServiceImpl에서 처리 */
    @Override

    /* @Transactional
     * 이 메서드 안에서 실행되는 추천조회/등록/삭제/추천수조회 작업을 하나의 트랜잭션으로 묶음
     * 처리 중 오류가 발생하면 중간까지만 DB에 반영되는 것을 막고 전체 작업을 롤백할 수 있도록 사용 */
    @Transactional
    public RecommendVO toggleRecommend(RecommendVO recommendVO)throws Exception{

        /* 추천하려는 게시글이 실제 존재하는지 먼저 확인
         * selectBoardPostCount 결과가 0이면 존재하지 않는 게시글이므로 이후 추천처리를 하지 않고 예외 발생 */
        if(recommendDAO.selectBoardPostCount(recommendVO)==0)
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");

        //현재 사용자가 이미 이 게시글을 추천했는지 확인
        int check=recommendDAO.selectRecommendCheck(recommendVO);

        /* 이미 추천한 상태라면 추천취소 처리
         * 추천정보를 DB에서 삭제하고 현재 추천상태를 N으로 변경 */
        if(check>0){
            recommendDAO.deleteRecommend(recommendVO);
            recommendVO.setRecommendedYn("N");

        /* 아직 추천하지 않은 상태라면 추천등록 처리
         * 추천정보를 DB에 INSERT하고 현재 추천상태를 Y로 변경 */
        }else{
            recommendDAO.insertRecommend(recommendVO);
            recommendVO.setRecommendedYn("Y");
        }

        /* 추천 또는 추천취소가 끝난 후 변경된 전체 추천수를 다시 조회
         * AJAX 화면에서 최신 추천수를 바로 표시할 수 있도록 RecommendVO에 저장 */
        recommendVO.setRecommendCount(recommendDAO.selectRecommendCount(recommendVO));

        //최종 추천여부와 변경된 추천수가 담긴 RecommendVO를 Controller로 반환
        return recommendVO;
    }
}