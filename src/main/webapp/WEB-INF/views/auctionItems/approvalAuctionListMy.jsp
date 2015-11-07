<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>拍卖物品列表</title>

<link href="${ctx}/static/styles/dkp.css" type="text/css" rel="stylesheet" />

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
 
	<br>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>拍卖物品</th>
				<th style="width: 35%;">描述</th>
				<th>库存数量</th> 
				<th>起拍价</th> 
				<th>限购数量</th>  
				<th>创建时间</th>
				<th>创建人</th>
				<th>状态</th> 
				<th>审批人</th>
				<th>审批时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${auctions.content}" var="task">
				<tr>
					<td>${task.goodsName}</td>
					<td>${task.description}</td>
					<td>${task.number}</td> 
					<td><fmt:formatNumber value="${task.integral}" pattern="##.##"/></td>
					<td>${task.limitedNumber}</td> 
					<td><fmt:formatDate value="${task.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				    <td>${task.createUser.name}</td>
					<td><c:if test="${task.status eq 'Y'}">审批中</c:if><c:if test="${task.status eq 'pass'}">竞拍成功</c:if><c:if test="${task.status eq 'reject'}">流拍</c:if></td>
					<td>${task.approvalUser.name}</td>
					<td><fmt:formatDate value="${task.approvalDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>

					<td>
					
					
						<c:if test="${task.status eq 'Y'}">
							<a href="${ctx}/auction/user/approvalConfirm/${task.id}" >审核竞拍</a> 
						</c:if>
						 <c:if test="${task.status eq 'pass'}">
						<a href="${ctx}/auction/user/viewAuctionUser/${task.id}" >查看竞拍结果</a> 
						</c:if>
					</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
 



 
	<tags:pagination page="${auctions}" paginationSize="10" />


</body>
</html>
