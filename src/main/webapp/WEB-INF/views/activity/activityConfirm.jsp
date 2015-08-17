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

	<title>积分发放</title>
	
	<script lang="javascript" type="text/javascript">
$(document).ready(function() {
	$("#chk_all").click(function(){ 
			var checkedVal=$(this).prop("checked");
		
	
			 $("input[name='chk_list']").each(function(){
			 	if($(this).prop("disabled")==false)
			 	{ 
			 		$(this).prop("checked",checkedVal);
			 	}
			//处理方法
			});
	});
 
});
</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/activity/activityConfirm" method="post" class="form-horizontal">  
	<input type="hidden" name="activityId" value="${activity.id}">
		<fieldset>
			<legend><small>积分发放</small></legend> 
	<div class="hidden-desktop">
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

<div class="visible-desktop"> 
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
			
			
	 <table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input  name="chk_all" type="checkbox"   id="chk_all" /></th> 
				<th>登记会员</th> 
				<th>会员游戏名</th> 
				<th>职责</th> 
				<th>登记时间</th> 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${activityUsers}" var="task">
				<tr>
					<td><input  name="chk_list"  id="chk_list_${vstatus.index+1}" type="checkbox"  value="${task.user.id}"   /></td>
					<td>${task.user.name}</td>
					<td>${task.user.gameName}</td>
					<td>
					<c:choose>
							<c:when test="${user.user.roles eq 'admin'}">管理员</c:when>
							<c:when test="${user.user.roles eq 'Head'}">团长</c:when>
							<c:when test="${user.user.roles eq 'OneLevel'}">一级</c:when>
							<c:when test="${user.user.roles eq 'TwoLevel'}">二级</c:when>
							<c:when test="${user.user.roles eq 'ThreeLevel'}">三级</c:when>
							<c:otherwise>会员</c:otherwise>
						</c:choose>
					</td> 
					<td><fmt:formatDate value="${task.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>   
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
			 <div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
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
