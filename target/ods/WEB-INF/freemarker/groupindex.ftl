<html>
<head>
    <script src="${absoluteContextPath}/js/group.js" type="text/javascript"></script>
</head>
<body>
<h1>${group.groupName}</h1>
<hr/>

<#if boss?exists>
<input type="text" id="membername"/> <input type="button" id="addmember" value="邀请好友"/>
</#if>
<input type="text" id="newname"/>
<input type="button" id="changename" value="设置我的群内昵称"/>
<h2>小组成员</h2>
<#list members as user>
    <p> ${user.userName} - ${nicknames[user_index]} - ${user.location}</p>
</#list>
<input type="hidden" id="groupid" value="${group.id}"/>



<#if boss?exists>
<hr/>
<!-- 订餐div -->
<div>
    <h2>发布新的订单</h2>
    <form id="order">
    订餐网站URL:<input type="text" id="orderurl" name="orderUrl"/>
    订餐消息备注:<input type="text" id="ordermark" name="orderMark"/>
        订餐类型:<input type="radio" name="ordertype" checked="true"/>早餐
        <input type="radio" name="ordertype"/>午餐
        <input type="radio" name="ordertype"/>晚餐
        <input type="radio" name="ordertype"/>其他
        下单时间:<input type="text" id="orderend"/>
    <input type="submit" value="提交"/>
    </form>
</div>
</#if>

<hr/>
<h2>最近的订单:</h2>
<div class="search-rules">
    <form id="search-form">
				<span>
					<div class="control-group">
                        <label class="control-label">日期范围：</label>
                        <div class="bui-form-group controls" data-rules="{dateRange : true}">
                            <input name="start" type="text" class="calendar" id="start"/> - <input name="end" type="text" class="calendar" id="end"/>
                        </div>
                    </div>
		           	<div class="control-group">
                        <label class="control-label">订单网址：</label>
                        <div class="controls">
                            <input name="website" type="text" class="input-large" id="url">
                        </div>
                    </div>
			       	<div class="control-group">
                        <button type="button" class="button button-primary" id="selectOrder">查询</button>
                    </div>
			    </span>
    </form>
    <hr/>
    <div id = "grid"></div>
    <h1>今日订单</h1>
    <div id = "today-order"></div>
</div>
<div class="search-results">
</div>



</body>
</html>