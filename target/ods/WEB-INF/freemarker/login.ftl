<html>
<head>
    <title>登陆页面</title>
    <script src="${absoluteContextPath}/js/jquery-2.1.4.min.js"></script>
    <script src="${absoluteContextPath}/js/jquery.cookie.js"></script>
    <script src="${absoluteContextPath}/js/login.js" type="text/javascript"></script>
</head>
<body>
<form id="loginForm" action="/inputLogin"  method="post" id="form">
    用户名: <input type="email" id="username" name="userName" required="true"/>
    密码:<input type="password" id="password" name="passWord" required="true"/>
    <input type="checkbox"  id="autologin"/>30天内自动登录
    验证码:<input type="text" name="checkCode" id="checkCode">
    <a href="javascript:void(0)"><img src="PictureCheckCode" id="validcheck"></a>
<#-- 如果账号密码错误输出如下 -->
    <a href="/forgetPassword">忘记密码</a>
    <input type="submit" value="登录"/>
    <input type="button" value="注册" id="register"/>
    ${passError!""}
</form>
</body>
</html>



<#--<!DOCTYPE html>-->
<#--<html>-->
<#--<head>-->
    <#--<meta charset="utf-8">-->
    <#--<title>test</title>-->
    <#--<link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/dpl.css">-->
    <#--<link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/bui.css">-->
    <#--<link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/ods.css">-->
<#--</head>-->
<#--<body>-->
<#--<div class="wrapper">-->
    <#--<div id="index-banner">-->
        <#--<div class="container">-->
            <#--<div class="span6 offset16">-->
                <#--<form id="register">-->
                    <#--<div class="form-header">-->
                        <#--<div class="left goto-register">注册</div>-->
                        <#--<div class="right goto-login">登录<span class="x-icon xicon-info icon-circle-arrow-right"></span></div>-->
                    <#--</div>-->
                    <#--<div class="register-form">-->
                        <#--<input name="email" type="text" placeholder="邮箱">-->
                    <#--</div>-->
                    <#--<div class="register-form">-->
                        <#--<input name="password" type="password" placeholder="请输入密码">-->
                    <#--</div>-->
                    <#--<div class="register-form">-->
                        <#--<input name="confirm_password" type="password"  placeholder="请确认密码">-->
                    <#--</div>-->
                    <#--<button type="submit" class="button button-primary button-large">注册</button>-->
                <#--</form>-->
                <#--<form id="login">-->
                    <#--<div class="form-header">-->
                        <#--<div class="left goto-login">登录</div>-->
                        <#--<div class="right goto-register">注册<span class="x-icon xicon-info icon-circle-arrow-right"></span></div>-->
                    <#--</div>-->
                    <#--<div class="register-form">-->
                        <#--<input name="email" type="text" placeholder="邮箱">-->
                    <#--</div>-->
                    <#--<div class="register-form">-->
                        <#--<input name="password" type="password" placeholder="请输入密码">-->
                    <#--</div>-->
                    <#--<div class="rem-passwd">-->
                        <#--<label class="checkbox">-->
                            <#--<input type="checkbox">记住密码-->
                        <#--</label>-->
                        <#--<a href="#" class="right">忘记密码？</a>-->
                    <#--</div>-->
                    <#--<br/>-->
                    <#--<button type="submit" class="button button-primary button-large">登录</button>-->
                <#--</form>-->
            <#--</div>-->
        <#--</div>-->
    <#--</div>-->
<#--</div>-->

<#--<script src="./src/js/jquery-2.1.4.min.js"></script>-->
<#--<script src="./src/js/bui.js"></script>-->
<#--<script>-->
    <#--$('#register .goto-login').on('click', function () {-->
        <#--$('#login').css('display','block');-->
        <#--$('#register').css('display','none');-->
    <#--});-->
    <#--$('#login .goto-register').on('click', function () {-->
        <#--$('#login').css('display','none');-->
        <#--$('#register').css('display','block');-->
    <#--});-->
<#--</script>-->


<#--</body>-->
<#--</html>-->