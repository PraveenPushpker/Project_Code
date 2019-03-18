<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>View IES Plans</title>
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script
	src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#accTbl').DataTable({
			"pagingType" : "full_numbers"
		});
	});

	function confirmDelete() {
		return confirm("Are you sure, you want to delete ?");
	}

	function confirmActivate() {
		return confirm("Are you sure, you want to Activate ?");
	}
</script>

</head>

<%@ include file="header-inner.jsp" %>
<body>
	<h2>View IES Plans</h2>

	<font color='red'>${failure}</font>
	<font color='green'>${success}</font>

	<table border="1" id="accTbl">
		<thead>
			<tr>
				<td>Sl.No</td>
				<td>Plan Name</td>
				<td>Plan Desc</td>
				<td>Start Date</td>
				<td>End Date</td>
				<td>Action</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${accounts}" var="pln" varStatus="index">
				<tr>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${pln.planName}" /></td>
					<td><c:out value="${pln.planDesc}" /></td>
					<td><c:out value="${pln.startDate}" /></td>
					<td><c:out value="${pln.endDate}" /></td>

					<td><a href="#">Edit</a> <c:if test="${pln.activeSw =='Y' }">
							<a href="iesPlndelete?planId=${pln.planId}"
								onclick="return confirmDelete()">Delete</a>
						</c:if> <c:if test="${pln.activeSw =='N' }">
							<a href="iesPlnActivate?planId=${pln.planId}"
								onclick="return confirmActivate()">Activate</a>
						</c:if></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>