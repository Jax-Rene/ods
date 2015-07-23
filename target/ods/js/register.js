$(document).ready(function () {
    $('#reset').click(function () {//重置注册表单
        $('#username').val('');
        $('#password').val('');
        $('#password2').val('');
        $('#s_province').val('');
        $('#s_city').val('');
        $('#s_country').val('');
    });

    $('#registerForm').submit(function () { //注册前的判断
        var password = $('#password').val();
        var password2 = $('#password2').val();
        if(password != password2) {
            alert('密码和确认密码不一致请重新输入!');
            return false;
        }else if(password.length < 6 || password.length > 14){
            alert('密码输入不符合规请重新输入!');
            return false;
        }
        $('#location').val($('#s_province').val() + '-' + $('#s_city').val() + '-' + $('#s_country').val());
        return true;
    });
});

