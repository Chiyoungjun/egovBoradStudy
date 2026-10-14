//boardService가 속한 패키지
package egovframework.example.board.service;
//여러 개의 데이터를 담기 위한 List 사용
import java.util.List;

/*interface부분
=> 실제 기능을 구현하는게 아닌 게시판Service가 제공해야 할 메서드를 정의하는 역할
*/
public interface BoardService {
	/*
	 * 게시글 조회 
	 * 반환 값 List<BoardVO> 
	 * 매개변수 BoardSearchVO타입의 searchVO
	 * 실제 구현은 ServiceImpl에서 작동 
	 * Service => 기능 명세 약속 -> implements -> ServiceImpl -> 실제 구현\
	 * throw Exception => 메서드 실행 도중 예외가 발생할 수 있으니 직접처리하지 않고 메서드를 호출한쪽으로 넘김
	 * ex)
	 * List<타입> => List안에 어떤 자료형을 넣을지 지정하는 타입 Generic라고 함 즉 <BoardVO>는 BoardVO객체를 담는 List라는 정의
	 * List<> =>반환 타입
	 * selectBoardList => 메서드 명
	 * () => 매개변수 명 
	 * 이 파일은 메서드 선먼을 하는 인터페이스 메서드라 void로 시작하는 코드가 아니더라고 return이 없음 실제 retrun 반환값은 serviceImpl에 있음
	 */
    List<BoardVO> selectBoardList(BoardSearchVO searchVO) throws Exception;
	/* 검색조건에 맞는 전체 개시글 조회
	 * ex) BoardVO = 자료형, boardVO = 매개변수명
	 * 반환값 Int */
    int selectBoardListTotCnt(BoardSearchVO searchVO) throws Exception;
	/*게시글 상세 조회
	 * 반환값 BoardVO*/
    BoardVO selectBoardDetail(BoardVO boardVO) throws Exception;
	/* 게시글 등록,수정 첨부파일 정보 수정 void이므로 반환값 없음 */
    void insertBoard(BoardVO boardVO) throws Exception;

    void updateBoard(BoardVO boardVO) throws Exception;

    void updateBoardFile(BoardVO boardVO) throws Exception;
}