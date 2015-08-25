$(document).ready(function () {
    var url = $('#orderUrl').val();
    $('#orderContent').attr('src', url);


    $('#submitOrder').submit(function () {
        var name = $('#orderName').val();
        var num = $('#orderNumber').val();
        var price = $('#singlePrice').val();
        var total = num * price;
        if (confirm("你当前所选套餐为:" + name + ",一共订了" + num + "份,总价" + total + "元")) {
            //设置总价值
            $('#orderPrice').val(total);
            var url = 'submitOrder?orderId=' + $('#orderId').val() + "&orderName=" + name
                + "&orderPrice=" + total + "&orderNumber=" + num;
            $.get(url, function (data) {
                if (data == true) {
                    alert('提交成功!');
                } else
                    alert('提交失败,请联系管理员!');
            });
        }
        return false;
    });
});