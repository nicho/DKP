<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%	
	//设置返回码200，避免浏览器自带的错误页面
	response.setStatus(200);
	//记录日志
	Logger logger = LoggerFactory.getLogger("500.jsp");
	logger.error(exception.getMessage(), exception);
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>500 - </title>
<style type="text/css">
body {
	font-family: 'Microsoft Yahei', 'Simsun';
	background: #03aae2;
	margin: 0px;
	padding: 0px;
	color: #FFF;
}

h1, h2, h3, h4, h5, h6 {
	font-size: 100%;
}

.cl {
	clear: both;
}

.page404 {
	width: 700px;
	margin: 100px auto 0px auto;
}

.ico404 {
	float: left;
	width: 300px;
}

.ico404 img {
	width: 300px;
	height: 260px;
}

.textbox {
	float: left;
	font-size: 14px;
	padding-top: 40px;
}

.textbox p {
	text-align: justify;
	line-height: 18px;
}

.textbox h2 {
	font-size: 18px;
}

.textbox a {
	color: #fff;
	text-decoration: none;
}

.textbox a:hover {
	color: #fc0;
	text-decoration: none;
}
</style>
</head>
<body>
	<div class="page404">
		<div class="ico404">
			<img src="${ctx}/static/images/404ico.jpg" alt="" />
		</div>
		<div class="textbox">
			<h2>抱歉！你所要访问的页面不存在</h2>
			<p>点击以下链接继续浏览网站</p>
			<p>
				<a href="javascript:history.back()">>返回上一级页面</a>
			</p>
			<p>
				<a href="<c:url value="/"/>">>>返回网站首页</a>
			</p>
		</div>
		<div class="cl"></div>
	</div>
</body>
</html>