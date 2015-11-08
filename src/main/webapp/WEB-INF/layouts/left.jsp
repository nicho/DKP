<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div id="leftbar" style="float: left; margin-left: 20px; width: 140px;" class="visible-desktop">
	
	<h1>公会展示</h1>
	<div class="submenu"> 
		<a id="myTask" href="${ctx}/activity">最近活动列表</a> 
		<a id="myTask" href="${ctx}/auction">公会拍卖物品</a> 
		<a id="myTask" href="${ctx}/auction/user">个人拍卖物品</a> 
		<a id="myTask" href="${ctx}/admin/user/integralList">个人积分排行</a>
		<a id="myTask" href="${ctx}/activityUser/activityUserRankingList">参与活动排行</a>
		<a id="myTask" href="${ctx}/punish/alllist">个人惩罚公告</a>
	<!-- 	<a id="myTask" href="${ctx}/exchangeGoods/alllist">近期拍卖成功物品</a>  -->

		 
	</div>
	<h1>个人页面</h1>
		<div class="submenu">
		<a id="myTask" href="${ctx}/activity/create">我要发起活动</a> 
		<a id="account-tab" href="${ctx}/exchangeIntegral">我要贡献物品</a> 
		<a id="myTask" href="${ctx}/activityUser/myActivity">我参与的活动</a>  
		<a id="myTask" href="${ctx}/activity/myfqActivity">我发起的活动</a>  
		<a id="myTask" href="${ctx}/auctionApply">我申请的拍卖</a>
		<a id="myTask" href="${ctx}/exchangeIntegralApply">我贡献的物品</a>  
	</div>
	 
	 <h1>系统管理</h1> 
		 
	<div class="submenu">
		  
		<shiro:hasAnyRoles name="admin,Head"> 
			<a id="myTask" href="${ctx}/exchangeIntegralApply/approvalAllList">历史贡献物品</a> 
			<a id="myTask" href="${ctx}/auctionApply/approvalAllList">历史拍卖物品</a>  
			<a id="myTask" href="${ctx}/punish">个人惩罚管理</a> 
			
			<a id="account-tab" href="${ctx}/valueSet">活动类型设置</a>  
			<a id="account-tab" href="${ctx}/penaltyValueSet">惩罚项目设置</a>  
			<a id="account-tab" href="${ctx}/admin/user">会员帐号管理</a>   
			<a id="account-tab" href="${ctx}/guild/guildInformation">公会公告信息</a>   
		</shiro:hasAnyRoles>
		 
		<a id="account-tab" href="${ctx}/profile">个人资料修改</a>
		
			 <a id="account-tab" href="${ctx}/auction/user/approvalAuctionList">个人竞拍审核</a> 
		 	 <a id="account-tab" href="${ctx}/auctionApply/user/approvalList">个人拍卖审核</a> 
	</div>

	<shiro:hasAnyRoles name="admin,Head,OneLevel,TwoLevel">
	 <h1>审核</h1> 
	 <div class="submenu">
	 		<shiro:hasAnyRoles name="admin,Head">
		 		<a id="account-tab" href="${ctx}/auction/approvalAuctionList">公会竞拍审核</a> 
		 		<a id="account-tab" href="${ctx}/auctionApply/approvalList">公会拍卖审核</a> 
				<a id="myTask" href="${ctx}/exchangeIntegralApply/approvalList">贡献物品审核</a>
			</shiro:hasAnyRoles>
			
		    <shiro:hasAnyRoles name="admin,Head,OneLevel,TwoLevel">
				<a id="myTask" href="${ctx}/activity/approvalConfirmList">积分发放审核</a>
				<a id="myTask" href="${ctx}/activity/approvalList">活动发起审核</a> 
			</shiro:hasAnyRoles>
	 </div>
	 </shiro:hasAnyRoles>

</div>