<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Home</title>
</head>
<body>
	<h1>Hello world!</h1>
	<c:if test="${memberId eq null }">
		<form action="member/login.kh" method="post">
			ID: <input type="text" name="memberId"><br> 
			PW: <input type="password" name="memberPw"><br> 
			<input type="submit" value="로그인">
		</form>
	</c:if>
	<c:if test="${memberId ne null }">
		${memberName }님 환영합니다. <a href="/member/logout.kh">로그아웃</a>
		
		<form action="/member/mypage.kh" method="post">
			<input type="hidden" name="memberId" value="${memberId }">
			<input type="submit" value="마이페이지"> 
		</form>
		<br>
<!-- 		a태그 .kh뒤에 ?가 필요한지 고민, 쿼리문 생각해보면 필요함  -->
		<a href="/member/mypage.kh">마이페이지</a>
		<a href="/board/list.kh">게시판</a>
	</c:if>
</body>
</html>
