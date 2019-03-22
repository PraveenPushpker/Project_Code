<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Show Plans Page</title>


<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">

<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script
	src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js">
	
</script>

<script>
	$(document).ready(function() {
		$('#planTable').DataTable({
			//"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
			"sPaginationType" : "full_numbers"
		});
	});
	function confirmDelete() {
		return confirm("Are you sure you want to Delete ?");
	}
	function confirmActivate() {
		return confirm("Are you sure you want to Activate ?");
	}
</script>

</head>
<%@ include file="header-inner.jsp"%><br/>
<body>
	<h2>All Plans Details</h2>
	<font color="green">${successMsg }</font>
	
	<font color="red">${failureMsg }</font>
	
	<table border="1" id="planTable">
		<thead>
			<tr>
				<td>SNo</td>
				<td>Plan Name</td>
				<td>Plan Description</td>
				<td>Plan Start Date</td>
				<td>Plan End Date</td>
				<td>Action</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${plans}" var="plan" varStatus="index">
				<tr>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${plan.planName}" /></td>
					<td><c:out value="${plan.planDescription}" /></td>
					<td><c:out value="${plan.planStartDate}" /></td>
					<td><c:out value="${plan.planEndDate}"/> </td>
					<td><a href="editPlan?planId=${plan.planId}">Edit</a> 
						<c:if
							test="${plan.activeSw =='Y'}">
							<a href="deletePlan?planId=${plan.planId}"
								onclick="return confirmDelete()">Delete</a>
						</c:if>
						<c:if test="${plan.activeSw =='N'}">
							<a href="activatePlan?planId=${plan.planId}"
								onclick="return confirmActivate()">Activate</a>
						</c:if></td>
						
				</tr>
			</c:forEach>
		</tbody>
	</table>

</body>
</html>