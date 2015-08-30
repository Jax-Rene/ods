$(document).ready(function () {
    /**
     * 定义全局变量用于存储小组成员
     * @type {{}}
     */
    var member = {};
    var memberId = [];


    $('#addmember').click(function () {
        var url = 'inviteMember?memberName=' + $('#membername').val() + '&groupId=' + $('#groupid').val();
        $.get(url, function (data) {
            if (data == 0) {
                alert('已向该用户发送邀请通知,请等待对方回应');
            } else if (data == 1) {
                alert('目标用户已经在小组中,请勿重新邀请!');
            } else
                alert('邀请失败,请检查所输入用户名是否存在!');
        });
    });

    $('#order').submit(function () {
        if (confirm('是否要提交订单,系统将为所有组员发送邮件消息!')) {
            //获取订餐类型
            var values = $("[name='ordertype']");
            for (var i = 0; i < values.length; i++) {
                if (values[i].checked)
                    var orderType = i;
            }
            var url = "createOrder";
            $.post(url, {
                groupId: $('#groupid').val(),
                orderType: orderType,
                orderUrl: $('#orderurl').val(),
                orderMark: $('#ordermark').val(),
                orderEnd: $('#orderend').val()
            }, function (data) {
                if (data == true)
                    alert('成功向所有组员发送邮件!请耐心等待组员选择!');
                else
                    alert('未知错误,请联系管理员!');
            });
        }
        return false;
    });


    //搜索订单
    function searchOrder() {
        BUI.use('bui/form', function (Form) {
            new Form.Form({
                srcNode: '#search-form'
            }).render();
        });
        BUI.use(['bui/grid', 'bui/data'], function (Grid, Data) {
            var Grid = Grid;
            var Store = Data.Store;
            var enumObj = {'0': '早餐', '1': '午餐', '2': '晚餐', '3': '其他'};


            var columns = [{
                title: '订单号',
                dataIndex: 'orderId',
                elCls: 'table-column'
            }, {
                title: '订单类型',
                dataIndex: 'orderType',
                renderer: Grid.Format.enumRenderer(enumObj)
            }, {
                title: '订单网址',
                dataIndex: 'orderUrl',
                width: '20%'
            }, {
                title: '订单描述',
                dataIndex: 'orderMark',
                width: '20%'
            }, {
                title: '订单时间',
                dataIndex: 'orderTime'
            }, {
                title: '订单价格',
                dataIndex: 'orderPrice'
            }, {
                title: '结束时间',
                dataIndex: 'orderEnd'
            }, {
                title: '详情详情',
                dataIndex: 'h',
                renderer: function () {
                    return '<span class="grid-command btn-edit">点击查看</span>';
                }
            }];
            var isAddRemote = false;
            var editing = new Grid.Plugins.DialogEditing({
                contentId: 'content', //设置隐藏的Dialog内容
                triggerCls: 'btn-edit', //触发显示Dialog的样式
                editor: {
                    title: '订单详请',
                    width: 600,
                    listeners: {
                        show: function () {
                            /*
                             if(!isAddRemote){
                             var bField = form.getField('a');
                             bField.set('remote',{
                             url : '../form/data/remote.php',
                             dataType:'json',//默认为字符串
                             callback : function(data){
                             if(data.success)
                             return '';
                             else
                             return data.msg;
                             }
                             });
                             isAddRemote = true;
                             }*/
                            //TO DO
                        }
                    }
                }
            });

            var store = new Store({
                    url: 'getOrder', //ajax url
                    pageSize: 5, //每页行数
                    autoLoad: true,
                    proxy: {
                        ajaxOptions: { //ajax的配置项，不要覆盖success,和error方法
                            traditional: true,
                            type: 'post'
                        }
                    },
                    params: {
                        groupId: $('#groupid').val(),
                        startTime: $('#startTime').val(),
                        endTime: $('#endTime').val(),
                        url: $('#url').val()
                    },
                    root: 'records',
                    totalProperty: 'totalCount'
                }),
                grid = new Grid.Grid({
                    render: '#grid',
                    columns: columns,
                    loadMask: true,
                    store: store,
                    bbar: {
                        pagingBar: true
                    },
                    plugins: [editing]

                });
            grid.render();
        });
    }

    $('#searchOrder').click(function () {
        $('#grid').empty();
        searchOrder();
    });


    /**
     * 今日订单
     */
    function showCurrentOrder() {
        BUI.use(['bui/grid', 'bui/data'], function (Grid, Data) {

            var Grid = Grid;
            var Store = Data.Store
            var columns = [
                {
                    title: '昵称',
                    dataIndex: 'userId',
                    editor: {xtype: 'select', items: member},
                    renderer: Grid.Format.enumRenderer(member)
                },
                {title: '订单名', dataIndex: 'orderName', editor: {xtype: 'text', rules: {required: true}}},
                {
                    title: '数量',
                    dataIndex: 'orderNumber',
                    editor: {xtype: 'number', rules: {required: true}},
                    summary: true
                },
                {
                    title: '价格',
                    dataIndex: 'orderPrice',
                    editor: {xtype: 'number', rules: {required: true}},
                    summary: true
                }
            ];

            var editing = new Grid.Plugins.RowEditing();
            debugger;
            var store = new Store({
                url: 'currentOrder',
                autoLoad: true,
                pageSize: 3,
                proxy: {
                    ajaxOptions: { //ajax的配置项，不要覆盖success,和error方法
                        traditional: true,
                        type: 'get'
                    }
                },
                params: {
                    groupId: $('#groupid').val()
                },
                root: 'records',
                totalProperty: 'totalCount'
            });

            var grid = new Grid.Grid({
                render: '#today-order',
                columns: columns,
                forceFit: true,
                tbar: {
                    items: [{
                        btnCls: 'button',
                        text: '<i class="icon-plus"></i>添加',
                        listeners: {
                            'click': addFunction
                        }
                    },
                        {
                            btnCls: 'button',
                            text: '<i class="icon-remove"></i>删除',
                            listeners: {
                                'click': delFunction
                            }
                        }]
                },
                plugins: [editing, Grid.Plugins.CheckSelection, Grid.Plugins.Summary],
                store: store
            });


            grid.render();

            function addFunction() {
                var newData = {};
                store.addAt(newData, 0);
                editing.edit(newData, 'userId'); //添加记录后，直接编辑
            }

            //删除选中的记录
            function delFunction() {
                var selections = grid.getSelection();
                if (confirm('确认要删除这' + selections.length + '条记录吗?')) {
                    for (var i = 0; i < selections.length; i++) {
                        $.post('deletePersonOrder', {
                            id: selections[i].id,
                            targetUserId: selections[i].userId,
                            orderId: selections[i].orderId
                        }, function (data) {
                            if (data == false) {
                                alert('您没有权限执行该删除操作!');
                            } else {
                                store.remove(selections);
                            }
                        });
                    }
                }
            }
        });
    }

    //获取组员信息用于设置今日订单
    $.get('getMemberIdAndName?groupId=' + $('#groupid').val(), function (data) {
        $.each(data, function (key, value) {
            member[key] = value;
        });
        showCurrentOrder();
    });

    //统计结果
    $('#getDetail').click(function () {
        $.get('currentOrderCount?groupId=' + $('#groupid').val(), function (data) {
            $('#total-count').empty();
            $.each(data, function (key, value) {
                debugger;
                var text = $('#total-count').html();
                $('#total-count').html(text + "<p>" + key + "* " + value + "</p>");
            });
            $('#total-count').slideToggle();
        });
    });


    //加入小组
    $('#addGroup').click(function () {
        var name = null;
        while (name == undefined || name == null || name == '') {
            name = prompt('请输入您要在该组中显示的昵称', '');
        }
        var url = 'addGroup?groupId=' + $('#groupid').val() + '&nickName=' + name;
        $.get(url, function (data) {
            if (data == true) {
                alert('已提交加入小组的申请,请等待小组组长回应!');
            } else {
                alert('加入小组失败,可能是您输入的昵称已经存在!');
            }
        });
    });

    //离开小组
    $('#exitGroup').click(function () {
        if (confirm('您确定要离开小组吗?')) {
            $.post('exitGroup', {
                groupId: $('#groupid').val()
            }, function () {
                alert('退出小组,成功请刷新页面!');
            });
        }
    });

    //解散小组
    $('#deleteGroup').click(function () {
        if (confirm('解散小组将不能恢复您确定要解散小组吗?')) {
            $.post('deleteGroup', {
                groupId: $('#groupid').val()
            }, function () {
                alert('解散小组成功,即将为您跳转到首页...');
                window.location.href = "/gotoIndex";
            });
        }
    });

    //邀请成员
    $('#inviteMember').click(function () {
        var userName = prompt('请输入您要邀请人的用户名');
        if (userName == '')
            alert('邀请人不能为空');
        else
            $.post('inviteMember', {
                memberName: userName,
                groupId: $('#groupid').val()
            }, function (data) {
                if (data == 2) {
                    alert('不存在该用户请检查输入是否正确!');
                } else if (data == 1) {
                    alert('该成员已经在小组中请勿重新邀请!');
                } else {
                    alert('邀请成功,请等待对方操作!');
                }
            });
    });
});
