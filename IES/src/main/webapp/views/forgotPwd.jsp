<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<title>Forgot Password Page</title>
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

	$('form[id="forgotPwd"]').validate({
		rules : {
			email : 'required',
		},
		messages : {
			email : 'Please enter a valid email',
		},
		submitHandler : function(form) {
			form.submit();
		}
	});
	});
</script>
</head>

<body>

	<font color='green'>${success}</font>
	<font color='red'>${failure}</font>

	<h2>Login</h2>
	<form:form action="forgotPwd" method="POST" id="forgotPwd">
		<table>
			<tr>
				<td>Email</td>
				<td><input type="text" name="email"></td>
			</tr>
			<tr>
				<td><input type="Submit" value="Validate" id="validateBtn" /></td>
			</tr>

		</table>


	</form:form>


</body>
</html>