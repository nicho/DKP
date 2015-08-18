
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>

<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/demo.css">
<script type="text/javascript" src="${ctx}/static/easyui/jquery.easyui.min.js"></script>
<link href="${ctx}/static/styles/dkp.css" type="text/css" rel="stylesheet" />

<title>活动登记</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/activity/registerActivity" method="post" class="form-horizontal">
		<input type="hidden" name="activityId" value="${activity.id}"> 
		   
		<fieldset>
			<legend>
				<small>活动登记</small>
			</legend>
			
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
      <div class="cl"></div>
    </ul> 
  <div class="onebtn"> 
    		 <c:if test="${isclose eq 'Y'}"><a href="${ctx}/activity">活动已停止登记,点击进入最近活动列表</a></c:if>
			 <c:if test="${isclose != 'Y'}"><input id="submit_btn" class="orangebtn" type="submit" value="活动登记" />&nbsp;</c:if>
  </div>
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
				submitHandler : function(form) { //通过之后回调 
					$("input[type='submit']").attr("disabled", "disabled");
					form.submit();
				}
			});
			
		 
		});
	</script>
</body>
</html>
