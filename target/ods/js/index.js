//加载所有组信息
url = '/getGroupInformation';
$.get(url, function (data, status) {
    if (status == 'success' && data != "") {
        var obj = eval(data); //解析json
        allGroup = obj.allGroup;
        allBossName = obj.allBossName;
        setPictureWall();
    }
});


//搜索小组
$('#search').click(function () {
    var name = null;
    while (name == undefined || name == null || name == '') {
        name = prompt('请输入您要在该组中显示的昵称', '');
    }
    var url = 'searchGroup?groupName=' + $('#searchGroup').val() + '&nickName=' + name;
    $.get(url, function (data) {
        alert(data);
        if (data == 0) {
            alert('您已经在该组中,请勿重复操作!');
        } else if (data == 1)
            alert('没有找到小组,请重新输入!');
        else
            alert('已提交加入小组的申请,请等待小组组长回应!');
    });
});
