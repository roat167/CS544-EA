<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Car Reservation List</title>
</head>
<body>
<br>
<span class="PageTitle">&nbsp; Car Reservations List</span>
<br>
	<div class="col-lg-12" style="text-align:right">
	<button class ="linkToUrl btn btn-default" data-url="${pageContext.request.contextPath}/reservations/add" 
		id="btnAddBooking" type="button" style="background-color: orange; color:#ffffff"> Reserve Another Car</button> 
	</div>
<div class="listing Box">
	<table class="sortable" id='tblList'>
	<tr>		
		<th>Customer</th>
		<th>Car Description</th>
		<th>Reservation <br/> Start Date</th>
		<th>Reservation <br/> End Date</th>				
		<th>Status</th>
	</tr>	
	<c:forEach var="reservation" items="${reservations}">
		<tr class="linkToUrl" data-url="${pageContext.request.contextPath}/reservations/u/${reservation.id}">
			<td>${reservation.user.fullName}</td>
			<td>${reservation.car.shortDescription}</td>
			<td><fmt:formatDate pattern="MM/dd/yyyy HH:mm:ss" value="${reservation.startDate}" /></td>
			<td><fmt:formatDate pattern="MM/dd/yyyy HH:mm:ss" value="${reservation.endDate}" /></td>				
			<td>${reservation.status}</td>
		</tr>
	</c:forEach>	
	</table>	
				<div id="Pagination" class="pagination"></div>
			<input value="Prev" name="prev_text" id="prev_text" type="hidden"><input value="Next" name="next_text" id="next_text" type="hidden"><input value="10" name="items_per_page" id="items_per_page" class="numeric" type="hidden"><input value="10" name="num_display_entries" id="num_display_entries" class="numeric" type="hidden"><input value="2" name="num_edge_entries" id="num_edge_entries" class="numeric" type="hidden">
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dataTable/filterTable.js"></script>

<script type="text/javascript">
//this script is important So, don't remove it
         var table=document.getElementById('tblList');
         if(table!=null)
         {/*
	        var pager = new Pager('tblList', 10); 
	        pager.init(); 
	        pager.showPageNav('pager', 'pageNavPosition'); 
	        pager.showPage(1);
 	        */
         }
    </script>
    </div>
	<div class="col-lg-12" style="text-align:right">
	&nbsp;
	</div>	
</body>
</html>