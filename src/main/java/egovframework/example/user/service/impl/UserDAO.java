package egovframework.example.user.service.impl;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.example.user.service.UserVO;

@Repository("userDAO")
public class UserDAO extends EgovAbstractMapper {

    // 로그인 사용자 조회
    public UserVO selectLoginUser(UserVO userVO) throws Exception
        {return selectOne("userDAO.selectLoginUser",userVO);}

    // 회원가입
    public void insertUser(UserVO userVO) throws Exception
        {insert("userDAO.insertUser",userVO);}

    // 마지막 로그인 시간 수정
    public void updateLastLogin(UserVO userVO) throws Exception
        {update("userDAO.updateLastLogin",userVO);}
}