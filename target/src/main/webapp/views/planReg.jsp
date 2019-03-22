<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Plan Registration</title>
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
		$('form[id="planRegForm"]').validate({
			rules : {
				planName : 'required',
				planDescription : 'required',
				planStartDate : 'required',
				planEndDate : 'required',

			},
			messages : {
				planName : 'Please enter plan name',
				planDescription : 'please enter description details',
				planStartDate : 'Please select start date',
				planEndDate : 'Please select end date',
			},
			submitHandler : function(form) {
				form.submit();
				
			}
		});

		$("#planName").blur(function() {
			var enteredPlan = $("#planName").val();

			$.ajax({
				url : window.location + "/validatePlan",
				data : "plan=" + enteredPlan,
				success : function(result) {
					if (result == 'Duplicate') {
						$("#planMsg").html("Plan already Existing.!!");
						$("#planName").focus();
						//$("#createPlanBtn").hide();
						$("#createPlanBtn").prop('disabled', true);
					} else {
						$("#planMsg").html("");
						$("#createPlanBtn").prop('disabled', false);
					}
				}
			});
		});

		$("#planStartDTPicker").datepicker(
				{
					dateFormat : 'dd/mm/yy',
					changeMonth : true,
					changeYear : true,
					minDate : new Date(),
					onSelect : function(date) {

						//Set Minimum Date of EndDatePicker After Selected Date of StartDatePicker
						$('#planEndDTPicker').datepicker('option', "minDate",
								$("#planStartDTPicker").datepicker("getDate"));

					}
				});

		$("#planEndDTPicker").datepicker({
			dateFormat : 'dd/mm/yy',
			changeMonth : true,
			changeYear : true

		});

	});
</script>
</head>
<%@ include file="header-inner.jsp"%><br />
<body>
	<font color="green">${success}</font>
	<font color="red">${failure}</font>

	<h2>Case Worker Registration</h2>
	<form:form action="planReg" method="POST" modelAttribute="planModel"
		id="planRegForm">
		<table>
			<tr>
				<td>Plan Name</td>
				<td><form:input path="planName" id="planName" /></td>
				<td><span id="planMsg" style="color: red"></span></td>
			</tr>
			<tr>
				<td>Plan Description</td>
				<td><form:textarea path="planDescription" /></td>
			</tr>
			<tr>
				<td>Plan Start Date</td>
				<td><form:input path="planStartDate" id="planStartDTPicker" /></td>
			</tr>
			<tr>
				<td>Plan End Date</td>
				<td><form:input path="planEndDate" id="planEndDTPicker" /></td>
			</tr>

			<tr>
				<td><input type="reset" value="Reset" /></td>
				<td><input type="Submit" value="Create Plan" id="createPlanBtn" /></td>
			</tr>


		</table>
	</form:form>



</body>
</html>