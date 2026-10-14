package egovframework.example.user.service;

public interface UserService {

    // 로그인 사용자 조회
    UserVO selectLoginUser(UserVO userVO) throws Exception;

    // 회원가입
    void insertUser(UserVO userVO) throws Exception;

    // 마지막 로그인 시간 수정
    void updateLastLogin(UserVO userVO) throws Exception;
}