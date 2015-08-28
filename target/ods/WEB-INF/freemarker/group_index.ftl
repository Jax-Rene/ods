<html>
<head>
    <script src="${absoluteContextPath}/js/group.js" type="text/javascript"></script>
    <script src="${absoluteContextPath}/js/list_member.js"></script>
</head>
<body>

<div style="text-align: center;margin-top: 50px;">
    <h1 style="font-size: 50px">${group.groupName}</h1>

<#if boss?exists || member?exists>
    您好,<span style="color: #A57F59;" id="nickName"> ${nickName}</span>(<a href="javascript:void(0)"
                                                                          id="changeName">更改昵称</a>)
    <br/>
<form id="group-operator">
    <#if boss?exists><input type="button" id="deleteGroup" class="button button-danger button-middle" value="解散该小组"/>
    <#else><input type="button" id="exitGroup" class="button button-warning button-middle" value="离开该小组"/>
    </#if>
<#else><input type="button" id="addGroup" class="button button-success button-middle" value="加入该小组"/>
</#if>
</form>
</div>

<div class="group-avater-wrapper">
    <div class="group-avater">
        <img src="${absoluteContextPath}/img/icon/${group.groupIcon}"
             style="width:200px;height:200px;border-radius:125px;"/>
    </div>
    <div id="members" style="font-family: '微软雅黑', 'Yuppy TC Regular', '幼圆', '黑体';width 50%">
        <div class="group-header">小组成员</div>
    <#list members?keys as memId>
        <a href="${memId}">${members[memId]}</a>
    </#list>
    </div>
</div>

<form>

    <div class="search-results">
        <div id="memberGrid"></div>
    </div>

    <input type="hidden" id="groupid" value="${group.id}"/>
</form>

<#if boss?exists>
<input type="hidden" id="boss" value="true"/>
<hr/>
<div class="new-order container">
    <h1>发布新的订单</h1>

    <form id="order">
        <div>
            <span>
               订餐网站URL:<input type="text" id="orderurl" name="orderUrl" class="input-normal"/>
            </span>
            <span class="new-order-remark">
               订餐消息备注:<input type="text" id="ordermark" name="orderMark" class="input-normal"/>
            </span>
        </div>
        <div>
            订餐类型:<input type="radio" name="ordertype" checked="true" class="radio"/>早餐
            <input type="radio" name="ordertype" class="radio"/>午餐
            <input type="radio" name="ordertype" class="radio"/>晚餐
            <input type="radio" name="ordertype" class="radio"/>其他
            <span class="new-order-time">
                下单时间:<input type="text" id="orderend" class="calendar calendar-time"/>
                <input type="submit" value="提交" class="button button-primary"/>
            </span>
        </div>

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
<script>
    //更改昵称窗口
    BUI.use('bui/overlay', function (Overlay) {
        var dialog = new Overlay.Dialog({
            title: '更改昵称',
            width: 250,
            height: 120,
            bodyContent: '<input type="text" id="newname" class="input-normal"  style="width: 190px" placeholder="输入新的昵称"/>',
            success: function () {
                if ($('#newname').val() == '')
                    alert('输入不能为空!');
                else {
                    var url = 'changeNickName?newName=' + $('#newname').val() + '&groupId=' + $('#groupid').val();
                    $.get(url, function (data) {
                        if (data == true) {
                            $('#nickName').text($('#newname').val());
                            alert('修改昵称成功!');
                        } else {
                            alert('修改失败!');
                        }
                    });
                }
                if ($('#newname').val() != '')
                    this.close();
            }
        });
        $('#changeName').on('click', function () {
            dialog.show();
        });
    });


    BUI.use('bui/calendar', function (Calendar) {
        var datepicker = new Calendar.DatePicker({
            trigger: '.calendar',
            minDate: '2015-06-01',
            maxDate: '2017-12-25',
            autoRender: true
        });
    });


    BUI.use('bui/calendar', function (Calendar) {
        var datepicker = new Calendar.DatePicker({
            trigger: '.calendar-time',
            showTime: true,
            autoRender: true
        });
    });

</script>
</body>
</html>