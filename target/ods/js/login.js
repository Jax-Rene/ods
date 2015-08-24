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

    $('#register').on('submit',function(){
       if($('#password').val() != $('#password2').val()){
            $('#error').text('两次输入密码不一致!');
           return false;
       }
        return true;
    });
});