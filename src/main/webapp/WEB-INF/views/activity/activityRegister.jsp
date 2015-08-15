
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>

<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/demo.css">
<script type="text/javascript" src="${ctx}/static/easyui/jquery.easyui.min.js"></script>

<title>活动登记</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/activity/registerActivity" method="post" class="form-horizontal">
		<input type="hidden" name="activityId" value="${activity.id}">
		<fieldset>
			<legend>
				<small>活动登记</small>
			</legend>
<div class="visible-desktop">
	<div class="control-group">
				<label class="control-label">活动级别:<c:if test="${activity.fType eq 'PersonalActivities'}">个人活动</c:if> <c:if test="${activity.fType eq 'AssociationActivity'}">公会活动</c:if></label>

			</div>
			<div class="control-group">
				<label class="control-label">标题:${activity.title}</label>

			</div>
			<div class="control-group">
				<label class="control-label">活动类型:<c:forEach var="list" items="${ActivityTypeList}">
						<c:if test="${activity.activityType eq list.id}">  ${list.typeName} </c:if>
					</c:forEach>
				</label>

			</div>
			<div class="control-group">
				<label class="control-label">人数规模:${activity.personCount}</label>

			</div>
			<div class="control-group">
				<label for="task_title" class="control-label">开始时间:<fmt:formatDate value="${activity.startDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></label>

			</div>
			<div class="control-group">
				<label for="task_title" class="control-label">结束时间:<fmt:formatDate value="${activity.endDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></label>

			</div>
			<div class="control-group">
				<label class="control-label">活动积分: ${activity.integral}</label>

			</div>
			<div class="control-group">
				<label for="description" class="control-label">活动说明:${activity.activityExplain}</label>

			</div>
</div>

 <div class="hidden-desktop">
			 <div class="control-group">
				<label class="control-label">活动级别:</label>
				<div class="controls">
					<c:if test="${activity.fType eq 'PersonalActivities'}">个人活动</c:if> <c:if test="${activity.fType eq 'AssociationActivity'}">公会活动</c:if>
					
				</div>
			</div> 
		    <div class="control-group">
				<label class="control-label">标题:</label>
				<div class="controls">
					${activity.title}
				</div>
			</div>
		 <div class="control-group">
				<label class="control-label">活动类型:</label>
				<div class="controls"> 
						<c:forEach var="list" items="${ActivityTypeList}"> 
							<c:if test="${activity.activityType eq list.id}">  ${list.typeName} </c:if>
						</c:forEach> 
				</div>
			</div>
		    <div class="control-group">
				<label class="control-label">人数规模:</label>
				<div class="controls"> 
					${activity.personCount}
				</div>
			</div>
			 <div class="control-group">
				<label for="task_title" class="control-label">开始时间:</label>
				<div class="controls">
					${activity.startDate}
				</div>
			</div>	
			 <div class="control-group">
				<label for="task_title" class="control-label">结束时间:</label>
				<div class="controls">
				<fmt:formatDate  value="${activity.endDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />
				</div>
			</div>	
									 <div class="control-group">
				<label class="control-label">活动积分:</label>
				<div class="controls">
					${activity.integral}
				</div>
			</div>
			<div class="control-group">
				<label for="description" class="control-label">活动说明:</label>
				<div class="controls">
					 ${activity.activityExplain} 
				</div>
			</div>	

		</div>		
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="活动登记" />&nbsp;
			</div>
		</fieldset>
	</form>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#task_gameName").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate({
				submitHandler : function(form) { //通过之后回调 
					$("input[type='submit']").attr("disabled", "disabled");
					form.submit();
				}
			});
		});
	</script>
</body>
</html>
