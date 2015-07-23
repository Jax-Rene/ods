<html>
<head>
    <title>找回密码</title>
    <script>
        $(document).ready(function () {
            $('#resetPass').submit(){
                if($('#password').val() != $('#password2').val()){
                    alert('两次密码输入不一致,请重新输入!');
                    return false;
                }else if($('#password').length < 6 || $('#password').length > 14){
                    alert('密码长度不符合规则,请重新输入');
                    return false;
                }else
                return true;
            }
        })
    </script>
</head>
<body>
    <form action="/resetPassWord?userName=${username}" method="post" id="resetPass">
        密码:<input type="password" placeholder="密码长度在6-14位间" id="password" name="passWord"/>
        重复密码:<input type="password" placeholder="请重新输入密码" id="password2"/>
        <input type="submit" value="提交"/>
    </form>
</body>
</html>