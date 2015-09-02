<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>我的兑换积分申请列表</title>
	<script>
		$(document).ready(function() {
			 if('${message}'!=null && '${message}'!='')
			 {
				 alert('${message}');
			 }
		});
	</script>
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
				<th>申请物品</th>
				<th>单价</th>
				<th>数量</th> 
				<th>申请描述</th>
				<th>总积分</th>
				<th>申请人</th>
				<th>申请时间</th> 
				<th>状态</th> 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${exchangeIntegralApplys.content}" var="task">
				<tr>
					<td>${task.exchangeIntegral.goodsName}</td>
					<td><fmt:formatNumber value="${task.exchangeIntegral.integral}" pattern="##.#"/></td>
					<td>${task.number}</td>
					<td>${task.description}</td>
					<td><fmt:formatNumber value="${task.integral}" pattern="##.#"/></td>
					<td>${task.cteateUser.name}</td>
					<td><fmt:formatDate value="${task.cteateDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td> 
					<td> 
					<c:choose>
							<c:when test="${task.status eq 'Approval'}">审批中</c:when>
							<c:when test="${task.status eq 'pass'}">审批通过</c:when>
							<c:when test="${task.status eq 'reject'}">审批拒绝</c:when> 
						</c:choose>
					</td>
					 

				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${exchangeIntegralApplys}" paginationSize="10" />


</body>
</html>
