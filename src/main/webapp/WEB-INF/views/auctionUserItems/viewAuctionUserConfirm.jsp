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
	<title>竞拍结果查看</title> 
</head>

<body>
	 
	<input type="hidden" name="auctionId" value="${auction.id}">
		<fieldset>
			<legend><small>竞拍结果查看</small></legend> 
	 <div class="bodycss">
  <div class="view_box">
    <ul>
      <li><span>竞拍物品 </span>${auction.goodsName}</li>
      <li><span>库存数量 </span>${auction.number}</li>
      <li><span>起拍价 </span>${auction.integral}</li>
      <li><span>创建时间 </span><fmt:formatDate value="${auction.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></li>
      <li><span>创建人 </span> ${auction.createUser.name} </li> 
      <li><span>描述 </span>
        <div class="fl divwidth">${auction.description} </div>
      </li>
   
      <div class="cl"></div>
    </ul> 
 
    <div class="cl"></div>
  </div> 
		 
		  
</div> 
			
			
	 <table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th> 
				<th>竞拍会员</th> 
				<th>会员游戏名</th> 
				<th>竞拍时间</th> 
				<th>竞拍积分(单价)</th> 
				<th>竞拍数量</th> 
				<th>竞拍积分(总价)</th> 
				<th>申请描述</th> 
				<th>状态</th>  
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${auctionUsers}" var="task">
				<tr>
					<td>${vstatus.index+1}</td>
					<td>${task.user.name}</td>
					<td>${task.user.gameName}</td> 
					<td><fmt:formatDate value="${task.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>   
					<td>${task.bidPrice}</td>
					<td>${task.number}</td>
					<td>${task.auctionIntegral}</td>
					<td>${task.description}</td>
					<td><c:if test="${task.status eq 'pass'}"><font color="red">竞拍成功</font></c:if></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	 
			   <div class="onebtn">  
				<input id="cancel_btn" class="orangebtn" type="button" value="返回" onclick="history.back()"/> 
			   </div>
		</fieldset>
	 
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
