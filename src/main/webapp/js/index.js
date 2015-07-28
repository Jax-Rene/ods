$(document).ready(function() {
    //加载所有组信息
    var url = '/getAllGroup';
    $.get(url, function(data,status) {
        if(status == 'success' && data!="") {
            var table = $("<table border=\"0\">");
            table.appendTo($("#group"));
            var obj = eval(data); //解析json
            $(obj).each(function (index) {
                var tr = $("<tr></tr>");
                tr.appendTo(table);
                var val = obj[index];
                //加入信息内容
                var td = $("<td><a href='/getGroupInfo?groupId=" + val.id + "'>" + val.groupName + "</a></td>");
                td.appendTo(tr);
            });
        }else
            $('#mygroup').text('暂时没有任何小组,快加入/创建小组吧!');
    });

    ////加载创建的组信息
    //url = '/getMyGroup';
    //$.get(url, function(data,status) {
    //    if(status == 'success' && data!="") {
    //        alert(data);
    //    }
    //});

    //搜索小组
    $('#search').click(function () {
        var name = null;
        while(name == undefined || name == null || name ==''){
            name = prompt('请输入您要在该组中显示的昵称','');
        }
        var url = 'searchGroup?groupName=' + $('#searchGroup').val() +'&nickName=' + name;
        $.get(url, function (data) {
            alert(data);
            if(data==0){
                alert('您已经在该组中,请勿重复操作!');
            }else if(data==1)
                alert('没有找到小组,请重新输入!');
            else
                alert('已提交加入小组的申请,请等待小组组长回应!');
        });
    });

});
