<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %> 
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>活动列表</title>
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
	<div> </div> <br>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>标题</th><th>类型</th><th>活动类型</th><th>人数规模</th><th>活动说明</th> <th>响应时间</th>  <th>状态</th>   <th>发起人</th> <th>管理</th></tr></thead>
		<tbody>
		<c:forEach items="${activitys.content}" var="task">
			<tr> 
				<td>${task.title}</td>
				<td>${task.fType}</td>
				<td>${task.activityType}</td>
				<td>${task.personCount}</td>
				<td>${task.activityExplain}</td>
				<td><fmt:formatDate value="${task.startDate}"
							pattern="yyyy-MM-dd  HH:mm:ss" /> - <fmt:formatDate value="${task.endDate}"
							pattern="yyyy-MM-dd  HH:mm:ss" /></td> 
				<td> 
				<c:if test="${task.status eq 'Y'}">有效</c:if>
				<c:if test="${task.status eq 'N'}">失效</c:if>
				</td>
				<td>${task.createUser.gameName}</td> 
			 
				<td> 
				 <c:if test="${task.status eq 'Y'}"><a href="#" onclick ="confirmDisabled('${ctx}/gameCode/disabled/${task.id}')">失效</a> 
				 </c:if>  
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${activitys}" paginationSize="10" />

	
</body>
</html>
