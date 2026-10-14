<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link rel="stylesheet" href="<c:url value='/css/egovframework/board.css?v=3'/>"/>
<link rel="stylesheet" href="<c:url value='/css/egovframework/boardTree.css?v=3'/>"/>
<link rel="stylesheet" href="<c:url value='/css/egovframework/ztree/zTreeStyle/zTreeStyle.css'/>"/>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.4.4.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.core.min.js'/>"></script>

<script type="text/javascript">
var contextPath='${pageContext.request.contextPath}';
var selectedCategoryCode='<c:out value="${searchVO.categoryCode}"/>';
var selectedMinRecommend='<c:out value="${searchVO.minRecommend}"/>';
var loginYn=false;
<c:if test="${not empty sessionScope.uiIdx}">loginYn=true;</c:if>

function fn_egov_select_linkPage(pageNo){
    document.boardForm.pageIndex.value=pageNo;
    document.boardForm.submit();
}

function fnSearchBoard(){
    document.boardForm.pageIndex.value=1;
    return true;
}

function moveBoardForm(){
    if(!loginYn){
        alert("로그인이 필요한 서비스입니다.");
        return;
    }
    location.href=contextPath+"/board/boardForm.do";
}

function clearTreeFilter(){
    document.boardForm.categoryCode.value="";
    document.boardForm.minRecommend.value="";
    document.boardForm.pageIndex.value=1;
    document.boardForm.submit();
}

var boardTreeSetting={
    data:{
        simpleData:{
            enable:true,
            idKey:"treeId",
            pIdKey:"parentTreeId",
            rootPId:"0"
        },
        key:{name:"treeName"}
    },
    view:{selectedMulti:false},
    callback:{
        onClick:onBoardTreeClick,
        onExpand:onBoardTreeExpand
    }
};

function initBoardTree(){
    $.ajax({
        url:contextPath+"/tree/selectBoardTreeList.do",
        type:"GET",
        dataType:"json",
        cache:false,
        success:function(data){
            if(data.result!=true){
                alert(data.resultMsg);
                return;
            }
            $.fn.zTree.init($("#boardTree"),boardTreeSetting,data.treeList);
            openTreeRoot();
            restoreTreeSelection();
        },
        error:function(xhr,status,error){
            console.log("HTTP STATUS : "+xhr.status);
            console.log("STATUS : "+status);
            console.log("ERROR : "+error);
            console.log("RESPONSE : "+xhr.responseText);
            alert("게시판 Tree 요청 중 오류가 발생했습니다.");
        }
    });
}

function openTreeRoot(){
    var treeObj=$.fn.zTree.getZTreeObj("boardTree");
    if(treeObj==null)return;
    var rootNodes=treeObj.getNodes();
    if(rootNodes.length>0)treeObj.expandNode(rootNodes[0],true,false,false);
}

function onBoardTreeClick(event,treeId,treeNode){
    var treeObj=$.fn.zTree.getZTreeObj(treeId);

    if(treeNode.nodeType=="ROOT"){
        clearTreeFilter();
        return;
    }

    if(treeNode.nodeType=="CATEGORY"){
        treeObj.expandNode(treeNode,!treeNode.open,false,true);
        return;
    }

    if(treeNode.nodeType=="RECOMMEND"){
        document.boardForm.categoryCode.value=treeNode.categoryCode;
        document.boardForm.minRecommend.value=treeNode.minRecommend;
        document.boardForm.pageIndex.value=1;
        document.boardForm.submit();
        return;
    }

    if(treeNode.nodeType=="POST"){
        location.href=contextPath+"/board/selectBoardDetail.do?biIdx="+treeNode.biIdx;
    }
}

function onBoardTreeExpand(event,treeId,treeNode){
    if(treeNode.nodeType=="RECOMMEND")loadRecommendPostNodes(treeNode);
}

function loadRecommendPostNodes(treeNode){
    if(treeNode.postLoaded==true)return;
    treeNode.postLoaded=true;

    $.ajax({
        url:contextPath+"/tree/selectRecommendPostList.do",
        type:"GET",
        dataType:"json",
        cache:false,
        data:{
            parentTreeId:treeNode.treeId,
            categoryCode:treeNode.categoryCode,
            minRecommend:treeNode.minRecommend
        },
        success:function(data){
            if(data.result!=true){
                treeNode.postLoaded=false;
                alert(data.resultMsg);
                return;
            }

            var treeObj=$.fn.zTree.getZTreeObj("boardTree");
            var postNodes=data.treeList;

            if(postNodes==null||postNodes.length==0){
                postNodes=[{
                    treeId:"EMPTY_"+treeNode.treeId,
                    parentTreeId:treeNode.treeId,
                    treeName:"해당 게시글이 없습니다.",
                    nodeType:"EMPTY",
                    isParent:false
                }];
            }
            treeObj.addNodes(treeNode,postNodes);
        },
        error:function(xhr,status,error){
            treeNode.postLoaded=false;
            console.log(xhr.responseText);
            alert("추천 게시글 요청 중 오류가 발생했습니다.");
        }
    });
}

function restoreTreeSelection(){
    if(selectedCategoryCode==""||selectedMinRecommend=="")return;

    var treeObj=$.fn.zTree.getZTreeObj("boardTree");
    if(treeObj==null)return;

    var allNodes=treeObj.transformToArray(treeObj.getNodes());

    for(var i=0;i<allNodes.length;i++){
        var node=allNodes[i];

        if(node.nodeType=="RECOMMEND"
            &&node.categoryCode==selectedCategoryCode
            &&String(node.minRecommend)==String(selectedMinRecommend)){

            var categoryNode=node.getParentNode();
            var rootNode=null;

            if(categoryNode!=null)rootNode=categoryNode.getParentNode();
            if(rootNode!=null)treeObj.expandNode(rootNode,true,false,false);
            if(categoryNode!=null)treeObj.expandNode(categoryNode,true,false,false);

            treeObj.selectNode(node);
            treeObj.expandNode(node,true,false,false);
            loadRecommendPostNodes(node);
            break;
        }
    }
}

$(document).ready(function(){
    initBoardTree();
});
</script>
</head>

<body>
<div class="board-wrap">
    <div class="top-info">
        <c:choose>
            <c:when test="${not empty sessionScope.uiIdx}">
                ${sessionScope.uiName}님
                <c:if test="${not empty sessionScope.uiLastlogin}">
                    &nbsp;|&nbsp; 이전 로그인 : <fmt:formatDate value="${sessionScope.uiLastlogin}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </c:if>
            </c:when>
            <c:otherwise>게시판에 오신 것을 환영합니다.</c:otherwise>
        </c:choose>
    </div>

    <div class="board-toolbar">
        <form id="boardForm" name="boardForm" class="search-area" method="get" action="<c:url value='/board/selectBoardList.do'/>" onsubmit="return fnSearchBoard();">
            <input type="hidden" name="pageIndex" value="${searchVO.pageIndex}"/>
            <input type="hidden" name="categoryCode" value="${searchVO.categoryCode}"/>
            <input type="hidden" name="minRecommend" value="${searchVO.minRecommend}"/>
            <input type="text" name="searchKeyword" value="${searchVO.searchKeyword}"/>
            <button type="submit">검색</button>
        </form>

        <div class="button-area">
            <c:choose>
                <c:when test="${not empty sessionScope.uiIdx}">
                    <a class="btn-link" href="<c:url value='/user/logout.do'/>">로그아웃</a>
                </c:when>
                <c:otherwise>
                    <a class="btn-link" href="#" onclick="openLoginModal();return false;">로그인</a>
                    <a class="btn-link btn-register" href="#" onclick="openJoinModal();return false;">회원가입</a>
                </c:otherwise>
            </c:choose>
            <a class="btn-link btn-register" href="#" onclick="moveBoardForm();return false;">등록</a>
        </div>
    </div>

    <div class="board-layout">
        <div class="board-tree-area">
            <div class="board-tree-title">게시판 트리</div>
            <ul id="boardTree" class="ztree board-tree"></ul>
        </div>

        <div class="board-list-area">
            <c:if test="${not empty searchVO.categoryCode and searchVO.minRecommend != null}">
                <div class="tree-filter-info">
                    현재 선택 :
                    <strong>
                        <c:choose>
                            <c:when test="${searchVO.categoryCode=='BACKEND'}">Backend</c:when>
                            <c:when test="${searchVO.categoryCode=='MAPPER'}">Mapper</c:when>
                            <c:when test="${searchVO.categoryCode=='JSP'}">JSP</c:when>
                            <c:when test="${searchVO.categoryCode=='SERVER'}">Server</c:when>
                            <c:otherwise>${searchVO.categoryCode}</c:otherwise>
                        </c:choose>
                        / ${searchVO.minRecommend}추 이상
                    </strong>
                    <button type="button" onclick="clearTreeFilter();">전체보기</button>
                </div>
            </c:if>

            <table class="board-table">
                <colgroup>
                    <col style="width:7%;"/>
                    <col style="width:12%;"/>
                    <col style="width:27%;"/>
                    <col style="width:20%;"/>
                    <col style="width:14%;"/>
                    <col style="width:8%;"/>
                    <col style="width:12%;"/>
                </colgroup>
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>카테고리</th>
                        <th>타이틀</th>
                        <th>게시등록일</th>
                        <th>등록자</th>
                        <th>추천수</th>
                        <th>상세보기</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="board" items="${resultList}">
                        <tr>
                            <td>${board.biIdx}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${board.biCategory=='BACKEND'}">Backend</c:when>
                                    <c:when test="${board.biCategory=='MAPPER'}">Mapper</c:when>
                                    <c:when test="${board.biCategory=='JSP'}">JSP</c:when>
                                    <c:when test="${board.biCategory=='SERVER'}">Server</c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="board-title-cell">${board.biTitle}</td>
                            <td><fmt:formatDate value="${board.timeCreated}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>${board.uiName}</td>
                            <td><c:choose><c:when test="${not empty board.recommendCount}">${board.recommendCount}</c:when><c:otherwise>0</c:otherwise></c:choose></td>
                            <td><a href="<c:url value='/board/selectBoardDetail.do'><c:param name='biIdx' value='${board.biIdx}'/></c:url>">상세보기</a></td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty resultList}">
                        <tr><td colspan="7">등록된 게시글이 없습니다.</td></tr>
                    </c:if>
                </tbody>
            </table>

            <div class="paging-wrap">
                <ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="fn_egov_select_linkPage"/>
            </div>
        </div>
    </div>
</div>

<%@ include file="../user/authModal.jsp" %>
</body>
</html>