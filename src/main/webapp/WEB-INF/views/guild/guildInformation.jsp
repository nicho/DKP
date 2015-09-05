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

	<title>公会信息</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" action="${ctx}/guild/updateGuildInformation" method="post" class="form-horizontal">  
	<input type="hidden" name="id" value="${org.id}">
		<fieldset>
			<legend><small>公会信息</small></legend>
			 <div class="control-group">
				<label class="control-label">公会名称:</label>
				<div class="controls">
					${org.orgName}
				</div>
			</div>
		  <div class="control-group">
				<label class="control-label">创建时间:</label>
				<div class="controls">
					<fmt:formatDate value="${org.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />
				</div>
			</div>
			 
			<div class="control-group">
				<label for="description" class="control-label">描述:</label>
				<div class="controls">
					${org.description}
				</div>
			</div>	
			
		   <div class="control-group">
				<label for="description" class="control-label">公告:</label>
				<div class="controls">
					<textarea id="codes" rows="5" name="notice" style="  width: 500px;"  class="input-large"> ${org.notice}</textarea>
				</div>
			</div>	
			
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="保存"/>&nbsp;	 
			</div>
		</fieldset>
	</form>
	 
</body>
</html>
