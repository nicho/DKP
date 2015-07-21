<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>登记的用户列表</title>
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
	 
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>活动名称</th> 
				<th>登记会员</th> 
				<th>会员游戏名</th> 
				<th>职责</th> 
				<th>登记时间</th> 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${activityUsers.content}" var="task">
				<tr>
					<td>${task.activity.title}</td>
					<td>${task.user.name}</td>
					<td>${task.user.gameName}</td>
					<td>
					<c:choose>
							<c:when test="${user.user.roles eq 'admin'}">管理员</c:when>
							<c:when test="${user.user.roles eq 'Head'}">团长</c:when>
							<c:when test="${user.user.roles eq 'OneLevel'}">一级</c:when>
							<c:when test="${user.user.roles eq 'TwoLevel'}">二级</c:when>
							<c:when test="${user.user.roles eq 'ThreeLevel'}">三级</c:when>
							<c:otherwise>会员</c:otherwise>
						</c:choose>
					</td> 
					<td><fmt:formatDate value="${task.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>   
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${activityUsers}" paginationSize="10" />


</body>
</html>
