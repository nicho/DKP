<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>惩罚项目列表</title>
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
		<div><a class="btn" href="${ctx}/penaltyValueSet/create">创建惩罚项目</a></div> <br>
	</shiro:hasAnyRoles>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>  <th>惩罚项目</th><th>惩罚项目描述</th> <th>扣除积分</th>   <th>状态</th>  <th>管理</th></tr></thead>
		<tbody>
		<c:forEach items="${valueSets.content}" var="task">
			<tr> 
				<td>${task.typeName}</td>
				<td>${task.typeExplain}</td>  
				<td>${task.integral}</td>  
				<td> 
				<c:if test="${task.status eq 'Y'}">有效</c:if>
				<c:if test="${task.status eq 'disabled'}">失效</c:if>
				</td> 
				<td> 
				 <c:if test="${task.status eq 'Y'}"><a href="#" onclick ="confirmDisabled('${ctx}/valueSet/disabled/${task.id}')">失效</a>  &nbsp;&nbsp;
				 
				</c:if>  
				  <a href="${ctx}/penaltyValueSet/update/${task.id}" >修改</a> &nbsp;&nbsp;
				  <a href="#" onclick ="confirmDelete('${ctx}/penaltyValueSet/delete/${task.id}')">删除</a> 
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${valueSets}" paginationSize="10" />

	
</body>
</html>
