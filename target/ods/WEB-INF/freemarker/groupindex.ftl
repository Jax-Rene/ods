<html>
<head>
    <script src="${absoluteContextPath}/js/group.js" type="text/javascript"></script>
</head>
<body>
<div class="wrapper">
    <h1>${group.groupName}</h1>
    <hr/>

<#if boss?exists>
    <input type="text" id="membername"/> <input type="button" id="addmember" value="邀请好友"/>
    <input type="hidden" id="boss" value="true"/>
</#if>
<#if boss?exists || member?exists>
    <span>当前昵称: ${nickName}</span>
    <input type="text" id="newname"/>
    <input type="button" id="changename" value="设置我的群内昵称"/>
<#else>
    <input type="button" id="addGroup" value="加入该小组"/>
</#if>

    <hr/>
    <h2>小组成员</h2>

    <div class="search-results">
        <div id="memberGrid"></div>
    </div>
<#list members as user>
    <p> ${user.userName} - ${nicknames[user_index]}</p>
</#list>
    <input type="hidden" id="groupid" value="${group.id}"/>
<#if boss?exists>
    <hr/>
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


    <div id="group-orders" class="container">
        <div class="today-order">
            <h1>今日订单</h1>
            <div id="today-order"></div>
            <span id="total-count"></span>
        </div>
        <hr/>

        <div class="history-order">
            <h1>历史订单</h1>

            <div class="search-rules">
                <form id="search-form">
					<span>
						<div class="control-group">
                            <label class="control-label">日期范围：</label>

                            <div class="bui-form-group controls" data-rules="{dateRange : true}">
                                <input name="start" type="text" class="calendar"/> - <input name="end" type="text"
                                                                                            class="calendar"/>
                            </div>
                        </div>
			           	<div class="control-group">
                            <label class="control-label">订单网址：</label>

                            <div class="controls">
                                <input name="website" type="text" class="input-large">
                            </div>
                        </div>
				       	<div class="control-group">
                            <button type="button" class="button button-primary" id="searchOrder">查询</button>
                        </div>
				    </span>
                </form>
                <br/>
            </div>
            <div class="search-results">
                <div id="grid"></div>
            </div>
        </div>
    </div>

    <!-- 初始隐藏 dialog内容 -->
    <div id="content" class="hide">
        <form class="form-horizontal">
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label">订单号：</label>

                    <div class="controls">
                        <input name="a" type="text" class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">订单类型：</label>

                    <div class="controls">
                        <input name="b" type="text" class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8 ">
                    <label class="control-label">订单网址：</label>

                    <div class="controls">
                        <input name="c" type="text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">订单描述：</label>

                    <div class="controls">
                        <input name="d" type="text" class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">订单时间：</label>

                    <div class="controls">
                        <input name="e" type="text" class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">截止时间：</label>

                    <div class="controls">
                        <input name="f" type="text" class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">订单价格：</label>

                    <div class="controls">
                        <input name="g" type="text" class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span15">
                    <label class="control-label">备注：</label>

                    <div class="controls control-row4">
                        <textarea name="h" class="input-large" type="text"></textarea>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>