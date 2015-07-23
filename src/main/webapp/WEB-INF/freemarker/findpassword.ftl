<html>
<head>
    <title>找回密码</title>
    <script>
//        $(document).ready(function () {
//            $('#forgetpassword').submit(function () {
//                var url = $('#forgetpassword').attr('action') + "?userName=" + $('#username').val();
//                $('#forgetpassword').attr('action',url);
//                return true;
//            });
//        });
        $(document).ready(function () {
            $('#submit').click(function () {
                var url = $('#submit').attr('href');
                $('#submit').attr('href',url + $('#username').val());
            })
        })
    </script>
</head>
<body>
<form action="forgetPassword" method="get" id="forgetpassword">
用户名:<input type="text" id="username"/>
    <a href="/findPassword?userName=" id="submit">提交</a>
</form>
${error!""}
</body>
</html>