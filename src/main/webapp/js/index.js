$(document).ready(function () {

//加载所有组信息
url = '/getGroupInformation';
$.get(url, function (data, status) {
    if (status == 'success' && data != "") {
        var obj = eval(data); //解析json
        allGroup = obj.allGroup;
        allBossName = obj.allBossName;
        setPictureWall();
        console.log(obj.lastOrder);
        console.log(obj.myGroup);

        lastOrder = obj.lastOrder;
        myGroup = obj.myGroup;


    }
});


});
