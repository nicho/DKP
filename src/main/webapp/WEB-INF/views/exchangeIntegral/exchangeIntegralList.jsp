<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>贡献物品列表</title>
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
	<shiro:hasAnyRoles name="admin,Head">
	<div><a class="btn" href="${ctx}/exchangeIntegral/create">添加物品</a></div> <br>
	</shiro:hasAnyRoles>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>物品名称</th> 
				<th>可兑换积分</th>
				<th style="width: 47%;">描述</th> 
				<th>每日限制数量</th> 
				<th>时间</th>
				<th>创建人</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${exchangeIntegrals.content}" var="task">
				<tr>
					<td>${task.goodsName}</td>
					<td><fmt:formatNumber value="${task.integral}" pattern="##.##"/></td>
					<td>${task.description}</td>
					<td>${task.limitedNumber}</td>
					<td><fmt:formatDate value="${task.cteateDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td> 
					<td>${task.cteateUser.name}</td>

					<td>
					<shiro:hasAnyRoles name="admin,Head">
					<a href="#" onclick="confirmDelete('${ctx}/exchangeIntegral/delete/${task.id}')">删除</a>&nbsp;
					<a href="${ctx}/exchangeIntegral/update/${task.id}" >修改</a>&nbsp;
					</shiro:hasAnyRoles>
					<a href="${ctx}/exchangeIntegralApply/create/${task.id}"  >申请贡献</a>&nbsp;
					</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${exchangeIntegrals}" paginationSize="10" />


</body>
</html>
