<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<title>Insert title here</title>
<style>
.error {
	color: #FF0000
}
</style>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script
	src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>
<script>
	$(function() {

		$('form[id="loginForm"]').validate({
			rules : {
				email : {
					required : true,
					email : true
				},
				pwd: {
			        required: true,
			        minlength: 5
			      }
			},
			messages : {
				email : 'Please enter a valid email',
				pwd : {
					required : 'Please enter password',
					minlength : 'Password must be at least 5 characters long'
				}
			},
			submitHandler : function(form) {
				form.submit();
			}
		});

	});
</script>
</head>
<body>

	<font color='red'>${failure}</font>

	<h2>Login</h2>
	<form:form action="login" method="POST" id="loginForm">
		<table>
			<tr>
				<td>Email:</td>
				<td><input type="email" name="email" id="email" /></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" name="pwd" id="password" /></td>
			</tr>

			<tr>
				<td></td>
				<td><input type="Submit" value="Login" id="loginBtn" /></td>
			</tr>
		</table>
	</form:form>
		
	<a href="forgotPwd">Forgot Password?</a>
</body>
</html>