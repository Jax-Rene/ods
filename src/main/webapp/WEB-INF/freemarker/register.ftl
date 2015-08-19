<html>
    <head>
        <title>注册账号</title>
        <script src="${absoluteContextPath}/js/register.js"></script>
    </head>
    <body>
    <form id="registerForm" action="inputRegister" method="post">
    邮箱账号:<input type="email" id="username" name="userName" placeholder="输入邮箱账号" required="true"/>
    密码:<input type="password" id="password" name="passWord" placeholder="密码大于6位小于14位" required="true"/>
    确认密码:<input type="password" id="password2"  placeholder="重新输入密码" required="true"/>
    所在地:	<select id="s_province" name="s_province"></select>  
    <select id="s_city" name="s_city"></select>  
    <select id="s_country" name="s_country"></select>
    <!-- 要在定义了select后在引入否则会找不到对象id -->
    <script src="${absoluteContextPath}/js/region.js" type="text/javascript"></script>
    <script type="text/javascript">_init_area();</script>

    <#-- 设置隐藏属性location 方便SPRING 自动注入 -->
    <input type="hidden" id="location" name="location" />
    <input type="submit" value="提交"/>
    <input type="button" value="重填" id="reset"/>

        <!-- 输出后来校验信息 -->
        ${error!""}
    </form>
    </body>
</html>