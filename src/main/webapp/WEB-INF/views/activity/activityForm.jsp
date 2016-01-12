<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

	<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/demo.css">
	<script type="text/javascript" src="${ctx}/static/easyui/jquery.easyui.min.js"></script>

	<title>创建活动</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/activity/${action}" method="post" class="form-horizontal">  
		<fieldset>
			<legend><small>创建活动</small></legend>
			 <div class="control-group">
				<label class="control-label">活动级别:</label>
				<div class="controls">
					<select name="fType" class="required" onclick="onchangefType(this)">
						<option value="">请选择</option>
						<option value="PersonalActivities">个人活动</option>
						<shiro:hasAnyRoles name="admin,Head,OneLevel,TwoLevel,ThreeLevel">
							<option value="AssociationActivity">公会活动</option>
						</shiro:hasAnyRoles>
					</select>
				</div>
			</div>
						 <div class="control-group">
				<label class="control-label">标题:</label>
				<div class="controls">
					<input type="text" id="title" name="title" value="" class="input-large required"/>
				</div>
			</div>
						 <div class="control-group">
				<label class="control-label">活动类型:</label>
				<div class="controls">
					<select name="activityType" class="required">
						<option value="">请选择</option>
						<c:forEach var="list" items="${ActivityTypeList}">
							<option value="${list.id }"  >${list.typeName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
									 <div class="control-group">
				<label class="control-label">人数规模:</label>
				<div class="controls">
					<select name="personCount" class="required" onchange="">
						<option value="">请选择</option> 
						<option value="≤10">≤10</option> 
						<option value="≤20">≤20</option> 
						<option value="≤30">≤30</option> 
						<shiro:hasAnyRoles name="admin,Head,OneLevel,TwoLevel">
							<option value="≤50">≤50</option>
							<option value="≤100">≤100</option>
							<option value="≤200">≤200</option>
						</shiro:hasAnyRoles>
						<shiro:hasAnyRoles name="admin,Head,OneLevel">
							<option value="≤300">≤300</option> 
							<option value="≤400">≤400</option> 
							<option value="≤500">≤500</option> 
						</shiro:hasAnyRoles>
					    <shiro:hasAnyRoles name="admin,Head">
							<option value="≤600">≤600</option> 
							<option value="≤700">≤700</option> 
							<option value="≤800">≤800</option> 
							<option value="≤900">≤900</option> 
							<option value="≤1000">≤1000</option> 
						</shiro:hasAnyRoles>
					</select>
				</div>
			</div>
			 <div class="control-group">
				<label for="task_title" class="control-label">开始时间:</label>
				<div class="controls">
					<input type="text" id="startDateStr" name="startDateStr"  value="<fmt:formatDate  value="${task.startDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />" class="input-large required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  />
				</div>
			</div>	
			 <div class="control-group">
				<label for="task_title" class="control-label">结束时间:</label>
				<div class="controls">
					<input type="text" id="endDateStr" name="endDateStr"  value="<fmt:formatDate  value="${task.endDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />" class="input-large required"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				</div>
			</div>	
			
			
		   <div class="control-group" id="jifenDiv" style="display:none;">
				<label class="control-label">活动积分:</label>
				<div class="controls">
					<input type="text" id="integral" name="integral" value="${user.integral}" class="input-large required digits" min="3"/>
				</div>
			</div>
			
			<div class="control-group">
				<label for="description" class="control-label">活动说明:</label>
				<div class="controls">
					<textarea id="codes" rows="5" name="activityExplain" style="  width: 500px;"  class="input-large"></textarea>
				</div>
			</div>	
			
			
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
	<script>
	 function onchangefType(obj)
	 {
		 	if(obj.value=="PersonalActivities")
			 { 
		 		$('#jifenDiv').attr("style","display:block;");
			 }
		 	if(obj.value=="AssociationActivity")
			 { 
		 		$('#jifenDiv').attr("style","display:none;");
			 }
	 }
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
