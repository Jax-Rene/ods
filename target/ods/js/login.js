/**
 * Created by zhuangjy on 2015/7/12.
 */

$(document).ready(function () {
    if($('#autologin').val() != '' ){
        $('#login').submit();
    }

    //刷新验证码
    $('#validcheck').click(function(){
        $('#validcheck').attr("src","PictureCheckCode?" + new Date());
    });
});