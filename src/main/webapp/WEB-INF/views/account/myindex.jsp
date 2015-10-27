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
<title>《DKP》</title>
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
 
	<title>我的首页</title>
	
	<script>
		$(document).ready(function() {
			 
		});
	</script>
	<style>
	.table th, .table td { 
	    line-height: 60px;
	    text-align: center; 
	    vertical-align: middle;
	    font-size: 15px;
	}
	</style>
</head>

<body>
   <c:if test="${not empty message}">
	     <div class="alert alert-success controls input-large">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
		</c:if>
<table cellspacing="0" cellpadding="0" class="table table-striped table-bordered table-condensed" >
  <col width="98" />
  <col width="72" span="3" />
  <tr>
    <td colspan="4" width="314">
    <div>
		<h3>
			<a href="${ctx}">《DKP》公会系统</a><small style="font-size: 23px;">--<%=ReadProperties.getDomainMap().get("orgName")%></small>
		</h3>
	</div></td>
  </tr>
   <tr>
    <td colspan="4"><font color="red">${orgNotice}</font></td>
  </tr>
  <tr>
    <td colspan="2" style="width: 40%">
    昵称:<shiro:principal property="name" /><br>
    <font color='blue'>积分：<a href="${ctx}/integralHistory/myIntegeral/<shiro:principal property="id" />"><shiro:principal property="integral" /></a>
			</font><br>
    权限：<shiro:principal property="rolesName" /><br>
    </td>
    <td colspan="2"> <a id="myTask" href="${ctx}/activity">最近活动列表</a></td>
  </tr>
  <tr>
    <td colspan="2"><a id="myTask" href="${ctx}/activityUser/myActivity">我参与的活动</a>  </td>
    <td colspan="2"><a id="myTask" href="${ctx}/auction">近期拍卖物品</a></td>
  </tr>
  <tr>
    <td colspan="2"><a id="myTask" href="${ctx}/activity/create">我要发起活动</a></td>
    <td colspan="2"><a id="myTask" href="${ctx}/exchangeIntegralApply">我贡献的物品</a>  </td>
  </tr>
  <tr>
    <td colspan="4"><a href="${ctx}/logout">退出登录</a></td>
  </tr>
</table>
	<script src="${ctx}/static/bootstrap/2.3.2/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>