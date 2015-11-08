<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>公会物品列表</title>

<link href="${ctx}/static/styles/dkp.css" type="text/css" rel="stylesheet" />

	<script>
		$(document).ready(function() {
			 if('${message}'!=null && '${message}'!='')
			 {
				 alert('${message}');
			 }
		});
	</script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="visible-desktop"> 
	<div class="row">
		<div class="span4 offset7">
			<form class="form-search" action="#"></form>
		</div>

	</div>
		<shiro:hasAnyRoles name="admin,Head">
			<div><a class="btn" href="${ctx}/auction/create">添加拍卖物品</a></div> 
		</shiro:hasAnyRoles>
	<br>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>拍卖物品</th>
				<th style="width: 40%;">描述</th>
				<th>库存数量</th> 
				<th>积分</th> 
				<th>限购数量</th> 
				<th>是否竞拍物品</th> 
				<th>创建时间</th>
				<th>创建人</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${auctions.content}" var="task">
				<tr>
					<td>${task.goodsName}</td>
					<td>${task.description}</td>
					<td>${task.number}</td> 
					<td><fmt:formatNumber value="${task.integral}" pattern="##.##"/></td>
					<td>${task.limitedNumber}</td>
					<td><c:if test="${task.isAuction eq 'Y'}">是</c:if><c:if test="${task.isAuction != 'Y'}">否</c:if></td>
					<td><fmt:formatDate value="${task.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${task.createUser.name}</td>

					<td>
					<c:if test="${task.number != 0}">
						<c:if test="${task.isAuction eq 'Y'}">
							<c:if test="${task.status eq 'Y'}"><a href="${ctx}/auctionUser/create/${task.id}" >参与竞拍</a>&nbsp;</c:if> 
						</c:if>
						<c:if test="${task.isAuction != 'Y'}"><a href="${ctx}/auctionApply/create/${task.id}" >购买</a>&nbsp;</c:if>
						
					</c:if>
					 <c:if test="${task.isAuction eq 'Y'}"> 
							<c:if test="${task.status eq 'pass'}"><a href="${ctx}/auction/viewAuctionUser/${task.id}" >查看竞拍结果</a> &nbsp;</c:if>
					 </c:if>
					<shiro:hasAnyRoles name="admin,Head">
							<c:if test="${task.status eq 'Y'}">
								<a href="${ctx}/auction/update/${task.id}"  >修改</a>&nbsp; 
							</c:if>
						<a href="#" onclick="confirmDelete('${ctx}/auction/delete/${task.id}')">删除</a>&nbsp; 
					</shiro:hasAnyRoles>
					</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>



<div class="hidden-desktop">
  <div class="listleave_box">
    <div class="listleave_bar">拍卖物品列表</div>
    <ul>
 	<c:forEach items="${auctions.content}" var="task">
      <li>
        <div class="listleave_text">
          <p><span>拍卖物品 </span>${task.goodsName}</p>
          <p><span>描述 </span>${task.description}</p>
          <p><span>库存数量 </span>${task.number}</p>
          <p><span>积分 </span><fmt:formatNumber value="${task.integral}" pattern="##.##"/></p>
          <p><span>创建时间 </span><fmt:formatDate value="${task.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></p> 
       
		  <div class="cl"></div>
        </div>
		<div class="morebtn">
		<c:if test="${task.number != 0}"> 
						 <c:if test="${task.isAuction eq 'Y'}">
							<c:if test="${task.status eq 'Y'}"><input name="" type="button" class="orangebtn2" value="参与竞拍" onClick="location.href='${ctx}/auctionUser/create/${task.id}'"></c:if> 
						</c:if>
						<c:if test="${task.isAuction != 'Y'}"><input name="" type="button" class="orangebtn2" value="购买" onClick="location.href='${ctx}/auctionApply/create/${task.id}'"></c:if>
	    </c:if>
	    			  <c:if test="${task.isAuction eq 'Y'}"> 
							<c:if test="${task.status eq 'pass'}"> <input name="" type="button" class="orangebtn2" value="查看竞拍结果" onClick="location.href='${ctx}/auction/viewAuctionUser/${task.id}'"></c:if>
					 </c:if>
		</div>
   		
      </li>
     </c:forEach> 
    </ul>
    <div class="cl"></div>
  </div>
 </div>
	<tags:pagination page="${auctions}" paginationSize="10" />


</body>
</html>
