<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div id="header" class="row">
	<div><h1><a href="${ctx}">《DKP》公会系统</a><small>--离恨天</small></h1></div>
	<div class="pull-right">
		<shiro:guest><a href="${ctx}/login">登录</a></shiro:guest>
		<shiro:user>你好, <shiro:principal property="name"/>。<font color='red'>积分：1000 </font>，权限：<shiro:principal property="roles"/>
		 <a href="${ctx}/logout">退出登录</a></shiro:user>
	</div>
</div>