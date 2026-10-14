<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 상세보기</title>
<link rel="stylesheet" href="<c:url value='/css/egovframework/board.css'/>"/>

<script type="text/javascript">
var contextPath="${pageContext.request.contextPath}";
var loginYn="${not empty sessionScope.uiIdx}";

document.addEventListener("DOMContentLoaded",function(){
    loadRecommendStatus();
    loadCommentList();
});

function loadRecommendStatus(){
    fetch(contextPath+"/recommend/selectRecommendStatus.do?biIdx=${board.biIdx}")
    .then(function(response){
        if(!response.ok)throw new Error("추천상태 조회 실패 : "+response.status);
        return response.json();
    })
    .then(function(data){
        if(!data.result)return;
        document.getElementById("recommendCount").innerText=data.recommendCount;
        var button=document.getElementById("recommendButton");
        if(button!=null)button.innerText=data.recommendedYn=="Y"?"추천취소":"추천";
    })
    .catch(function(error){console.error(error);});
}

function toggleRecommend(){
    if(loginYn!="true"){
        alert("로그인이 필요한 서비스입니다.");
        return;
    }

    var params=new URLSearchParams();
    params.append("biIdx","${board.biIdx}");

    fetch(contextPath+"/recommend/toggleRecommend.do",{
        method:"POST",
        headers:{"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"},
        body:params.toString()
    })
    .then(function(response){
        if(!response.ok)throw new Error("추천 처리 실패 : "+response.status);
        return response.json();
    })
    .then(function(data){
        if(data.result){
            document.getElementById("recommendCount").innerText=data.recommendCount;
            document.getElementById("recommendButton").innerText=data.recommendedYn=="Y"?"추천취소":"추천";
        }else{
            alert(data.resultMsg);
        }
    })
    .catch(function(error){
        console.error(error);
        alert("추천 처리 중 오류가 발생했습니다.");
    });
}

function loadCommentList(){
    var biIdx="${board.biIdx}";

    fetch(contextPath+"/comment/selectCommentList.do?biRefidx="+biIdx)
    .then(function(response){
        if(!response.ok)throw new Error("댓글 목록 조회 실패 : "+response.status);
        return response.text();
    })
    .then(function(html){
        document.getElementById("commentListArea").innerHTML=html;
    })
    .catch(function(error){
        console.error(error);
        document.getElementById("commentListArea").innerHTML="댓글 목록을 불러오지 못했습니다.";
    });
}

function checkCommentLogin(){
    if(loginYn!="true"){
        alert("로그인이 필요한 서비스입니다.");
        return false;
    }
    return true;
}

function insertComment(){
    if(!checkCommentLogin())return;

    var content=document.getElementById("commentContent").value;

    if(content.trim()==""){
        alert("댓글 내용을 입력해주세요.");
        return;
    }

    var params=new URLSearchParams();
    params.append("biRefidx","${board.biIdx}");
    params.append("biContent",content);

    fetch(contextPath+"/comment/insertComment.do",{
        method:"POST",
        headers:{"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"},
        body:params.toString()
    })
    .then(function(response){
        if(!response.ok)throw new Error("댓글 등록 실패 : "+response.status);
        return response.text();
    })
    .then(function(result){
        result=result.trim();

        if(result=="success"){
            document.getElementById("commentContent").value="";
            loadCommentList();
        }else if(result=="empty"){
            alert("댓글 내용을 입력해주세요.");
        }else if(result=="login"){
            alert("로그인이 필요한 서비스입니다.");
        }else{
            alert("댓글 등록에 실패했습니다.");
        }
    })
    .catch(function(error){
        console.error(error);
        alert("댓글 등록 중 오류가 발생했습니다.");
    });
}

function updateComment(biIdx){
    if(!checkCommentLogin())return;

    var content=document.getElementById("comment_"+biIdx).value;

    if(content.trim()==""){
        alert("댓글 내용을 입력해주세요.");
        return;
    }

    var params=new URLSearchParams();
    params.append("biIdx",biIdx);
    params.append("biContent",content);

    fetch(contextPath+"/comment/updateComment.do",{
        method:"POST",
        headers:{"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"},
        body:params.toString()
    })
    .then(function(response){
        if(!response.ok)throw new Error("댓글 수정 실패 : "+response.status);
        return response.text();
    })
    .then(function(result){
        result=result.trim();

        if(result=="success"){
            loadCommentList();
        }else if(result=="empty"){
            alert("댓글 내용을 입력해주세요.");
        }else if(result=="login"){
            alert("로그인이 필요한 서비스입니다.");
        }else{
            alert("댓글 수정에 실패했습니다.");
        }
    })
    .catch(function(error){
        console.error(error);
        alert("댓글 수정 중 오류가 발생했습니다.");
    });
}
</script>
</head>

<body>
<div class="detail-page">
    <div class="top-info">
        <c:choose>
            <c:when test="${not empty sessionScope.uiIdx}">
                <strong>${sessionScope.uiName}님</strong>&nbsp;|&nbsp;마지막 접속일 :
                <c:choose>
                    <c:when test="${not empty sessionScope.uiLastlogin}"><fmt:formatDate value="${sessionScope.uiLastlogin}" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
                    <c:otherwise>첫 로그인</c:otherwise>
                </c:choose>
                &nbsp;|&nbsp;<a href="<c:url value='/user/logout.do'/>">로그아웃</a>
            </c:when>
            <c:otherwise>비로그인 상태입니다.</c:otherwise>
        </c:choose>
    </div>

    <div class="detail-wrap">
        <div class="detail-top">
            <c:if test="${not empty sessionScope.uiIdx and sessionScope.uiIdx eq board.uiIdx}">
                <c:url var="updateUrl" value="/board/updateBoardView.do"><c:param name="biIdx" value="${board.biIdx}"/></c:url>
                <a class="detail-btn detail-update-btn" href="${updateUrl}">수정</a>
            </c:if>
            <a class="detail-btn" href="<c:url value='/board/selectBoardList.do'/>">리스트</a>
        </div>

        <table class="detail-table">
            <tr><th>카테고리</th><td>
                <c:choose>
                    <c:when test="${board.biCategory eq 'BACKEND'}">Backend</c:when>
                    <c:when test="${board.biCategory eq 'MAPPER'}">Mapper</c:when>
                    <c:when test="${board.biCategory eq 'JSP'}">JSP</c:when>
                    <c:when test="${board.biCategory eq 'SERVER'}">Server</c:when>
                    <c:otherwise>-</c:otherwise>
                </c:choose>
            </td></tr>

            <tr><th>타이틀</th><td>${board.biTitle}</td></tr>
            <tr><th>등록자</th><td>${board.uiName}</td></tr>
            <tr><th>게시등록일</th><td><fmt:formatDate value="${board.timeCreated}" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>

            <tr>
                <th>추천</th>
                <td>
                    <div class="recommend-area">
                        추천수 : <strong id="recommendCount">${board.recommendCount}</strong>
                        <button type="button" id="recommendButton" class="recommend-btn" onclick="toggleRecommend();">추천</button>
                    </div>
                </td>
            </tr>

            <tr><th>내용</th><td><div class="detail-content">${board.biContent}</div></td></tr>

            <tr>
                <th>첨부파일</th>
                <td>
                    <c:choose>
                        <c:when test="${not empty board.biFilename}">
                            <c:url var="downloadUrl" value="/board/downloadBoardFile.do"><c:param name="biIdx" value="${board.biIdx}"/></c:url>
                            <a class="detail-file-link" href="${downloadUrl}">${board.biFilename}</a>
                        </c:when>
                        <c:otherwise>첨부파일 없음</c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>

        <div class="comment-section">
            <h3 class="comment-title">댓글</h3>

            <div class="comment-write">
                <c:choose>
                    <c:when test="${not empty sessionScope.uiIdx}">
                        <textarea id="commentContent" placeholder="댓글을 입력해주세요."></textarea>
                    </c:when>
                    <c:otherwise>
                        <textarea id="commentContent" placeholder="로그인 후 댓글을 작성할 수 있습니다." readonly="readonly" onclick="checkCommentLogin();"></textarea>
                    </c:otherwise>
                </c:choose>
                <button type="button" class="comment-register-btn" onclick="insertComment();">댓글 등록</button>
            </div>

            <div id="commentListArea" class="comment-list-area">댓글을 불러오는 중입니다.</div>
        </div>
    </div>
</div>
</body>
</html>