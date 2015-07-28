<html>
<head>
    <title>登陆页面</title>
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

    <a href="/j_spring_security_logout"
</form>
</body>
</html>
