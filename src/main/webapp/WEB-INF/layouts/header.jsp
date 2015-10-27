<%@ page language="java" pageEncoding="UTF-8" import="com.gamewin.weixin.util.ReadProperties"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div id="header" class="row">
	<div>
		<h3>
			<a href="${ctx}">《DKP》公会系统</a><small style="font-size: 23px;">--<%=ReadProperties.getDomainMap().get("orgName")%></small>
		</h3>
	</div>
	<div class="pull-right">
		<shiro:guest>
			<a href="${ctx}/login">登录</a>
		</shiro:guest>
		<shiro:user>你好, <shiro:principal property="name" />。 
			 权限：<shiro:principal property="rolesName" />
			<a href="${ctx}/logout">退出登录</a>
		</shiro:user>
	</div>


</div>

<div class="navbar navbar-fixed-top hidden-desktop">
	<div class="navbar-inner">
		<div class="container">
			<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="brand" href="./index.html">菜单</a>
			<div class="nav-collapse collapse">
				<ul class="nav">
					<li class=""><a id="myTask" href="${ctx}/activity">最近活动列表</a></li>
					<li class=""><a id="myTask" href="${ctx}/auction">近期拍卖物品</a></li> 
					<li class=""><a id="myTask" href="${ctx}/admin/user/integralList">个人积分排行</a></li>
					<li class=""><a id="myTask" href="${ctx}/activityUser/activityUserRankingList">参与活动排行</a></li>
					<li class=""><a id="myTask" href="${ctx}/activity/create">我要发起活动</a></li>
									<li><a id="account-tab" href="${ctx}/exchangeIntegral">我要贡献物品</a></li>
					<li class="active"><a id="myTask" href="${ctx}/activityUser/myActivity">我参与的活动</a></li>
					<li class=""><a id="myTask" href="${ctx}/activity/myfqActivity">我发起的活动</a></li>
					<li class=""><a id="myTask" href="${ctx}/auctionApply">我申请的拍卖</a></li>
					<li><a id="myTask" href="${ctx}/exchangeIntegralApply">我贡献的物品</a></li> 
					<shiro:hasAnyRoles name="admin,Head"> 
						<li><a id="myTask" href="${ctx}/exchangeIntegralApply/approvalAllList">历史贡献物品</a> </li> 
						<li><a id="myTask" href="${ctx}/auctionApply/approvalAllList">历史拍卖物品</a> </li>  
						<li><a id="account-tab" href="${ctx}/valueSet">活动类型设置</a></li>
						<li><a id="account-tab" href="${ctx}/admin/user">会员帐号管理</a></li>
						<li><a id="account-tab" href="${ctx}/guild/guildInformation">公会公告信息</a></li> 
					</shiro:hasAnyRoles>
					
					<shiro:hasAnyRoles name="admin,Head"> 
						<li><a id="myTask" href="${ctx}/auction/approvalAuctionList">物品竞拍审核</a></li>  
						<li><a id="myTask" href="${ctx}/auctionApply/approvalList">物品拍卖审核</a></li>  
						<li><a id="myTask" href="${ctx}/exchangeIntegralApply/approvalList">贡献物品审核</a></li>  
					</shiro:hasAnyRoles>
					
					<shiro:hasAnyRoles name="admin,Head,OneLevel,TwoLevel,ThreeLevel">
						<li><a id="myTask" href="${ctx}/activity/approvalConfirmList">积分发放审核</a></li>
						<li><a id="myTask" href="${ctx}/activity/approvalList">活动申请审核</a></li>
					</shiro:hasAnyRoles>
					
					
					<li><a id="account-tab" href="${ctx}/profile">个人资料修改</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>