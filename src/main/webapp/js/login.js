/**
 * Created by zhuangjy on 2015/7/12.
 */

$(document).ready(function () {

    //页面加载完判断是否有cookie，有的话自动登录
    if($.cookie('username')!= undefined && $.cookie('password')!=undefined){
        $('#username').val($.cookie('username'));
        $('#password').val($.cookie('password'));
        $('form').submit();
    }

    $('#register').click(function () { //跳转注册页面
        location.href = '/gotoRegister';
    });

    $('#loginForm').submit(function (){ //检查是否要自动登录
        if($('#autologin').attr('checked')=='checked'){
            $.cookie('username',$('#username').val(),{expires:30,path:'/'});
            $.cookie('password',$('#password').val(),{expires:30,path:'/'});
        }
        return true;
    });

    //刷新验证码
    $('#validcheck').click(function(){
        $('#validcheck').attr("src","PictureCheckCode?" + new Date());
    });
});