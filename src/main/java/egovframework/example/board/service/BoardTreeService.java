package egovframework.example.board.service;

import java.util.List; //여러 개의 BoardTreeVO 객체를 담기 위해 사용

public interface BoardTreeService {

    /* 게시판 Tree의 기본 노드 목록 조회
     * 반환타입 => List<BoardTreeVO>
     * -> Tree에 표시할 여러 개의 노드 정보를 BoardTreeVO 목록으로 반환
     * 매개변수가 없는 이유
     * -> 기본 Tree 목록은 board_tree_info에 등록되어 있는 전체 Tree 구조를 조회하기 때문에
     *    Controller에서 별도의 조회조건을 전달할 필요가 없음 */
    List<BoardTreeVO> selectBoardTreeList() throws Exception;


    /* 추천조건에 맞는 게시글 Tree 노드 목록 조회
     * 반환타입 => List<BoardTreeVO>
     * -> 조회조건에 맞는 여러 개의 게시글을 BoardTreeVO 목록으로 반환
     * BoardTreeVO treeVO
     * -> 조회할 Tree의 조건을 담아서 전달하는 매개변수
     * 매개변수가 있는 이유
     * -> 어떤 카테고리인지, 최소 추천수가 몇인지,
     *    어떤 부모 Tree 아래에 게시글을 붙일지 등의 조건이 필요하기 때문 */
    List<BoardTreeVO> selectRecommendPostList(BoardTreeVO treeVO) throws Exception;
}