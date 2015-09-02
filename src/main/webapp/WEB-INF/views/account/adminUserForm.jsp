<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>用户管理</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/admin/user/update" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${user.id}"/>
		<fieldset>
			<legend><small>用户管理</small></legend>
			<div class="control-group">
				<label class="control-label">登录名:</label>
				<div class="controls">
					<input type="text" value="${user.loginName}" class="input-large" disabled="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">昵称:</label>
				<div class="controls">
					<input type="text" id="name" name="name" value="${user.name}" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">级别:</label>
				<div class="controls">   
					<select name="roles">
					<shiro:hasRole name="admin">
						<option value="admin" <c:if test="${user.roles eq 'admin'}">selected</c:if>>管理员</option>
						<option value="Head" <c:if test="${user.roles eq 'Head'}">selected</c:if>>团长</option>
					</shiro:hasRole>
						<option value="OneLevel" <c:if test="${user.roles eq 'OneLevel'}">selected</c:if>>一级</option>
						<option value="TwoLevel" <c:if test="${user.roles eq 'TwoLevel'}">selected</c:if>>二级</option>
						<option value="ThreeLevel" <c:if test="${user.roles eq 'ThreeLevel'}">selected</c:if>>三级</option>
						<option value="user" <c:if test="${user.roles eq 'user'}">selected</c:if>>会员</option>
					</select>
				</div>
			</div>
		   
		    <div class="control-group">
				<label class="control-label">游戏名:</label>
				<div class="controls">
					<input type="text" id="gameName" name="gameName" value="${user.gameName}" class="input-large"/>
				</div>
			</div>
				    <div class="control-group">
				<label class="control-label">邮箱:</label>
				<div class="controls">
					<input type="text" id="email" name="email" value="${user.email}" class="input-large"/>
				</div>
			</div>
				    <div class="control-group">
				<label class="control-label">电话:</label>
				<div class="controls">
					<input type="text" id="phone" name="phone" value="${user.phone}" class="input-large"/>
				</div>
			</div>
			<div class="control-group">
				<label for="plainPassword" class="control-label">密码:</label>
				<div class="controls">
					<input type="password" id="plainPassword" name="plainPassword" class="input-large" placeholder="...Leave it blank if no change"/>
				</div>
			</div>
			<div class="control-group">
				<label for="confirmPassword" class="control-label">确认密码:</label>
				<div class="controls">
					<input type="password" id="confirmPassword" name="confirmPassword" class="input-large" equalTo="#plainPassword" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">注册日期:</label>
				<div class="controls">
					<span class="help-inline" style="padding:5px 0px"><fmt:formatDate value="${user.registerDate}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></span>
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
			$("#name").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</body>
</html>
