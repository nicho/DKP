<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>活动类型列表</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="row">
		<div class="span4 offset7">
			<form class="form-search" action="#">
				 
				 
		    </form>
	    </div>
	   
	</div>
	 <shiro:hasAnyRoles name="admin,Head">
		<div><a class="btn" href="${ctx}/valueSet/create">创建活动类型</a></div> <br>
	</shiro:hasAnyRoles>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>  <th>活动类型</th><th>活动类型描述</th>   <th>状态</th>  <th>管理</th></tr></thead>
		<tbody>
		<c:forEach items="${valueSets.content}" var="task">
			<tr> 
				<td>${task.typeName}</td>
				<td>${task.typeExplain}</td>  
				<td> 
				<c:if test="${task.status eq 'Y'}">有效</c:if>
				<c:if test="${task.status eq 'disabled'}">失效</c:if>
				</td> 
				<td> 
				 <c:if test="${task.status eq 'Y'}"><a href="#" onclick ="confirmDisabled('${ctx}/valueSet/disabled/${task.id}')">失效</a>  &nbsp;&nbsp;
				 
				</c:if>  
				  <a href="#" onclick ="confirmDelete('${ctx}/valueSet/delete/${task.id}')">删除</a> 
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${valueSets}" paginationSize="10" />

	
</body>
</html>
