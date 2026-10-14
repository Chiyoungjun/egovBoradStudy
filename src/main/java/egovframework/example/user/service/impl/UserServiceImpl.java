package egovframework.example.user.service.impl;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.example.user.service.UserService;
import egovframework.example.user.service.UserVO;

@Service("userService")
public class UserServiceImpl extends EgovAbstractServiceImpl implements UserService {

    @Resource(name="userDAO")
    private UserDAO userDAO;

    // 로그인 사용자 조회
    @Override
    public UserVO selectLoginUser(UserVO userVO) throws Exception
        {return userDAO.selectLoginUser(userVO);}

    // 회원가입
    @Override
    public void insertUser(UserVO userVO) throws Exception
        {userDAO.insertUser(userVO);}

    // 마지막 로그인 시간 수정
    @Override
    public void updateLastLogin(UserVO userVO) throws Exception
        {userDAO.updateLastLogin(userVO);}
}