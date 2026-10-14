package egovframework.example.board.service.impl;

import java.util.List; //여러 개의 BoardTreeVO 객체를 담기 위해 사용

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper; //MyBatis DB 접근 메서드 사용
import org.springframework.stereotype.Repository; //DAO 클래스로 Spring Bean 등록

import egovframework.example.board.service.BoardTreeVO; //Tree 노드 정보를 담는 VO

/* Tree 관련 DB 접근을 담당하는 DAO
 * boardTreeDAO라는 이름으로 Spring Bean에 등록 */
@Repository("boardTreeDAO")
public class BoardTreeDAO extends EgovAbstractMapper {

    /* 기본 게시판 Tree 목록 조회
     * 반환타입 => List<BoardTreeVO>
     * -> 여러 개의 Tree 노드를 반환
     * 매개변수가 없는 이유
     * -> 기본 Tree 전체 구조를 조회하므로 별도의 조회조건이 필요 없음
     * selectList("boardTreeDAO.selectBoardTreeList")
     * -> Mapper XML의 namespace + id를 찾아 여러 행 조회 */
    public List<BoardTreeVO> selectBoardTreeList() throws Exception
        {return selectList("boardTreeDAO.selectBoardTreeList");}


    /* 추천조건에 맞는 게시글 Tree 노드 목록 조회
     * 반환타입 => List<BoardTreeVO>
     * -> 조건에 맞는 게시글 노드 여러 개 반환
     * BoardTreeVO treeVO
     * -> categoryCode, minRecommend, parentTreeId 같은 조회조건을 담아 전달
     * selectList의 두 번째 값 treeVO
     * -> Mapper XML에서 #{categoryCode}, #{minRecommend},
     *    #{parentTreeId} 등으로 사용할 수 있도록 전달 */
    public List<BoardTreeVO> selectRecommendPostList(BoardTreeVO treeVO) throws Exception
        {return selectList("boardTreeDAO.selectRecommendPostList",treeVO);}
}