<html>
<head>
    <title>登陆页面</title>
    <script src="${absoluteContextPath}/js/cookie.js"></script>

</head>
<body>
<form action="inputLogin"  method="post" id="form" onsubmit="return checkLogin()">
    name: <input type="email" id="username" name="userName" required="true"/>
    password:<input type="password" id="password" name="passWord" required="true"/>
    <input type="checkbox"  id="autologin"/>自动登录
    <a href="#">忘记密码</a>
    <input type="submit"/>
    <input type="button" value="注册" onclick="gotoRegister()"/>
    <#-- 如果账号密码错误输出如下 -->
    ${passError!""}
    <script>loginAuto();</script>
</form>
</body>
</html>
