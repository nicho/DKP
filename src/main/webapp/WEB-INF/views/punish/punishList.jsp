<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>惩罚列表</title>
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
	<div><a class="btn" href="${ctx}/punish/create">惩罚登记</a></div> <br>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>惩罚名称</th>
				<th>惩罚类型</th>
				<th>扣除积分</th>
				<th>被处罚人</th>
				<th>处罚时间</th>
				<th>处罚人</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${punishs.content}" var="task">
				<tr>
					<td>${task.punishName}</td>
					<td>${task.description}</td>
					<td><fmt:formatNumber value="${task.integral}" pattern="##.##"/></td>
					<td>${task.user.name}</td>
					<td><fmt:formatDate value="${task.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${task.createUser.name}</td>

					<td><a href="#" onclick="confirmDelete('${ctx}/punish/delete/${task.id}')">删除</a>&nbsp;</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${punishs}" paginationSize="10" />


</body>
</html>
