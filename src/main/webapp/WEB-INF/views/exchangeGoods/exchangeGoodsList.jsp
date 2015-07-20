<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>拍卖列表</title>
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
	<div><a class="btn" href="${ctx}/exchangeGoods/create">拍卖登记</a></div> <br>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>拍卖物品</th>
				<th>描述</th>
				<th>积分</th>
				<th>买受人</th>
				<th>时间</th>
				<th>登记人</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${exchangeGoodss.content}" var="task">
				<tr>
					<td>${task.goodsName}</td>
					<td>${task.description}</td>
					<td>${task.integral}</td>
					<td>${task.user.name}</td>
					<td><fmt:formatDate value="${task.cteateDate}" pattern="yyyy年MM月dd日 HH时mm分ss秒" /></td>
					<td>${task.cteateUser.name}</td>

					<td><a href="#" onclick="confirmDelete('${ctx}/exchangeGoods/delete/${task.id}')">删除</a>&nbsp;</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${exchangeGoodss}" paginationSize="10" />


</body>
</html>
