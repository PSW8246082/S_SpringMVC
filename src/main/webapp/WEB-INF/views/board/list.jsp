<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
<link rel="stylesheet" href="../resources/css/board/board.css"> 
</head>
<body>
<h1><a href="/index.jsp">HOME</a></h1>
	<h1>게시글 목록</h1>
	<table>
		<colgroup>
			<col width="40px"></col>
			<col width="400px"></col>
			<col width="80px"></col>
			<col width="120px"></col>
			<col width="40px"></col>
			<col width="30px"></col>
		 </colgroup>
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성날짜</th>
				<th>첨부파일</th>
				<th>조회수</th>
			</tr>
		</thead>
		<tbody>
<!-- 		list데이터는 items에 넣었고 var에서 설정한 변수로 list데이터에서 꺼낸 값을 사용하고 i의 값은 varStatus로 사용 -->
<%-- 		<c:forEach begin="1" end="10" var="board" items="${nList }" varStatus="i"> --%>
		<c:forEach var="board" items="${bList }" varStatus="i">
			<tr>
				<td>${i.count }</td>
				<c:url var="detailUrl" value="/board/detail.kh">
				<c:param name="boardNo" value="${board.boardNo }"></c:param>
				</c:url>
				<td><a href="${detailUrl }">${board.boardTitle }</a></td>
<%-- 				<td><a href="/board/detail.kh?boardNo=${boardNo }">${board.boardSubject }</a></td> --%>
				<td>${board.boardWriter }</td>
				<td>
				<fmt:formatDate pattern="YYYY-MM-dd" value="${board.bCreateDate}"/>
<%-- 				${board.nCreateDate} --%>
				</td>
				<td>
				<c:if test="${!empty board.boardFilename }">o</c:if>
				<c:if test="${empty board.boardFilename }">x</c:if>
				</td>
				
				<td>
				<fmt:formatNumber pattern="##,###,###" value="${board.boardCount }"></fmt:formatNumber>
				</td>
			</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr align="center">
				<td colspan="6">
				
				<c:if test="${pInfo.startNavi != 1 }">
				<c:url var="prevUrl" value="/board/list.kh">
				<c:param name="page" value=""></c:param>
				</c:url>
				
				<a href="${prevUrl }">이전</a>
				</c:if>
				
				<c:forEach begin="${pInfo.startNavi }" end="${pInfo.endNavi }" var="p">
				<c:url var="pageUrl" value="/board/list.kh">
					<c:param name="page" value="${p }"></c:param>
				</c:url>
				<a href="${pageUrl }">${p }</a>&nbsp;
<%-- 					<a href="/board/list.kh?page=${p }">${p }</a>&nbsp; --%>
				</c:forEach>
<%-- 				${pInfo } --%>


				<c:if test="${pInfo.endNavi != pInfo.naviTotalCount }">
				<c:url var="nextUrl" value="/board/list.kh"> 
				<c:param name="page" value="${pInfo.endNavi + 1 }"></c:param>
				</c:url>
				<a href="${nextUrl }">다음</a>
				</c:if>
				
				</td>
			</tr>
			<tr>
				<td colspan="5">
					<form action="/board/search.kh" method="get">
						<select name="searchCondition">
							<option value="all">전체</option>
							<option value="writer">작성자</option>
							<option value="title">제목</option>
							<option value="content">내용</option>
						</select>
						<input type="text" name="searchKeyword" placeholder="검색어를 입력하세요.">
						<input type="submit" value="검색">
					</form>
				</td>
				<td> 
					<button type="button" onclick="showRegisterForm();">글쓰기</button>
				</td>
			</tr>
		</tfoot>
	</table>
	
	<script>
		const showRegisterForm = () => {
			location.href="/board/write.kh";
		}
	</script>
</body>
</html>