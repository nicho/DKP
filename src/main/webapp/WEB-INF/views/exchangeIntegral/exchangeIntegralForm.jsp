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

	<title>物品兑换积分列表</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/exchangeIntegral/${action}" method="post" class="form-horizontal">  
	<input type="hidden" id="id" name="id" value="${exchangeIntegral.id}"  />
		<fieldset>
			<legend><small>可兑换积分登记</small></legend>
			 
		    <div class="control-group">
				<label class="control-label">物品名称:</label>
				<div class="controls">
					<input type="text" id="goodsName" name="goodsName" value="${exchangeIntegral.goodsName}" class="input-large required"/>
				</div>
			</div>
					    <div class="control-group">
				<label class="control-label">可兑换积分:</label>
				<div class="controls">
					<input type="text" id="integral" name="integral" value="${exchangeIntegral.integral}" class="input-large required number"/>
				</div>
			</div>   
							    <div class="control-group">
				<label class="control-label">每日限制数量:</label>
				<div class="controls">
					<input type="text" id="limitedNumber" name="limitedNumber" value="${exchangeIntegral.limitedNumber}" class="input-large required digits"/>
				</div>
			</div>   
			<div class="control-group">
				<label for="description" class="control-label">描述:</label>
				<div class="controls">
					<textarea id="codes" rows="5" name="description" style="  width: 500px;"  class="input-large">${exchangeIntegral.description}</textarea>
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
	</script>
</body>
</html>
