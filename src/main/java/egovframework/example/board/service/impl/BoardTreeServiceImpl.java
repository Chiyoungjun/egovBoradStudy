package egovframework.example.board.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.example.board.service.BoardTreeService;
import egovframework.example.board.service.BoardTreeVO;

@Service("boardTreeService")
public class BoardTreeServiceImpl extends EgovAbstractServiceImpl implements BoardTreeService {

    @Resource(name = "boardTreeDAO")
    private BoardTreeDAO boardTreeDAO;

    @Override
    public List<BoardTreeVO> selectBoardTreeList() throws Exception {
        return boardTreeDAO.selectBoardTreeList();
    }

    @Override
    public List<BoardTreeVO> selectRecommendPostList(BoardTreeVO treeVO) throws Exception {
        return boardTreeDAO.selectRecommendPostList(treeVO);
    }
}