package egovframework.example.board.service; //java파일에 속해 있는 패키지

import java.io.Serializable; //Serializable 인터페이스를 사용하기 위한 import 구문 객체를 저장하거나 직렬화 할 수 있게 함 

//게시판 검색 조건을 담는 VO 
//게시판을 어떤 조건으로 조회할 것인지에 대한 데이터
/*boradList.jsp
->searchKeyWord
->BoardController
->BoardSerachVO.searchKeyWord
->Mapper*/
public class BoardSearchVO implements Serializable {
	
	//초기값은 null 대신 빈 문자열 상태로 시작하기 위해 ""로 지정
    private String searchKeyword = "";
    public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Integer getMinRecommend() {
		return minRecommend;
	}

	public void setMinRecommend(Integer minRecommend) {
		this.minRecommend = minRecommend;
	}
	//view에서 페이징 번호를 클릭하면 boardList.jsp에서 이 값을 변경
	private int pageIndex = 1;
	//현재 보고 있는 페이지 번호의 기본값 1
	//한페이지에 보여주는 게시글 개수 10
	//페이징 영역에서 보여줄 개수 10
    private int pageUnit = 10;
    private int pageSize = 10;
    private int firstIndex = 0;
    private int lastIndex = 0;
    private int recordCountPerPage = 10;
    
    //ztree영역에서 선택할 게시글 카테고리 저장 기본값은 빈 문자열
    private String categoryCode = "";
    //최소 추천 수 저장
    private Integer minRecommend;
    
	/*
	 * getter = 값을 꺼내는 메서드 
	 * ex)getPageIndex() ->
	 * VO안의 pageIndex값을 읽음 
	 * ->return
	 * pageIndex ->호출한 쪽에 값 전달
	 */
	/*
	 * setter = 값을 넣는 메서드
	 * setter는 VO 내부 변수 값 변경을 위해 사용
	 * ex)외부에서 값 변경
	 * ->매개변수에 변경된 값 저장
	 * ->this.~ = ~
	 * VO의 실제 Index 값 변경
	 * ->public void(매개변수)
	 * public int-> int = 자료형
	 */
    
	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageUnit() {
		return pageUnit;
	}

	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}

}