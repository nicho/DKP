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

	<title>惩罚登记</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/punish/${action}" method="post" class="form-horizontal">  
		<fieldset>
			<legend><small>惩罚登记</small></legend>
			 
		    <div class="control-group">
				<label class="control-label">惩罚名称:</label>
				<div class="controls">
					<input type="text" id="punishName" name="punishName" value="" class="input-large required"/>
				</div>
			</div>
									 <div class="control-group">
				<label class="control-label">惩罚项目:</label>
				<div class="controls">
					<select name="punishItemId" id="punishItem" class="required" onchange="changPunishItem(this)">
						<option value="">请选择</option>
						<c:forEach var="list" items="${ActivityTypeList}">
							<option value="${list.id }"  integral='${list.integral}' typeExplain='${list.typeExplain}'  >${list.typeName}:${list.typeExplain}</option>
						</c:forEach>
					</select>
				</div>
			</div>
					    <div class="control-group">
				<label class="control-label">扣除积分:</label>
				<div class="controls">
					<input type="text" id="integral" name="integral" value="" class="input-large required number"/>
				</div>
			</div>
				  
			 <div class="control-group">
				<label class="control-label">被处罚人:</label>
				<div class="controls">
					<select name="userId" class="required">
						<option value="" >请选择</option>
						<c:forEach var="list" items="${userList }">
							<option value="${list.id }" >${list.name }积分:<fmt:formatNumber value="${list.integral}" pattern="##.##"/></option>
						</c:forEach>
					</select>
				</div>
			</div>  
				  
			<div class="control-group">
				<label for="description" class="control-label">描述:</label>
				<div class="controls">
					<textarea id="description" rows="5" name="description" style="  width: 500px;"  class="input-large"></textarea>
				</div>
			</div>	
			
			
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
		
		function changPunishItem(obj)
		{  
			$('#integral').val($('#punishItem').find("option:selected").attr("integral"));
			$('#description').val($('#punishItem').find("option:selected").attr("typeExplain"));
		}
	</script>
</body>
</html>
