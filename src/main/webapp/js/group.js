$(document).ready(function () {
    $('#changename').click(function () {
        var url = 'changeNickName?newName=' + $('#newname').val() + '&groupId=' + $('#groupid').val();
        $.get(url, function (data) {
            if (data == true) {
                alert('恭喜您,修改昵称成功!');
            } else
                alert('修改失败!小组中存在重复的昵称');
        });
    });

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
            alert($('#orderend').val());
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
            var enumObj = {'0': '早餐', '1': '午餐', '2': '晚餐','3' : '其他'};

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
                    }
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
            var Store = Data.Store;
            var columns = [
                {title: '姓名', dataIndex: 'nickName', editor: {xtype: 'text', rules: {required: true}}},
                {title: '订单名', dataIndex: 'orderName', editor: {xtype: 'text', rules: {required: true}}},
                {title: '价格', dataIndex: 'orderPrice', editor: {xtype: 'number', rules: {required: true}}},
                {
                    title: '数量',
                    dataIndex: 'orderNumber',
                    editor: {xtype: 'number', rules: {required: true}},
                    summary: true
                }
            ];

            var editing = new Grid.Plugins.RowEditing();
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
                var newData = {b: 0};
                store.addAt(newData, 0);
                editing.edit(newData, 'a'); //添加记录后，直接编辑
            }

            //删除选中的记录
            function delFunction() {
                var selections = grid.getSelection();
                store.remove(selections);
            }

            //$.get('currentOrderCount?groupId=' + $('#groupid').val() , function(data){
            //    $.each(data,function(key,value){
            //        alert(key + ': ' + value));
            //    });
            //});

            $.get('currentOrderCount?groupId=' + $('#groupid').val() , function(data){
                $.each(data, function (key,value) {
                    if(key === 'price')
                        alert(value);
                    else
                    alert(key + ' : ' + value);
                });
            });
        });
    }

    showCurrentOrder();
});