<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty commentList}">
    <div class="comment-empty">
        등록된 댓글이 없습니다.
    </div>
</c:if>

<c:forEach var="comment" items="${commentList}">

    <div class="comment-item">

        <div class="comment-info">

            <span class="comment-writer">
                ${comment.uiName}
            </span>

            <span class="comment-date">
                <fmt:formatDate
                    value="${comment.timeCreated}"
                    pattern="yyyy-MM-dd HH:mm:ss" />
            </span>

        </div>

        <c:choose>

            <c:when test="${not empty sessionScope.uiIdx and sessionScope.uiIdx eq comment.uiIdx}">

                <div class="comment-edit-area">

                    <textarea
                        id="comment_${comment.biIdx}">${comment.biContent}</textarea>

                    <button type="button"
                            class="comment-edit-btn"
                            onclick="updateComment('${comment.biIdx}');">
                        수정
                    </button>

                </div>

            </c:when>

            <c:otherwise>

                <div class="comment-content">
                    ${comment.biContent}
                </div>

            </c:otherwise>

        </c:choose>

    </div>

</c:forEach>