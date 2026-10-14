//파일이 속한 패키지 ->기능 실제 구현 클래스들을 모아두는 service.impl 패키지
package egovframework.example.board.service.impl;

import java.util.List;

/*이 클클래스를 상속받으면 DB 접근메서드 사용가능*/
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper; // 전자정부에서 제공하는 MyBatis 지원 클래스
import org.springframework.stereotype.Repository; //Spring에서 이 클래스가 DAO 역할이라는 것을 알려주는 어노테이션

import egovframework.example.board.service.BoardSearchVO; //게시글 검색조건 담는 VO
import egovframework.example.board.service.BoardVO; //게시글 정보 담는 VO

/*이 클래스는 DB접근을 담당하는 DAO라는 정의
 * 이 파일 기준 boardDAO라는 이름으로 Spring Bean에 등록
 * ServiceImpl에서 
 * @Resource(name="boardDAO")
 * private BoardDAO boardDAO; 로 불러서 사용 가능*/
@Repository("boardDAO")
/* DAO 클래스 선언 
 * EgovAbstractMapper 기능을 상속받기에 MyBatisDB 호출 메서드 바로 사용 가능 */
public class BoardDAO extends EgovAbstractMapper {
	/* 게시판 리스트 여러 건 조회
	 * 반환타입 => List<BoardVO> -> boardVO 여러 개를 담은 목록 반환 */
    public List<BoardVO> selectBoardList(BoardSearchVO searchVO) throws Exception
		/* selectList() -> sql조회 결과가 여러 행일 때 사용
		 * 첫번 째 값 -> boardDAO.selectBoardList->Mapper XML의 namespace +id
		 * <mapper namespace="boardDAO"> <select id ="selectBoardList"> */
        {return selectList("boardDAO.selectBoardList",searchVO);}

	/* 게시글 전체 개수 조회 -> 페이징 계산에 사용 반환타입 int */
    public int selectBoardListTotCnt(BoardSearchVO searchVO) throws Exception
        {return selectOne("boardDAO.selectBoardListTotCnt",searchVO);}

	/* 게시글 상세조회 반환타입 BoardVO 매개변수 BoardVO boardVO*/
    public BoardVO selectBoardDetail(BoardVO boardVO) throws Exception
        {return selectOne("boardDAO.selectBoardDetail",boardVO);}

    public void insertBoard(BoardVO boardVO) throws Exception
        {insert("boardDAO.insertBoard",boardVO);}

    public void updateBoard(BoardVO boardVO) throws Exception
        {update("boardDAO.updateBoard",boardVO);}

    public void updateBoardFile(BoardVO boardVO) throws Exception
        {update("boardDAO.updateBoardFile",boardVO);}
}