<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입폼</title>
</head>
<body>
	<form action="/member/register.kh" method="post">
		<fieldset>
			<legend>회원가입</legend>
			<ul>
				<li><label>아이디</label> 
				<input type="text" name="memberId">
				</li>
				<li>
			<label>비밀번호*</label>
			<input type="password" name="memberPw">
		</li>
		<li>
			<label>이름*</label>
			<input type="text" name="memberName">
		</li>
		<li>
			<label>나이*</label>
			<input type="text" name="memberAge">
		</li>
		<li>
			<label>성별*</label>
			남<input type="radio" name="memberGender" value="M">
			여<input type="radio" name="memberGender" value="F">
		</li>
		<li>
			<label>이메일*</label>
			<input type="text" name="memberEmail">
		</li>
		<li>
			<label>전화번호*</label>
			<input type="text" name="memberPhone">
		</li>
		<li>
			<label>주소*</label>
			<input type="text" id="memberAddr" name="memberAddr">
			<input type="button" onclick="sample4_exeDaumPostcode()" value="우편번호찾기">
		</li>
		<li>
			<label>취미*</label>
			<input type="text"  name="memberHobby">
		</li>
			</ul>
		</fieldset>
		<div>
		<input type="submit" value="가입">
		</div>
	</form>
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script>
	function sample4_exeDaumPostcode() {
		new daum.Postcode({
			oncomplete: function(data) {
				document.querySelector("#memberAddr")
				.value= data.zonecode + ", " + data.autoJibunAddress + ", " + data.buildingName;
			}	
		}).open();
	}
	</script>
</body>
</html>