<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>活动参与排行</title> 
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>

	<div class="row">
		<div class="span4 offset7">
			 <form class="form-search" action="#">
			<div>姓名&nbsp;<input type="text" name="search_LIKE_userName" value="${param.search_LIKE_userName}"> &nbsp; &nbsp;<input type="submit" value="查询" class="btn"></div>	
			</form>
		</div>

	</div>
	<div>
	 
	</div>
	<br>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr> 
				<th>昵称</th>
				<th>游戏角色名</th> 
				<th>级别</th> 
				<th>参与活动次数</th>  
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${usersx}" var="user">
				<tr> 
					<td>${user.name}</td>
					<td>${user.gameName}</td> 
					<td><c:choose>
							<c:when test="${user.roles eq 'admin'}">管理员</c:when>
							<c:when test="${user.roles eq 'Head'}">团长</c:when>
							<c:when test="${user.roles eq 'OneLevel'}">一级</c:when>
							<c:when test="${user.roles eq 'TwoLevel'}">二级</c:when>
							<c:when test="${user.roles eq 'ThreeLevel'}">三级</c:when>
							<c:otherwise>会员</c:otherwise>
						</c:choose>
					</td> 
					<td>${user.cn}</td>  
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<tags:paginationMybatis />

</body>
</html>
