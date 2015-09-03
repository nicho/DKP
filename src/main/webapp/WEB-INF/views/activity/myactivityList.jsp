<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>我发起的活动列表</title>
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
	<div></div>
	<br>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th>活动级别</th>
				<th>活动类型</th>
				<th>人数规模</th>
				<th>活动积分</th>
				<th>活动说明</th>
				<th>响应时间</th>
				<th>状态</th>
				<th>发起人</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${activitys.content}" var="task">
				<tr>
					<td>${task.title}</td>
					<td><c:if test="${task.fType eq 'PersonalActivities'}">个人活动</c:if> <c:if test="${task.fType eq 'AssociationActivity'}">公会活动</c:if></td>
					<td><c:forEach var="list" items="${ActivityTypeList}">
							<c:if test="${list.id eq task.activityType}">${list.typeName}</c:if>
						</c:forEach></td>
					<td>${task.personCount}</td>
					<td><fmt:formatNumber value="${task.integral}" pattern="##.##"/></td>
					<td>${task.activityExplain}</td>
					<td><fmt:formatDate value="${task.startDate}" pattern="yyyy-MM-dd HH:mm:ss" /> 至 <fmt:formatDate value="${task.endDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>
					
					<c:if test="${task.status eq 'pass'}">活动进行中</c:if>
					<c:if test="${task.status eq 'close'}">活动停止登记</c:if>
					<c:if test="${task.status eq 'process'}">活动发起审批中</c:if>
					<c:if test="${task.status eq 'reject'}">活动发起审批拒绝</c:if>
					<c:if test="${task.status eq 'ConfirmProcess'}">积分发放审批中</c:if>
					<c:if test="${task.status eq 'ConfirmReject'}">积分发放审批拒绝</c:if>
					<c:if test="${task.status eq 'ConfirmPass'}">活动结束</c:if>
					<c:if test="${task.status eq 'disabled'}">失效</c:if>
					
					</td>
					<td>${task.createUser.gameName}</td>

					<td><a href="${ctx}/activity/view/${task.id}">查看</a> &nbsp; 
						<a href="${ctx}/activityUser/list/${task.id}">参与的会员</a>  &nbsp; 
						<c:if test="${task.status eq 'pass'}">
							<a href="${ctx}/activity/closeActivity/${task.id}">活动停止</a>  &nbsp; 
						</c:if>
						<c:if test="${task.status eq 'pass' || task.status eq 'close'}">
							<a href="${ctx}/activity/confirmActivity/${task.id}">积分发放</a>  &nbsp; 
							<shiro:hasAnyRoles name="admin,Head"> 
							<a href="#" onclick="confirmDisabled('${ctx}/activity/disabled/${task.id}')">失效</a> 
							</shiro:hasAnyRoles>
						</c:if>
						
					  
						
					</td>
						
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${activitys}" paginationSize="10" />


</body>
</html>
