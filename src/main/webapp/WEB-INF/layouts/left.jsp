<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div id="leftbar" style="float: left; margin-left: 20px; width: 140px;" class="visible-desktop">
	<h1>公会展示</h1>
	<div class="submenu"> 
		<a id="myTask" href="${ctx}/activity">最近所有活动列表</a>
		<a id="myTask" href="${ctx}/admin/user/integralList">积分排行</a>
		<a id="myTask" href="${ctx}/exchangeGoods/alllist">近期拍卖成功物品</a> 

		 
	</div>
	<h1>个人页面</h1>
		<div class="submenu">
		<a id="myTask" href="${ctx}/activity/create">发起活动</a> 
		<a id="myTask" href="${ctx}/activityUser/myActivity">我参与的活动</a>  
		<a id="myTask" href="${ctx}/activity/myfqActivity">我发起的活动</a>  
		<a id="myTask" href="${ctx}/exchangeGoods/mylist">我兑换的物品</a>
		<a id="myTask" href="${ctx}/exchangeIntegralApply">我贡献的物品</a>  
	</div>
	 <h1>系统管理</h1> 
		 
	<div class="submenu">
		
		
		<a id="account-tab" href="${ctx}/exchangeIntegral">贡献物品</a> 
		
 
		<shiro:hasAnyRoles name="admin,Head">
			<a id="account-tab" href="${ctx}/exchangeGoods">物品拍卖登记</a> 
			<a id="myTask" href="${ctx}/exchangeIntegralApply/approvalList">贡献物品审核</a>
			
			<a id="account-tab" href="${ctx}/admin/user">帐号管理</a>   
			<a id="account-tab" href="${ctx}/valueSet">活动类型设置</a>  
		</shiro:hasAnyRoles>
		
		
		<shiro:hasAnyRoles name="admin,Head,OneLevel,TwoLevel">
			<a id="myTask" href="${ctx}/activity/approvalConfirmList">活动确认审核</a>
			<a id="myTask" href="${ctx}/activity/approvalList">活动发起审核</a>
		
		</shiro:hasAnyRoles>
		
		
		<a id="account-tab" href="${ctx}/profile">资料修改</a>
	</div>



</div>