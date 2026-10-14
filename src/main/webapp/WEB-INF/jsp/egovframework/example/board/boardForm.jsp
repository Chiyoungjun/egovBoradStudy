<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><c:choose><c:when test="${mode eq 'update'}">게시글 수정</c:when><c:otherwise>게시글 등록</c:otherwise></c:choose></title>
<link rel="stylesheet" href="<c:url value='/css/egovframework/board.css'/>"/>
</head>
<body>
<div class="form-page">
    <div class="top-info">
        <strong>${sessionScope.uiName}님</strong>&nbsp;|&nbsp;마지막 접속일 :
        <c:choose>
            <c:when test="${not empty sessionScope.uiLastlogin}"><fmt:formatDate value="${sessionScope.uiLastlogin}" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
            <c:otherwise>첫 로그인</c:otherwise>
        </c:choose>
        &nbsp;|&nbsp;<a href="<c:url value='/user/logout.do'/>">로그아웃</a>
    </div>

    <c:choose>
        <c:when test="${mode eq 'update'}"><c:set var="formAction" value="/board/updateBoard.do"/></c:when>
        <c:otherwise><c:set var="formAction" value="/board/insertBoard.do"/></c:otherwise>
    </c:choose>
    <c:url var="submitUrl" value="${formAction}"/>

    <div class="form-wrap">
        <div class="form-top"><a class="form-list-btn" href="<c:url value='/board/selectBoardList.do'/>">리스트</a></div>

        <form method="post" action="${submitUrl}" enctype="multipart/form-data">
            <c:if test="${mode eq 'update'}"><input type="hidden" name="biIdx" value="${board.biIdx}"/></c:if>

            <div class="form-content">
                <div class="form-row">
                    <div class="form-label">카테고리</div>
                    <select class="form-input" name="biCategory" required>
                        <option value="">선택</option>
                        <option value="BACKEND" <c:if test="${board.biCategory eq 'BACKEND'}">selected="selected"</c:if>>Backend</option>
                        <option value="MAPPER" <c:if test="${board.biCategory eq 'MAPPER'}">selected="selected"</c:if>>Mapper</option>
                        <option value="JSP" <c:if test="${board.biCategory eq 'JSP'}">selected="selected"</c:if>>JSP</option>
                        <option value="SERVER" <c:if test="${board.biCategory eq 'SERVER'}">selected="selected"</c:if>>Server</option>
                    </select>
                </div>

                <div class="form-row">
                    <div class="form-label">제목</div>
                    <input type="text" class="form-input" name="biTitle" value="${board.biTitle}" required/>
                </div>

                <div class="form-row form-row-content">
                    <div class="form-label">내용</div>
                    <textarea class="form-textarea" name="biContent" required>${board.biContent}</textarea>
                </div>

                <div class="form-row">
                    <div class="form-label">첨부파일</div>
                    <div class="form-file-area">
                        <input type="file" name="uploadFile"/>
                        <c:if test="${mode eq 'update' and not empty board.biFilename}"><div class="current-file">기존 파일 : ${board.biFilename}</div></c:if>
                    </div>
                </div>

                <div class="form-button-area">
                    <c:choose>
                        <c:when test="${mode eq 'update'}"><button type="submit" class="form-submit-btn">수정</button></c:when>
                        <c:otherwise><button type="submit" class="form-submit-btn">등록</button></c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>