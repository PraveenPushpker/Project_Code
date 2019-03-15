<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>View Accounts</title>
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
	<h2>View Accounts</h2>

	<font color='red'>${failure}</font>
	<font color='green'>${success}</font>

	<table border="1" id="accTbl">
		<thead>
			<tr>
				<td>S.No</td>
				<td>First Name</td>
				<td>Last Name</td>
				<td>Email</td>
				<td>Role</td>
				<td>Action</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${accounts}" var="acc" varStatus="index">
				<tr>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${acc.firstName}" /></td>
					<td><c:out value="${acc.lastName}" /></td>
					<td><c:out value="${acc.email}" /></td>
					<td><c:out value="${acc.role}" /></td>

					<td><a href="#">Edit</a> <c:if test="${acc.activeSw =='Y' }">
							<a href="delete?accId=${acc.accId}"
								onclick="return confirmDelete()">Delete</a>
						</c:if> <c:if test="${acc.activeSw =='N' }">
							<a href="activate?accId=${acc.accId}"
								onclick="return confirmActivate()">Activate</a>
						</c:if></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>