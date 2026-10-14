/* RecommendService는 추천 처리 후 현재 추천수(recommendCount)와
 * 추천 여부(recommendedYn)를 화면에 바로 전달해야 하므로
 * RecommendVO를 매개변수로 받아 처리하고 결과도 RecommendVO로 반환한다.
 *
 * 즉 RecommendVO 하나가 조회조건 전달과 처리결과 반환 역할을 같이 하기 때문에
 * 기존 게시글, 댓글 메서드 구조와 조금 다르게 보인다. */
package egovframework.example.recommend.service;

/* 추천 기능 Service 인터페이스
 * RecommendVO를 매개변수로 받아 추천 관련 조건을 전달하고,
 * 처리 결과도 RecommendVO에 담아서 반환하는 구조
 * RecommendVO 안에는
 * biIdx           -> 어떤 게시글인지
 * uiIdx           -> 어떤 사용자인지
 * recommendCount  -> 현재 추천수
 * recommendedYn   -> 현재 사용자가 추천했는지 Y/N
 * 가 같이 들어있기 때문에
 * 조회조건 전달과 처리결과 반환을 하나의 VO로 처리 */
public interface RecommendService {

    /* 현재 게시글의 추천상태 조회
     * 매개변수 RecommendVO recommendVO
     * -> biIdx, uiIdx를 Service에 전달
     * 반환 RecommendVO
     * -> 조회한 recommendCount와 recommendedYn을
     *    RecommendVO에 담아서 반환
     * 즉 입력도 RecommendVO, 결과도 RecommendVO 사용 */
    RecommendVO selectRecommendStatus(RecommendVO recommendVO)throws Exception;


    /* 추천 / 추천취소 처리
     * 매개변수 RecommendVO recommendVO
     * -> biIdx와 로그인 사용자의 uiIdx 전달
     * 이미 추천했으면 추천취소
     * 추천하지 않았으면 추천등록
     * 반환 RecommendVO
     * -> 처리 후 변경된 추천수와
     *    현재 추천상태 Y/N을 담아서 반환 */
    RecommendVO toggleRecommend(RecommendVO recommendVO)throws Exception;
}