<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>积分历史列表</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="row">
		<div class="span4 offset7">
			<form class="form-search" action="#"></form>
		</div>

	</div>
	<div></div> <br>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主题</th>
				<th>描述</th>
				<th>积分</th> 
				<th>时间</th>
			 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${integralHistorys.content}" var="task">
				<tr>
					<td>${task.title}</td>
					<td>${task.description}</td>
					<td>${task.mark}<fmt:formatNumber value="${task.integral}" pattern="##.##"/></td> 
					<td><fmt:formatDate value="${task.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td> 

				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${integralHistorys}" paginationSize="10" />


</body>
</html>
