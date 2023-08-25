<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 수정</title>
</head>
<body>
	<h1>공지사항 수정</h1>
	<form action="/notice/update.kh" method="post" enctype="multipart/form-data">
<!-- 		수정할때 , 리다이렉트 될 때 사용 -->
		<input type="hidden" name="noticeNo" value="${notice.noticeNo }">
<!-- 		기존 업로드 파일 체크할 때 사용 -->
		<input type="hidden" name="noticeFilename" value="${notice.noticeFilename }">
		<input type="hidden" name="noticeFileRename" value="${notice.noticeFileRename }">
		<input type="hidden" name="noticeFilepath" value="${notice.noticeFilepath }">
		<input type="hidden" name="noticeFilelength" value="${notice.noticeFilelength }">
		<ul>
			<li>
				<label>제목</label>
				<input type="text" name="noticeSubject" value="${notice.noticeSubject}">
			</li>
			<li>
				<label>작성자</label>
				<input type="text" name="noticeWriter" value="${notice.noticeWriter}" readonly>
			</li>
			<li>
				<label>내용</label>
				<textarea rows="4" cols="50"  name="noticeContent">${notice.noticeContent}</textarea>
			</li>
			<li>
				<label>첨부파일</label>
<!-- 				String으로 받을 수 없어서 변환작업이 필요함 -->
				<a href="../resources/nuploadFiles/${notice.noticeFilename }" download>${notice.noticeFilename }</a>  
				<input type="file" name="uploadFile">
			</li>
		</ul>
		
		<div>
			<input type="submit" value="수정">
		</div>
	</form>
</body>
</html>