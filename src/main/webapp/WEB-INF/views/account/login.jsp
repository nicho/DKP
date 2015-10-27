<%@ page contentType="text/html;charset=UTF-8" import="com.gamewin.weixin.util.ReadProperties"%>
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
<title>《DKP》公会系统:</title>
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
<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
<link href="${ctx}/static/styles/dkp.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/bootstrap/2.3.2/css/bootstrap-responsive.min.css" rel="stylesheet">
</head>

<body> 
 <form id="loginForm" action="${ctx}/login" method="post" class="form-horizontal">
   <c:if test="${not empty message}">
	     <div class="alert alert-success controls input-large">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
		</c:if>
		<%
			String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
			if(error != null){
		%>
				<div class="alert alert-error controls input-large">
					<button class="close" data-dismiss="alert">×</button>
		<%
				if(error.contains("DisabledAccountException")){
					out.print("用户已被屏蔽,请登录其他用户.");
				}
				else if(error.contains("ConcurrentAccessException")){
					out.print("用户正在审批,请等待.");
				} 
				else{
					out.print("登录失败，请重试.");
				}
		%>		
				</div>
		<%
			}
		%>
		<br>
 <div class="bodycss">
  <div class="logindiv">
    <div class="loge  "> <h1><%=ReadProperties.getDomainMap().get("orgName")%></h1></div>
    <div class="loginbox">
      <p><input id="username" name="username"  type="text" class="medium-input userico required" value="${username}" /><div class="cl"></div></p>
      <p><input id="password" name="password" type="password" class="medium-input passwordico required"  autocomplete="off"/><div class="cl"></div></p>
	  <p><input name="rememberMe" type="checkbox" value="true" class="checkbox" checked><span class="grayfont">记住我</span><span class="fr link"><a href="${ctx}/register">注册</a></span></p>
	  <p><input name="submit_btn" type="submit" value="登&ensp;&ensp;录" class="orangebtn"></p>
    </div>
    <div class="loginpic "></div>
  <div class="visible-desktop"> </div>
  </div>
    
</div>
</form>
 
	

	<script>
		$(document).ready(function() {
			$("#loginForm").validate();
			
			
			   var obj = new WxLogin({
                   id:"login_container", 
                   appid: "wxf867a7a208f34432", 
                   scope: "snsapi_login", 
                   redirect_uri: "http://dkp.lihentian.com/DKP/weixinUser/bindUserOpenId",
                   state: "",
                   style: "",
                   href: ""
                 });
		});
	</script>
		
 
	<script src="${ctx}/static/bootstrap/2.3.2/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>