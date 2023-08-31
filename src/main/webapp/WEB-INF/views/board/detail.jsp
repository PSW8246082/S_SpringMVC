<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 조회</title>
</head>
<body>
	<h1>게시글 조회</h1>
	
		<ul>
			<li>
				<label>제목</label>
				<input type="text" name="boardTitle" value="${board.boardTitle }" readonly>
			</li>
			<li>
				<label>작성자</label>
				<span>"${board.boardWriter }"</span>
			</li>
			<li>
				<label>내용</label>
				<p>"${board.boardContent }"</p>
			</li>
			<li>
				<label>첨부파일</label>
<!-- 				String으로 받을 수 없어서 변환작업이 필요함 -->
				<!-- 첨부파일 화면에 띄울때 -->
				<img alt="첨부파일" src="../resources/buploadFiles/${board.boardFilename }">
				<!-- 다운로드 받을 때 -->
				<a href="${board.boardFilepath }" download>${board.boardFilename }</a>  
				<c:if test="${not empty board.boardFilename }">
					<a href="#">삭제</a>
				</c:if>
			</li>
		</ul>
		
		<c:url  var="boardDelUrl" value = "/board/delete.kh">
			<c:param name="boardNo" value="${board.boardNo}"></c:param>
			<c:param name="boardWriter" value="${board.boardWriter}"></c:param>
		</c:url>
		
		<c:url  var="modifyUrl" value = "/board/modify.kh">
			<c:param name="boardNo" value="${board.boardNo}"></c:param>
			<c:param name="boardWriter" value="${board.boardWriter}"></c:param>
		</c:url>
		
		<div>
			<c:if test="${board.boardWriter eq memberId }">
				<button type="button" onclick="showModifyPage('${modifyUrl }')">수정하기</button>
				<button type="button" onclick="deleteBoard('${boardDelUrl }')">삭제하기</button>
			</c:if>
				<button type="button" onclick="showBoardList()">목록으로</button>
				<button type="button" onclick="javascript:history.go(-1);">뒤로가기</button>
		</div>
<br><br>
<hr>
<br><br>


	<!--댓글등록 -->
	<form action="/reply/add.kh" method="post">
	<input type="hidden" name="refBoardNo" value="${board.boardNo }">
		<table width="500" border="1">
			<tr>
				<td><textarea rows="3" cols="55" name="replyContent"></textarea></td>
				<td><input type="submit" value="완료"></td>
			</tr>
		</table>
	</form>

<br>

	<!--댓글목록 -->
		<table width="500" border = "1">
		<c:forEach var="reply" items="${rList}">
			<tr> 
				<td>${reply.replyWriter } </td>
				<td>${reply.replyContent } </td>
				<td>${reply.rCreateDate }</td>
				<td>
					<a href="javascript:void(0);" onclick="showModifyForm(this, '${reply.replyContent }');">수정하기</a>
					<c:url var="delUrl" value="/reply/delete.kh">
					<c:param name="replyNo" value="${reply.replyNo}"></c:param>
					<!-- 내것(작성자) 것만 지우도록 하기위해서 추가함 -->
					<c:param name="replyWriter" value="${reply.replyWriter}"></c:param>
					<!-- 성공하면 detail로 가기 위한 boardNo 세팅 -->
					<c:param name="refBoardNo" value="${reply.refBoardNo}"></c:param>
					</c:url>
					<a href="javascript:void(0)" onclick="deleteReply('${delUrl }');">삭제하기</a>
				 </td>
			</tr>
				<!-- 방법1 -->
			<tr id="replyModifyForm" style="display:none">
<!-- 			form태그 이용방식 -->
<!-- 			<form action="/reply/update.kh" method="post"> -->
<%-- 			<input type="hidden" name="replyNo" value="${reply.replyNo }"> --%>
<%-- 			<input type="hidden" name="refBoardNo" value="${reply.refBoardNo }"> --%>
<%-- 				<td colspan="3"><input id="replyContent" type="text" size="50" name ="replyContent" value="${reply.replyContent }"></td> --%>
<!-- 				<td><input type="submit" value="완료"></td>  -->
<!-- 		     </form> -->
			 
			 
			 <!-- 방법2 -->
			 <td colspan="3"><input id="replyContent" type="text" size="50" name ="replyContent" value="${reply.replyContent }"></td>				
			<td><input type="button" onclick="replyModify(this,'${reply.replyNo }','${reply.refBoardNo }');" value="완료"></td>
			
			
			</tr>
			</c:forEach>
		</table>
		
		
		
		
		
		
		
		
		
		<script>
		
// 	################################# 게시글 ############################################ 		
		
		//게시글 수정
		function showModifyPage(boardUrl) {
// 			alert("test");
			location.href = boardUrl;
		}
		
		//게시글 삭제
		const deleteBoard = (boardUrl) => {
			//alert("test");
			location.href = boardUrl;
		}
		
		
		//목록으로 이동하기
		function showBoardList() {
// 			alert("test");
			location.href="/board/list.kh";
		}
		
		
		
		
// 		################################# 댓글 ############################################ 
		//댓글삭제
			function deleteReply(url){
				//DELETE FROM REPLY_TBL WHERE REPLY_NO = 샵{replyNo}, AND R_STATUS = 'Y'
				//UPDEATE REPLY_TBL SET R_STTUS = 'N' WHERE REPLY_NO = 샵{replyNo }
				//alert(url);
				location.href=url;
			}	
			
	
			function replyModify(obj, replyNo, refBoardNo) {
// 				alert("test");
				//DOM프로그래밍을 이용하는 방법
				const form = document.createElement("form");
				form.action = "/reply/update.kh";
				form.method = "post";
				
				const input = document.createElement("input");
				input.type = "hidden";
				input.value= replyNo;
				input.name = "replyNo";
				
				const input2 = document.createElement("input");
				input2.type= "hidden";
				input2.value= refBoardNo;
				input2.name= "refBoardNo";
				
				const input3 = document.createElement("input");
				input3.type= "text";
				//여기를 this를 이용하여 수정해주세요.
// 				input3.value= document.querySelector("#replyContent").value;
				//this를 이용하여 내가 원하는 노드 찾기
				//방법1
				input3.value = obj.parentElement.previousElementSibling.childNodes[0].value;
				//방법2
				//input3.value = obj.parentElement.previousElementSibling.children[0].value;
				input3.name= "replyContent";
				
				form.appendChild(input);
				form.appendChild(input2);
				form.appendChild(input3);
				
				document.body.appendChild(form);
				form.submit();
			}
			
			
			
			function showModifyForm(obj, replyContent) {
	
// 		        방법1 : HTML태그, display:none 사용하는 방법
// 				alert("test");
				obj.parentElement.parentElement.nextElementSibling.style.display="";
			
//	 			방법2 : DOM프로그래밍을 이용하는 방법

//	 			<tr id="replyModifyForm" style="display:none">
//	 				<td colspan="3"><input type="text" size="50" value="${reply.replyContent }"></td>
//	 				<td><input type="button" value="완료"></td>
//	 			</tr>


// 				const trTag = document.createElement("tr");
// 				const tdTag1 = document.createElement("td");
// 				tdTag1.colSpan = 3;
// 				const inputTag1 = document.createElement("input");
// 				inputTag1.type="text";
// 				inputTag1.size=50;
// 				inputTag1.value= replyContent;
// 				tdTag1.appendChild(inputTag1);
// 				const tdTag2 = document.createElement("td");
// 				const inputTag2 = document.createElement("input");
// 				inputTag2.type="button";
// 				inputTag2.value="완료";
// 				tdTag2.appendChild(inputTag2);
// 				trTag.appendChild(tdTag1);
// 				trTag.appendChild(tdTag2);
// // 				console.log(trTag);
// 				//클릭한 a를 포함하고 있는 tr 다음에 수정폼이 있는 tr 추가하기
// 				const prevTrTag = obj.parentElement.parentElement;
// // 				if(prevTrTag.nextElementSibing == null || !prevTrTag.nextElementSibling.querySelector("input")) 오류
// 				prevTrTag.parentNode.insertBefore(trTag, prevTrTag.nextSibling);
			
			}



			
			

			
		</script>

</body>
</html>