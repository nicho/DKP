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
	<link href="${ctx}/static/styles/dkp.css" type="text/css" rel="stylesheet" />

	<title>查看活动</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" action="${ctx}/activity/${action}" method="post" class="form-horizontal">  
		<fieldset>
			<legend><small>查看活动</small></legend>
			
 <div class="bodycss">
  <div class="view_box">
    <ul>
      <li><span>标题 </span>${activity.title}</li>
      <li><span>开始时间 </span><fmt:formatDate value="${activity.startDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></li>
      <li><span>结束时间 </span><fmt:formatDate value="${activity.endDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></li>
      <li><span>活动积分 </span> ${activity.integral} </li> 
      <li><span>活动说明 </span>
        <div class="fl divwidth">${activity.activityExplain} </div>
      </li>
       <c:if test="${activity.status eq 'pass'}">
       <li><span>网站链接 </span>
        <a href="#" onclick="window.open('${activity.webUrl}')">${activity.webUrl}</a>  
      </li>
       <li><span>二维码 </span>
        <div class="fl divwidth"><img src="${HttpImageUrl}/${activity.qrCodeUrl}" width="350px" height="350px"/> </div>
      </li>
      </c:if>
      
      <div class="cl"></div>
    </ul> 
 
    <div class="cl"></div>
  </div> 
		 
		  
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
