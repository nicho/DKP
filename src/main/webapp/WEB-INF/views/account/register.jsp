<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title>《DKP》系统:</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0"/>
<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
<link href="${ctx}/static/bootstrap/2.3.2/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.11.1/messages_bs_zh.js" type="text/javascript"></script>
<link href="${ctx}/static/styles/dkp.css" type="text/css" rel="stylesheet" /> 
 
	<title>用户注册</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#loginName").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					loginName: {
						remote: "${ctx}/register/checkLoginName"
					},
					gameName: {
						remote: "${ctx}/register/checkGameName"
					} 
				},
				messages: {
					loginName: {
						remote: "用户登录名已存在"
					}, 
					gameName: {
						remote: "游戏名已存在"
					}  
				}
			});
		});
	</script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>

 
	<form id="inputForm" action="${ctx}/register" method="post" class="form-horizontal">
	<input type="hidden" name="openId" value="${openId}">
 
 <div class="bodycss">
 	<div>
 	<h3><a href="${ctx}">《DKP》公会系统</a><small>--</small> <small>用户注册</small></h3>
 	</div>
  <div class="view_box mb15">
    <ul>
      <li><span>登录名</span>
      	<input type="text" id="loginName" name="loginName" class="input-large required" minlength="3"/><div class="cl"></div></li>
      <li><span>昵称</span>
      <input type="text" id="name" name="name" class="input-large required"/><div class="cl"></div></li> 
      <li><span>游戏名</span>
         <input type="text" id="gameName" name="gameName" class="input-large required"/><div class="cl"></div>
      </li>
            <li><span>邮箱</span>
        <input type="text" id="email" name="email" class="input-large required"/><div class="cl"></div>
      </li>
            <li><span>电话</span>
         <input type="text" id="phone" name="phone" class="input-large required"/><div class="cl"></div>
      </li>
            <li><span>密码</span>
         <input type="password" id="plainPassword" name="plainPassword" class="input-large required"/><div class="cl"></div>
      </li>
            <li><span>确认密码</span>
         <input type="password" id="confirmPassword" name="confirmPassword" class="input-large required" equalTo="#plainPassword"/><div class="cl"></div>
      </li>
      
       
      <div class="cl"></div>
    </ul>
  
     <div class="twobtn"><input id="saveBtn" name="" type="button" class="graybtn" value="返&ensp;回" onclick="history.back()"><input id="submitButton" name="submitButton" type="submit" class="brownbtn" value="提&ensp;交"  ></div>
    
    <div class="cl"></div>
  </div>
  
</div> 
	</form>
 
		 
	<script src="${ctx}/static/bootstrap/2.3.2/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>