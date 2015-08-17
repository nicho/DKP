<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>我参与的活动</title>
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
				<th>活动级别</th>
				<th>活动类型</th>
				<th>人数规模</th>
				<th>活动积分</th>
				<th>活动说明</th>
				<th>响应时间</th>
				<th>状态</th>
				<th>发起人</th>
				<th>参与时间</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${activityUsers.content}" var="task">
				<tr>
					<td>${task.activity.title}</td>  
					<td><c:if test="${task.activity.fType eq 'PersonalActivities'}">个人活动</c:if> <c:if test="${task.activity.fType eq 'AssociationActivity'}">公会活动</c:if></td>  
					<td>
					<c:forEach var="list" items="${ActivityTypeList}">
							<c:if test="${list.id eq task.activity.activityType}">${list.typeName}</c:if>
					</c:forEach>
					</td>  
					<td>${task.activity.personCount}</td>  
					<td><fmt:formatNumber value="${task.activity.integral}" pattern="##.##" /></td>  
					<td>${task.activity.activityExplain}</td>  
					<td><fmt:formatDate value="${task.activity.startDate}" pattern="yyyy-MM-dd HH:mm:ss" /> 至 <fmt:formatDate value="${task.activity.endDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>  
				 
								 	<td>
					<c:if test="${task.activity.status eq 'pass'}">活动进行中</c:if>
					<c:if test="${task.activity.status eq 'process'}">活动发起审批中</c:if>
					<c:if test="${task.activity.status eq 'reject'}">活动发起审批拒绝</c:if>
					<c:if test="${task.activity.status eq 'ConfirmProcess'}">积分发放审批中</c:if>
					<c:if test="${task.activity.status eq 'ConfirmReject'}">积分发放审批拒绝</c:if>
					<c:if test="${task.activity.status eq 'ConfirmPass'}">活动结束</c:if>
					<c:if test="${task.activity.status eq 'N'}">失效</c:if> 
					</td>
							
					<td>${task.activity.createUser.gameName}</td>    
					<td><fmt:formatDate value="${task.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>  
					<td><a href="${ctx}/activity/view/${task.activity.id}">查看</a> &nbsp;</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${activityUsers}" paginationSize="10" />


</body>
</html>
