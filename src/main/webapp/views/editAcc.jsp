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
		$('form[id="accRegForm"]').validate({
			rules : {
				firstName : 'required',
				lastName : 'required',
				email : {
					required : true,
					email : true,
				},
				password : {
					required : true,
					minlength : 5,
				},
				dob : 'required',
				gender : 'required',
				role : 'required',
				phno : 'required',
				ssn : 'required'
			},
			messages : {
				firstName : 'Please enter first name',
				lastName : 'please enter last name',
				email : 'Please enter a valid email',
				password : {
					required : 'Please enter password',
					minlength : 'Password must be at least 5 characters long'
				},
				dob : 'Please select dob',
				gender : 'Please select Gender',
				role : 'Please select a Role',
				phno : 'Please enter Phno',
				ssn : 'Please enter SSN'
			},
			submitHandler : function(form) {
				form.submit();
			}
		});
		$("#email").blur(function() {
			var enteredEmail = $("#email").val();
			$.ajax({
				url : window.location + "/validateEmail",
				data : "email=" + enteredEmail,
				success : function(result) {
					if (result == 'Duplicate') {
						$("#emailMsg").html("Email already registered.!!");
						$("#email").focus();
						$("#createAccBtn").prop("disabled",true);
					} else {
						$("#emailMsg").html("");
						$("#createAccBtn").prop("disabled",false);
					}
				}
			});
		});
		$("#datepicker").datepicker({
			changeMonth : true,
			changeYear : true,
			maxDate : new Date(),
			dateFormat : 'dd/mm/yy'
		});
	});
</script>
</head>
<%@ include file="header-inner.jsp" %>
<body>

	<font color='green'>${success}</font>
	<font color='red'>${failure}</font>

	<h2>Edit Account Details</h2>
	<form:form action="editAcc" method="POST" modelAttribute="plan"
		id="accRegForm">
		<table>
			<tr>
				<td>Account ID</td>
				<td><form:input path="accId" readonly="true"/></td>
			</tr>
			<tr>
				<td>First Name</td>
				<td><form:input path="firstName" /></td>
			</tr>
			<tr>
				<td>Last Name</td>
				<td><form:input path="lastName" /></td>
			</tr>
			<tr>
				<td>Email</td>
				<td><form:input path="email" id="email" readonly="true"/></td>
				<td><font color='red'><span id="emailMsg"></span></font></td>
			</tr>
			<tr>
				<td>Password</td>
				<td><form:input path="password" /></td>
			</tr>
			<tr>
				<td>Date Of Birth:</td>
				<td><form:input path="dob" id="datepicker" /></td>
			</tr>
			<tr>
				<td>Gender:</td>
				<td><form:radiobuttons path="gender" items="${gendersList}" /></td>
			</tr>
			<tr>
				<td>SSN:</td>
				<td><form:input path="ssn" /></td>
			</tr>
			<tr>
				<td>Phno:</td>
				<td><form:input path="phno" /></td>
			</tr>

			<tr>
				<td>Select Role:</td>
				<td><form:select path="role" items="${rolesList}" /></td>
			</tr>
			<tr>
				<td><input type="Submit" value="Update" id="createAccBtn"/></td>
			</tr>
		</table>
	</form:form>

</body>
</html>