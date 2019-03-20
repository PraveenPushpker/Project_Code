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

		$('form[id="forgotPwdForm"]').validate({
			rules : {
				
				email : {
					required : true,
					email : true
				}
			},//rules
			messages : {
				email : 'Please enter a valid email'
			},
			submitHandler : function(form) {
				form.submit();
			}
		});//validate
		
	});
</script>
</head>

<body>
	
	<font color='green'>${success}</font>
	<font color='red'>${failure}</font>

	<h2>Forgot Password</h2>
	<form action="forgotPwd" method="POST" id="forgotPwdForm">
		<table>
			<tr>
				<td>Email</td>
				<td><input type="text" name="email" placeholder="enter your email"/></td>
			</tr>
			<tr></tr>
			<tr>
				<td>
					<td><input type="Submit" value="SUBMIT"/></td>
				</td>
			</tr>
		</table>
	</form>

</body>
</html>