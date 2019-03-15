<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script
	src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>
<script>
$(function() {

	$('form[id="crtplanForm"]').validate({
		rules : {
			PlanName : 'required',
			PlanDescription : 'required',
			psd : 'required',
			ped : 'required'
		},
		messages : {
			PlanName : 'Please enter Plan Name',
			PlanDescription : 'Please enter Description of Plan',
			psd : 'Please select psd',
			psd : 'Please select ped'
		},
		submitHandler : function(form) {
			form.submit();
		}
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
<body>

	<font color='green'>${success}</font>
	<font color='red'>${failure}</font>

	<h2>Create Plan</h2>
	<form action="crtPlan" method="POST" modelAttribute="crtModel"
		id="crtplanForm">
		<table>
			<tr>
				<td>Plan Name</td>
				<td><form:input path="PlanName" /></td>
			</tr>
			<tr>
				<td>Plan Description</td>
				<td><form:input path="PlanDescription" /></td>
			</tr>
			<tr>
				<td>Plan Start Date</td>
				<td><form:input path="psd" id="datepicker" /></td>
			</tr>
			<tr>
				<td>Plan End Date</td>
				<td><form:input path="ped" id="datepicker" /></td>
			</tr>
			<tr>
				<td><input type="Submit" value="Create Plan" /></td>
			</tr>

		</table>


	</form>


</body>
</html>