<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>用户管理</title>
<script type="text/javascript">
 
</script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>

	<div class="row">
		<div class="span4 offset7">
			<form class="form-search" action="#">
			  <label>姓名</label> <input type="text" name="search_LIKE_userName" class="input-medium" value="${param.search_LIKE_userName}"> 
			  <button type="submit" class="btn" id="search_btn">查询</button>
			</form>
		</div>
			
	</div>
	<div>
	 <font color="red"><b>公会总积分:<fmt:formatNumber value="${IntegralSum}" pattern="##.##"/></b></font>
	</div>
	<br>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>登录名</th>
				<th>昵称</th>
				<th>游戏角色名</th>
				<th>注册时间</th>
				<th>权限</th>
				<th>邮箱</th>
				<th>电话</th>
				<th>积分</th>
				<th>状态</th> 
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${usersx}" var="user">
				<tr>
					<td>${user.loginName}</td>
					<td>${user.name}</td>
					<td>${user.gameName}</td>
					<td><fmt:formatDate value="${user.registerDate}"
							pattern="yyyy-MM-dd  HH:mm:ss" /></td>
					<td><c:choose>
							<c:when test="${user.roles eq 'admin'}">管理员</c:when>
							<c:when test="${user.roles eq 'Head'}">团长</c:when>
							<c:when test="${user.roles eq 'OneLevel'}">一级</c:when>
							<c:when test="${user.roles eq 'TwoLevel'}">二级</c:when>
							<c:when test="${user.roles eq 'ThreeLevel'}">三级</c:when>
							<c:otherwise>会员</c:otherwise>
						</c:choose></td>
					<td>${user.email}</td>
					<td>${user.phone}</td>
					<td><a href="${ctx}/integralHistory/myIntegeral/${user.id}"><fmt:formatNumber value="${user.integral}" pattern="##.##"/></a></td>
					<td>${allStatus[user.status]}&nbsp;</td>
					<td>
					<shiro:hasAnyRoles name="admin,Head">
							<a href="${ctx}/admin/user/update/${user.id}">修改</a>&nbsp; 
							<a href="#" onclick="confirmDelete('${ctx}/admin/user/delete/${user.id}')">删除</a>&nbsp; 
							
							<c:if test="${user.status eq 'enabled'}">
							<a href="#"	onclick="confirmable('${ctx}/admin/user/disabled/${user.id}')">失效</a>&nbsp;
							</c:if>
					 		 <c:if test="${user.status eq 'disabled'}">
								<a href="#"	onclick="confirmDisabled('${ctx}/admin/user/able/${user.id}')">生效</a>&nbsp;
							</c:if>
					 		
					 		
					</shiro:hasAnyRoles>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:paginationMybatis />

</body>
</html>
