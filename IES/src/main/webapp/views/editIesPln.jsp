<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset=ISO-8859-1">
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

		$('form[id="iesplanForm"]').validate({
			rules : {
				planName : 'required',
				planDesc : 'required',
				startDate : 'required',
				endDate : 'required'
			},
			messages : {
				planName : 'Please enter Plan Name',
				planDesc : 'Please enter Description of Plan',
				startDate : 'Please select psd',
				endDate : 'Please select ped'
			},
			submitHandler : function(form) {
				form.submit();
			}
		});

		$("#planName").blur(function() {
			var enteredPlanName = $("#planName").val();
			$.ajax({
				url : window.location + "/validatePlanName",
				data : "planName=" + enteredPlanName,
				success : function(result) {
					if (result == 'Duplicate') {
						$("#planNameMsg").html("Plan already registered.!!");
						$("#planName").focus();
						$("#createPlnBtn").attr("disabled", true);
					} else {
						$("#planNameMsg").html("");
						$("#createPlnBtn").attr("disabled", false);
					}

				}
			});

			$("#datepicker").datepicker({
				numberOfMonths : 1,
				onSelect : function(selected) {
					var dt = new Date(selected);
					dt.setDate(dt.getDate());
					$("#datepicker1").datepicker("option", "minDate", dt);
				}
			});
			$("#datepicker1").datepicker({
				numberOfMonths : 1,
				onSelect : function(selected) {
					var dt = new Date(selected);
					dt.setDate(dt.getDate());
					$("#datepicker").datepicker("option", "maxDate", dt);
				}
			});

		});
	});
</script>
</head>
<%@ include file="header-inner.jsp"%>
<body>


	<font color='green'>${success}</font>
	<font color='red'>${failure}</font>

	<h2>Create Plan</h2>
	<form:form action="#" method="POST" modelAttribute="account"
		id="iesplanForm">
		<table>
			<tr>
				<td>Plan ID</td>
				<td><form:input path="planId" readonly="true" /></td>
			</tr>
			<tr>
				<td>Plan Name</td>
				<td><form:input path="planName" id="planName" readonly="true" /></td>
				<td><font color='red'><span id="planNameMsg"></span></font></td>
			</tr>
			<tr>
				<td>Plan Description</td>
				<td><form:input path="planDesc" /></td>
			</tr>
			<tr>
				<td>Plan Start Date</td>
				<td><form:input path="startDate" id="datepicker" /></td>
			</tr>
			<tr>
				<td>Plan End Date</td>
				<td><form:input path="endDate" id="datepicker1" /></td>
			</tr>
			<tr>


				<td><input type="Submit" value="Update" id="createPlnBtn" /></td>
				</td>
			</tr>

		</table>


	</form:form>


</body>
</html>