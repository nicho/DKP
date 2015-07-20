<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div id="leftbar" style="float: left; margin-left: 20px; width: 140px;">
	<h1>展示页面</h1>
	<div class="submenu">
		<a id="myTask" href="${ctx}/activity/create">活动发起</a>
		<a id="myTask" href="${ctx}/manageTask/myTask">活动登记</a>
		<a id="myTask" href="${ctx}/activity">最近所有活动列表</a>
		<a id="myTask" href="${ctx}/manageTask/myTask">积分排行</a>
		<a id="myTask" href="${ctx}/exchangeGoods">近期拍卖成功物品</a> 

		 
	</div>
	<h1>个人页面</h1>
		<div class="submenu">
		<a id="myTask" href="${ctx}/manageTask/myTask">发起活动</a> 
		<a id="myTask" href="${ctx}/manageTask/myTask">我参与的活动</a>
		<a id="myTask" href="${ctx}/manageTask/myTask">我兑换的物品</a>
		<a id="myTask" href="${ctx}/manageTask/myTask">我的拍卖</a>
		<a id="myTask" href="${ctx}/manageTask/myTask">我贡献的物品</a>  
	</div>
	 <h1>积分管理</h1>
		<div class="submenu">
		<a id="account-tab" href="${ctx}/exchangeIntegral">物品兑换积分</a> 
		<a id="account-tab" href="${ctx}/exchangeGoods">物品拍卖登记</a> 
		
		<a id="myTask" href="${ctx}/exchangeIntegralApply">我的申请</a>
		<a id="myTask" href="${ctx}/exchangeIntegralApply/approvalList">物品兑换积分审核</a>
	</div>

	<h1>系统管理</h1>
	<div class="submenu">

		<shiro:hasAnyRoles name="admin">
			<a id="account-tab" href="${ctx}/admin/user">帐号管理</a>  
		</shiro:hasAnyRoles>
		<a id="account-tab" href="${ctx}/valueSet">活动类型设置</a>  
		<a id="account-tab" href="${ctx}/profile">资料修改</a>
	</div>



</div>