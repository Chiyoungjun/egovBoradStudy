// 이 클래스가 속한 패키지 -> Service의 실제 기능을 구현하는 클래스
package egovframework.example.board.service.impl;

import java.util.List;

/*@Resource 어노테이션을 사용하기 위해 import Spring이 관리 하는 객체 중 이름에 맞는 객체를 찾아 자동으로 연결
 * @Resource(name="boardDAO")
    private BoardDAO boardDAO; 이런 형태로 BoardDAO 객체를 주입받을 때 사용*/
import javax.annotation.Resource; 

/*전자정부프레임워크에서 제공하는 Service 구현용 기본 클래스
   BoardServiceImpl 클래스를 상속받음 
 * 전자정부프레임워크 Service 계층에서 공통 기능이나 서비스 구현 구조를 맞추기 위해 사용
*/import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Repository;
/*Spring의 @Service 어노테이션을 사용하기 위해 import
  @Service를 붙이면 Spring이 이 클래스를
  Service 객체로 생성하고 관리한다.*/
import org.springframework.stereotype.Service;

/*게시글 검색VO, 게시글데이터VO,Service인터페이스 import*/
import egovframework.example.board.service.BoardSearchVO;
import egovframework.example.board.service.BoardService;
import egovframework.example.board.service.BoardVO;

/*해당 클래스를 Spring Service 객체로 지정
 * "boardService라는 이름으로 등록"
 * 컨트롤단에서 
 *  @Resource(name="boardDAO")
    private BoardDAO boardDAO;
    라고 하면 BoardServiceImpl과 객체가 연결된다.*/
@Service("boardService")
/*BoardServiceImpl 클래스 선언 -> BoardService인터페이스에서 정한 메서드를 실제로 구현
 * 상속은 전자정부 프레임워크 Service 기본 클래스를 상속*/
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService {
	/*Spring이 boardDAO라는 이름으로 등록된 객체를 찾아서 주입
	 * BoardDAO에는 @Repository가 붙어 있음
	 * 즉 @Repository("boardDAO") -> Spring Container -> @Resource(name="BoardDAO")
	 * BoardDAO 객체를 직접 new하지 않아도 사용할 수 있음*/
    @Resource(name="boardDAO")
    private BoardDAO boardDAO;

	/*BoardService 인터페이스에 선언된 메서드를 실제로 구현한 메서드
	 * @Override가 붙으면 부모 클래스 또는 인터페이스의 메서드를 제대로 재정의했는지 컴파일러가 확인*/
    @Override
    public List<BoardVO> selectBoardList(BoardSearchVO searchVO) throws Exception
		/*ServiceImpl에서는 직접 SQL을 실행하지 않고 BoardDAO에게 검색조건을 넘겨 조회를 요청
		 *boardDAO.selectBoardList(searchVO)
		 *->DAO가 Mapper XML을 호출 
		 *->DB에서 게시글 목록 조회
		 *DAO에서 받은 List<BoardVO>를 그대로 컨트롤러쪽으로 return*/
        {return boardDAO.selectBoardList(searchVO);}

    @Override
    public int selectBoardListTotCnt(BoardSearchVO searchVO) throws Exception
        {return boardDAO.selectBoardListTotCnt(searchVO);}

    @Override
    public BoardVO selectBoardDetail(BoardVO boardVO) throws Exception
        {return boardDAO.selectBoardDetail(boardVO);}

    @Override
    public void insertBoard(BoardVO boardVO) throws Exception
        {boardDAO.insertBoard(boardVO);}

    @Override
    public void updateBoard(BoardVO boardVO) throws Exception
        {boardDAO.updateBoard(boardVO);}

    @Override
    public void updateBoardFile(BoardVO boardVO) throws Exception
        {boardDAO.updateBoardFile(boardVO);}
}