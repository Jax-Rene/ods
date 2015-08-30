$(document).ready(function () {

    //加载所有组信息
    url = '/getGroupInformation';
    $.get(url, function (data, status) {
        if (status == 'success' && data != "") {
            var obj = eval(data);

            allGroup = obj.allGroup;
            allBossName = obj.allBossName;
            setPictureWall();
            lastOrder = obj.lastOrder;
            console.log(obj.lastOrder);
            myGroup  = obj.myGroup;

        }
    });
    var table = $('<table></table>'),
         tr = $('<tr></tr>'),
         //tdNikeName = $('<td></td>'),
         tdOrderName = $('<td></td>'),
         tdOrderNumber = $('<td></td>'),
         tdOrderPrice = $('<td></td>');
    //for(var i = 0; i < lastOrder.length; i ++) {
        var nikeName = lastOrder.nickName,
            orderNumber = lastOrder.orderNumber,
            orderName = lastOrder.orderName,
            orderPrice = lastOrder.orderPrice;
       // tdNikeName.append(nikeName);
        tdOrderNumber.append(orderNumber);
        tdOrderName.append(orderName);
        tdOrderPrice.append(orderPrice);
        tr.append(tdOrderName).append(tdOrderNumber).append(tdOrderPrice);
        table.appendTo(tr);
        $('.order-content').append(table);
    //}

});
