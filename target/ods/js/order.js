$(document).ready(function () {
    var url = $('#orderUrl').val();
    $('#orderContent').attr('src',url);


    $('#submitOrder').submit(function(){
        var name = $('#orderName').val();
        var num = $('#orderNum').val();
        var price = $('#singlePrice').val();
        var total = num * price;
        if(confirm("你当前所选套餐为:" + name +",一共订了"+num+"份,总价"+total+"元" )){
            //设置总价值
            $('#orderPrice').val(total);
            return true;
        }else
        return false;
    });
});