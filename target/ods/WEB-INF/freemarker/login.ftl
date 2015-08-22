<#--<html>-->
<#--<head>-->
    <#--<title>登陆页面</title>-->
    <#--<script src="${absoluteContextPath}/js/jquery-2.1.4.min.js"></script>-->
    <#--<script src="${absoluteContextPath}/js/jquery.cookie.js"></script>-->
    <#--<script src="${absoluteContextPath}/js/login.js" type="text/javascript"></script>-->
<#--</head>-->
<#--<body>-->
<#--<form id="loginForm" action="/inputLogin"  method="post" id="form">-->
    <#--密码:<input type="password" id="password" name="passWord" required="true"/>-->
    <#--<input type="checkbox"  id="autologin"/>30天内自动登录-->
    <#--验证码:<input type="text" name="checkCode" id="checkCode">-->
    <#--<a href="javascript:void(0)"><img src="PictureCheckCode" id="validcheck"></a>-->
<#--&lt;#&ndash; 如果账号密码错误输出如下 &ndash;&gt;-->
    <#--<a href="/forgetPassword">忘记密码</a>-->
    <#--<input type="submit" value="登录"/>-->
    <#--<input type="button" value="注册" id="register"/>-->
    <#--${passError!""}-->
<#--</form>-->
<#--</body>-->
<#--</html>-->



<html>
<head>
    <title>欢迎光临ods系统</title>
    <script src="${absoluteContextPath}/js/jquery-2.1.4.min.js"></script>
    <script src="${absoluteContextPath}/js/jquery.cookie.js"></script>
    <script src="${absoluteContextPath}/js/login.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/dpl.css">
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/bui.css">
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/ods.css">
</head>
<body>
<div class="wrapper">
    <div id="index-banner">
        <div class="container">
            <div class="span6 offset16">
                <form id="register" action="inputRegister" method="post">
                    <div class="form-header">
                        <div class="left goto-register">注册</div>
                        <div class="right goto-login">登录<span class="x-icon xicon-info icon-circle-arrow-right"></span></div>
                    </div>
                    <div class="register-form">
                        <input type="email" id="username" name="userName" placeholder="输入邮箱账号" required="true"/>
                    </div>
                    <div class="register-form">
                        <input type="password" id="password" name="passWord" placeholder="密码大于6位小于14位" required="true"/>
                    </div>
                    <div class="register-form">
                        <input type="password" id="password2"  placeholder="重新输入密码" required="true"/>
                    </div>
                    <button type="submit" class="button button-primary button-large">注册</button>
                </form>
                <form id="login"  action="/inputLogin"  method="post">
                    <div class="form-header">
                        <div class="left goto-login">登录</div>
                        <div class="right goto-register">注册<span class="x-icon xicon-info icon-circle-arrow-right"></span></div>
                    </div>
                    <div class="register-form">
                        <input type="email" id="username" name="userName" required="true" placeholder="邮箱帐号" value="${userName!""}"/>
                    </div>
                    <div class="register-form">
                        <input type="password" id="password" name="passWord" required="true" placeholder="输入密码" value="${passWord!""}"/>
                    </div>
                    <div class="vaild-box">
                        <input type="text" name="checkCode" id="checkCode" placeholder="输入验证码">
                        <a href="javascript:void(0)"><img src="PictureCheckCode" id="validcheck"></a>
                    </div>
                    <div class="rem-passwd">
                        <label class="checkbox">
                            <input type="checkbox" id="autologin" name="autoLogin" value="${curUser!""}">自动登录
                        </label>
                        <a href="/forgetPassword" class="right">忘记密码？</a>
                    </div>
                    <br/>
                    <button type="submit" class="button button-primary button-large">登录</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    $('#register .goto-login').on('click', function () {
        $('#login').css('display','block');
        $('#register').css('display','none');
    });
    $('#login .goto-register').on('click', function () {
        $('#login').css('display','none');
        $('#register').css('display','block');
    });
</script>


</body>
</html>