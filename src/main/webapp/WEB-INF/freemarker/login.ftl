<html>
<head>
    <title>欢迎光临ods系统</title>
    <script src="${absoluteContextPath}/js/jquery-2.1.4.min.js"></script>
    <script src="${absoluteContextPath}/js/login.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/dpl.css">
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/bui.css">
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/ods.css">
    <script src="http://g.alicdn.com/bui/seajs/2.3.0/sea.js"></script>
    <script src="http://g.alicdn.com/bui/bui/1.1.21/config.js"></script>
</head>
<body>
<div class="wrapper">
    <div id="index-banner">
        <div class="container">
            <div class="span6 offset16">
                <form id="register" action="inputRegister" method="post">
                    <div class="form-header">
                        <div class="left goto-register">注册</div>
                        <div class="right goto-login">登录<span class="x-icon xicon-info icon-circle-arrow-right"></span>
                        </div>
                    </div>
                    <div class="register-form">
                        <input type="text" id="username" name="userName" data-rules="{email:true}" placeholder="邮箱账号"
                               required="true"/>
                    </div>
                    <div class="register-form">
                        <input type="password" id="password" name="passWord" placeholder="密码6位-14位" required="true" data-rules="{minlength:5}"/>
                    </div>
                    <div class="register-form">
                        <input type="password" id="password2" name="passWord2" placeholder="重新输入密码" required="true"
                               data-rules="{minlength:5}"/>
                    </div>
                    <button type="submit" class="button button-primary button-large" style="width: 207px">注册</button>
                </form>
                <form id="login" action="/inputLogin" method="post">
                    <div class="form-header">
                        <div class="left goto-login">登录</div>
                        <div class="right goto-register">注册<span
                                class="x-icon xicon-info icon-circle-arrow-right"></span></div>
                    </div>
                    <div class="register-form">
                        <input type="text" id="username" name="userName" data-rules="{email:true}" required="true"
                               placeholder="邮箱帐号"
                               value="${userName!""}"/>
                    </div>
                    <div class="register-form">
                        <input type="password" id="password" name="passWord" required="true" placeholder="输入密码"
                               data-rules="{minlength:5}"
                               value="${passWord!""}"/>
                    </div>
                    <div class="vaild-box">
                        <input type="text" name="checkCode" id="checkCode" placeholder="输入验证码"
                               style="height: 25px;width: 50%">
                        <a href="javascript:void(0)"><img src="PictureCheckCode" id="validcheck"></a>
                    </div>
                    <div class="rem-passwd">
                        <label class="checkbox">
                            <input type="checkbox" id="autologin" name="autoLogin" value="${curUser!""}">自动登录
                        </label>
                        <a href="javascript:void(0)" id="forgetPass" class="right">忘记密码？</a>
                    </div>
                    <br/>
                    <button type="submit" class="button button-primary button-large" style="width: 207px">登录</button>
                </form>
                <span id="error" style="color: red">${error!""}</span>
            </div>
        </div>
    </div>
</div>

<script>
    $('#register .goto-login').on('click', function () {
        $('#login').css('display', 'block');
        $('#register').css('display', 'none');
    });
    $('#login .goto-register').on('click', function () {
        $('#login').css('display', 'none');
        $('#register').css('display', 'block');
    });


    //忘记密码找回窗口
    BUI.use('bui/overlay', function (Overlay) {
        var dialog = new Overlay.Dialog({
            title: '找回密码',
            width: 250,
            height: 120,
            bodyContent: '<div> <input type="text" id="eamilAddress" style="width: 100%" placeholder="请输入邮箱"/></div>',
            success: function () {
                if ($('#eamilAddress').val() == '')
                    alert('输入不能为空!');
                else {
                    $.post('findPassword', {
                        userName: $('#eamilAddress').val()
                    }, function (data) {
                        if (data == true) {
                            alert('请登陆邮箱,点击链接找回密码!');
                        } else {
                            alert('输入邮箱地址错误!');
                        }
                    });
                }
                if ($('#eamilAddress').val() != '')
                    this.close();
            }
        });
        $('#forgetPass').on('click', function () {
            dialog.show();
        });
    });

    <!-- script start -->
    BUI.use('bui/form', function (Form) {

        new Form.Form({
            srcNode: '#register'
        }).render();

    });

    <!-- script start -->
    BUI.use('bui/form', function (Form) {

        new Form.Form({
            srcNode: '#login'
        }).render();

    });

</script>
</body>
</html>