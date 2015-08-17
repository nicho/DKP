<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div id="header" class="row">
	<div>
		<h1>
			<a href="${ctx}">《DKP》公会系统</a><small>--离恨天</small>
		</h1>
	</div>
	<div class="pull-right">
		<shiro:guest>
			<a href="${ctx}/login">登录</a>
		</shiro:guest>
		<shiro:user>你好, <shiro:principal property="name" />。<font color='blue'>积分：<a href="${ctx}/integralHistory/myIntegeral/<shiro:principal property="id" />"><shiro:principal property="integral" /></a>
			</font>，权限：<shiro:principal property="rolesName" />
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
					<li class=""><a id="myTask" href="${ctx}/activity">最近所有活动列表</a></li>
					<li class=""><a id="myTask" href="${ctx}/admin/user/integralList">积分排行</a></li>
					<li class=""><a id="myTask" href="${ctx}/exchangeGoods/alllist">近期拍卖成功物品</a></li>
					<li class=""><a id="myTask" href="${ctx}/activity/create">发起活动</a></li>
					<li class="active"><a id="myTask" href="${ctx}/activityUser/myActivity">我参与的活动</a></li>
					<li class=""><a id="myTask" href="${ctx}/activity/myfqActivity">我发起的活动</a></li>
					<li class=""><a id="myTask" href="${ctx}/exchangeGoods/mylist">我兑换的物品</a></li>
					<li><a id="myTask" href="${ctx}/exchangeIntegralApply">我贡献的物品</a></li>
					<li><a id="account-tab" href="${ctx}/exchangeIntegral">贡献物品</a></li>
					<shiro:hasAnyRoles name="admin,Head">
						<li><a id="account-tab" href="${ctx}/exchangeGoods">物品拍卖登记</a></li>
						<li><a id="myTask" href="${ctx}/exchangeIntegralApply/approvalList">贡献物品审核</a></li> 
						<li><a id="myTask" href="${ctx}/exchangeIntegralApply/approvalAllList">历史贡献物品</a> </li> 
						<li><a id="account-tab" href="${ctx}/admin/user">帐号管理</a></li>
						<li><a id="account-tab" href="${ctx}/valueSet">活动类型设置</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="admin,Head,OneLevel,TwoLevel,ThreeLevel">
						<li><a id="myTask" href="${ctx}/activity/approvalConfirmList">积分发放审核</a></li>
						<li><a id="myTask" href="${ctx}/activity/approvalList">活动申请审核</a></li>
					</shiro:hasAnyRoles>
					<li><a id="account-tab" href="${ctx}/profile">资料修改</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>