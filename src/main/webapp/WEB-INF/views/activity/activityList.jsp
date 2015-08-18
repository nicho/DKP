<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>最近活动列表</title>

<link href="${ctx}/static/styles/dkp.css" type="text/css" rel="stylesheet" />

	<script lang="javascript" type="text/javascript">
	function deleteTask(id)
	{
		if(confirm("确认删除?"))
		{
			location.href="${ctx}/activity/deleteindex/"+id;
		}
	}
	</script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="visible-desktop"> 
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
				<!-- <th>活动级别</th>
				<th>活动类型</th>
				<th>人数规模</th> -->
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
					<!--<td><c:if test="${task.fType eq 'PersonalActivities'}">个人活动</c:if> <c:if test="${task.fType eq 'AssociationActivity'}">公会活动</c:if></td>
					<td><c:forEach var="list" items="${ActivityTypeList}">
							<c:if test="${list.id eq task.activityType}">${list.typeName}</c:if>
						</c:forEach></td>
					<td>${task.personCount}</td> -->
					<td><fmt:formatNumber value="${task.integral}" pattern="##.##"/></td>
					<td>${task.activityExplain}</td>
					<td><fmt:formatDate value="${task.startDate}" pattern="yyyy-MM-dd HH:mm:ss" /> 至  <fmt:formatDate value="${task.endDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
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
						<a href="${ctx}/activityUser/list/${task.id}">已登记的会员</a>  &nbsp;  
						<c:if test="${task.status eq 'pass'}">
						<a href="${ctx}/activity/registerActivity/${task.id}">参与活动</a>  &nbsp;  </c:if>
						
					    <shiro:hasAnyRoles name="admin,Head"> 
								<a href="#" onclick="deleteTask('${task.id}')">删除</a>&nbsp;  
						</shiro:hasAnyRoles>
					</td>
						
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
<div class="hidden-desktop">
  <div class="listleave_box">
    <div class="listleave_bar">活动列表</div>
    <ul>
 	<c:forEach items="${activitys.content}" var="task">
      <li>
        <div class="listleave_text">
          <p><span>标题 </span>${task.title}</p>
       <!--   <p><span>活动级别 </span><c:if test="${task.fType eq 'PersonalActivities'}">个人活动</c:if> <c:if test="${task.fType eq 'AssociationActivity'}">公会活动</c:if></p>
          <p><span>活动类型 </span><c:forEach var="list" items="${ActivityTypeList}">
							<c:if test="${list.id eq task.activityType}">${list.typeName}</c:if>
						</c:forEach></p>
          <p><span>人数规模 </span>${task.personCount}</p> -->
          <p><span>活动积分 </span><fmt:formatNumber value="${task.integral}" pattern="##.##"/></p>
		  <p><span>活动说明 </span>${task.activityExplain}</p>
		  <p><span>响应时间 </span><fmt:formatDate value="${task.startDate}" pattern="yyyy-MM-dd HH:mm:ss" /> </p>
		  <p><span> 至 </span>  <fmt:formatDate value="${task.endDate}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
		  <p><span>状态 </span><c:if test="${task.status eq 'pass'}">活动进行中</c:if>
					<c:if test="${task.status eq 'process'}">活动发起审批中</c:if>
					<c:if test="${task.status eq 'reject'}">活动发起审批拒绝</c:if>
					<c:if test="${task.status eq 'ConfirmProcess'}">积分发放审批中</c:if>
					<c:if test="${task.status eq 'ConfirmReject'}">积分发放审批拒绝</c:if>
					<c:if test="${task.status eq 'ConfirmPass'}">活动结束</c:if>
					<c:if test="${task.status eq 'disabled'}">失效</c:if></p>
		  <p><span>发起人 </span>${task.createUser.gameName}</p> 
		  <div class="cl"></div>
        </div>
		<div class="morebtn">
		<input name="" type="button" class="orangebtn2" value="查看" onClick="location.href='${ctx}/activity/view/${task.id}'">
		<input name="" type="button" class="orangebtn2" value="已登记的会员" onClick="location.href='${ctx}/activityUser/list/${task.id}'">
		<c:if test="${task.status eq 'pass'}">
	    	<input name="" type="button" class="orangebtn2" value="参与活动" onClick="location.href='${ctx}/activity/registerActivity/${task.id}'">
		</c:if>
		 <shiro:hasAnyRoles name="admin,Head"> 
			<input name="" type="button" class="orangebtn2" value="删除" onClick="deleteTask('${task.id}')"> 
	     </shiro:hasAnyRoles>
		 
		</div>
   		
      </li>
     </c:forEach> 
    </ul>
    <div class="cl"></div>
  </div>
 </div>

	<tags:pagination page="${activitys}" paginationSize="10" />


</body>
</html>
