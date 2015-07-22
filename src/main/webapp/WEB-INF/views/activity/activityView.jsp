 <%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>  
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

	<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/demo.css">
	<script type="text/javascript" src="${ctx}/static/easyui/jquery.easyui.min.js"></script>

	<title>查看活动</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" action="${ctx}/activity/${action}" method="post" class="form-horizontal">  
		<fieldset>
			<legend><small>查看活动</small></legend>
			 <div class="control-group">
				<label class="control-label">活动级别:</label>
				<div class="controls">
					<input type="text" id="title" name="title" value="<c:if test="${activity.fType eq 'PersonalActivities'}">个人活动</c:if> <c:if test="${activity.fType eq 'AssociationActivity'}">公会活动</c:if>" class="input-large " disabled="disabled"/>
					
				</div>
			</div> 
		    <div class="control-group">
				<label class="control-label">标题:</label>
				<div class="controls">
					<input type="text" id="title" name="title" value="${activity.title}" class="input-large" disabled="disabled"/>
				</div>
			</div>
		 <div class="control-group">
				<label class="control-label">活动类型:</label>
				<div class="controls"> 
						<c:forEach var="list" items="${ActivityTypeList}"> 
							<c:if test="${activity.activityType eq list.id}"> <input type="text" id="title" name="title" value="${list.typeName}" class="input-large" disabled="disabled"/></c:if>
						</c:forEach> 
				</div>
			</div>
		    <div class="control-group">
				<label class="control-label">人数规模:</label>
				<div class="controls"> 
					<input type="text" id="title" name="title" value="${activity.personCount}" class="input-large" disabled="disabled"/>
				</div>
			</div>
			 <div class="control-group">
				<label for="task_title" class="control-label">开始时间:</label>
				<div class="controls">
					<input type="text" id="startDateStr" name="startDateStr"  value="<fmt:formatDate  value="${activity.startDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />" class="input-large required"  disabled="disabled"  />
				</div>
			</div>	
			 <div class="control-group">
				<label for="task_title" class="control-label">结束时间:</label>
				<div class="controls">
					<input type="text" id="endDateStr" name="endDateStr"  value="<fmt:formatDate  value="${activity.endDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />" class="input-large required"   disabled="disabled" />
				</div>
			</div>	
									 <div class="control-group">
				<label class="control-label">活动积分:</label>
				<div class="controls">
					<input type="text" id="integral" name="integral" value="${activity.integral}" class="input-large required digits" disabled="disabled"/>
				</div>
			</div>
			<div class="control-group">
				<label for="description" class="control-label">活动说明:</label>
				<div class="controls">
					<textarea id="codes" rows="5" name="activityExplain" style="  width: 500px;"  class="input-large" disabled="disabled">${activity.activityExplain}</textarea>
				</div>
			</div>	
			 <c:if test="${activity.status eq 'pass'}">
		  <div class="control-group">
				<label class="control-label">网站链接:</label>
				<div class="controls">
					<a href="#" onclick="window.open('${activity.webUrl}')">${activity.webUrl}</a>
				</div>
			</div>
		 <div class="control-group">
				<label class="control-label">二维码:</label>
				<div class="controls">
					 <img src="${HttpImageUrl}/image/${activity.qrCodeUrl}" />
				</div>
			</div>
	 </c:if>
		</fieldset>
	</form>
	<script>
	 
		$(document).ready(function() {  
			//聚焦第一个输入框
			$("#task_gameName").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate({
				  submitHandler: function(form) {  //通过之后回调 
					 $("input[type='submit']").attr("disabled","disabled"); 
				  	form.submit();
				 } 
			});
		});
	</script>
</body>
</html>
