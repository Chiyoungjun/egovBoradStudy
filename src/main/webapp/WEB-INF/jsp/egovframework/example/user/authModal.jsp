<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="loginModal" class="auth-modal">
    <div class="auth-modal-box">
        <button type="button" class="auth-close" onclick="closeAuthModal();">×</button>
        <h2>로그인</h2>

        <form id="loginForm">
            <div class="auth-row">
                <label for="loginUiId">ID</label>
                <input type="text" id="loginUiId" name="uiId" value="${cookie.savedId.value}" />
            </div>

            <div class="auth-row">
                <label for="loginUiPwd">PW</label>
                <input type="password" id="loginUiPwd" name="uiPwd" />
            </div>

            <div class="auth-save">
                <label>
                    <input type="checkbox" name="saveId" value="Y"
                        <c:if test="${not empty cookie.savedId.value}">checked="checked"</c:if> />
                    ID 저장
                </label>
            </div>

            <div class="auth-button-area">
                <button type="button" onclick="loginUser();">로그인</button>
            </div>
        </form>
    </div>
</div>

<div id="joinModal" class="auth-modal">
    <div class="auth-modal-box">
        <button type="button" class="auth-close" onclick="closeAuthModal();">×</button>
        <h2>회원가입</h2>

        <form id="joinForm">
            <div class="auth-row">
                <label for="joinUiId">ID</label>
                <input type="text" id="joinUiId" name="uiId" />
            </div>

            <div class="auth-row">
                <label for="joinUiPwd">PW</label>
                <input type="password" id="joinUiPwd" name="uiPwd" />
            </div>

            <div class="auth-button-area">
                <button type="button" onclick="joinUser();">회원가입</button>
            </div>
        </form>
    </div>
</div>

<script type="text/javascript">
function openLoginModal() {
    closeAuthModal();
    document.getElementById("loginModal").style.display = "flex";
}

function openJoinModal() {
    closeAuthModal();
    document.getElementById("joinModal").style.display = "flex";
}

function closeAuthModal() {
    document.getElementById("loginModal").style.display = "none";
    document.getElementById("joinModal").style.display = "none";
}

function loginUser() {
    var form = document.getElementById("loginForm");
    var params = new URLSearchParams(new FormData(form));

    fetch("${pageContext.request.contextPath}/user/actionLoginAjax.do", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
        },
        body: params.toString()
    })
    .then(function(response) {
        if (!response.ok) {
            throw new Error("로그인 요청 실패 : " + response.status);
        }
        return response.text();
    })
    .then(function(result) {
        result = result.trim();

        if (result == "success") {
            location.reload();
        } else if (result == "empty") {
            alert("ID와 비밀번호를 입력해주세요.");
        } else if (result == "noId") {
            alert("등록되지 않은 ID입니다.");
        } else if (result == "wrongPwd") {
            alert("비밀번호가 틀렸습니다.");
        } else {
            alert("로그인에 실패했습니다.");
        }
    })
    .catch(function(error) {
        console.error(error);
        alert("로그인 처리 중 오류가 발생했습니다.");
    });
}

function joinUser() {
    var form = document.getElementById("joinForm");
    var params = new URLSearchParams(new FormData(form));

    fetch("${pageContext.request.contextPath}/user/insertUserAjax.do", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
        },
        body: params.toString()
    })
    .then(function(response) {
        if (!response.ok) {
            throw new Error("회원가입 요청 실패 : " + response.status);
        }
        return response.text();
    })
    .then(function(result) {
        result = result.trim();

        if (result == "success") {
            alert("회원가입이 완료되었습니다.");

            var joinId = document.getElementById("joinUiId").value;

            document.getElementById("joinUiId").value = "";
            document.getElementById("joinUiPwd").value = "";

            closeAuthModal();

            document.getElementById("loginUiId").value = joinId;
            document.getElementById("loginUiPwd").value = "";

            openLoginModal();
        } else if (result == "empty") {
            alert("ID와 비밀번호를 입력해주세요.");
        } else if (result == "duplicate") {
            alert("이미 사용 중인 ID입니다.");
        } else {
            alert("회원가입에 실패했습니다.");
        }
    })
    .catch(function(error) {
        console.error(error);
        alert("회원가입 처리 중 오류가 발생했습니다.");
    });
}

window.addEventListener("click", function(event) {
    var loginModal = document.getElementById("loginModal");
    var joinModal = document.getElementById("joinModal");

    if (event.target == loginModal || event.target == joinModal) {
        closeAuthModal();
    }
});
</script>