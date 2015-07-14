/**
 * Created by zhuangjy on 2015/7/12.
 */
document.write("<script language=javascript src='/js/cookie.js'></script>");
function gotoRegister(){
    window.location.href='/gotoRegister';
}

function checkLogin(){ //登录前的检查
    if(document.getElementById('autologin').checked){
        var username = document.getElementById('username').value;
        var password = document.getElementById('password').value;
        setCookie("username",username,30);
        setCookie("password",password,30);
    }
    return true;
}


function loginAuto(){
    var password = getCookie("password");
    var username = getCookie("username");
    document.getElementById('username').value = username;
    document.getElementById('password').value = password;
    if(username!="" && password!="")
    document.getElementById('form').submit();
}



function resetRegister(){
    document.getElementById('username').value="";
    document.getElementById('password').value="";
    document.getElementById('password2').value="";
    document.getElementById('s_province').selectedIndex = 0;
    document.getElementById('s_city').selectedIndex = 0;
    document.getElementById('s_country').selectedIndex = 0;
}


function checkRegister(){
    //判断两次密码是否一样
    var pass1 = document.getElementById('password').value;
    var pass2 = document.getElementById('password2').value;
    if(pass1!=pass2){
        alert('密码和确认密码不一致请重新输入!');
        return false;
    }else if(pass1.length < 6 || pass1.length > 14){
        alert('密码输入不符合规请重新输入!');
        return false;
    }else
    {
        if(document.getElementById('s_province').selectedIndex == 0 ||
            document.getElementById('s_city').selectedIndex == 0 ||
            document.getElementById('s_country').selectedIndex == 0){
            alert('请输入完整的地址!');
            return false;
        }
    }

    //获取地址值
    document.getElementById('location').value = getRegion();
    return true;
}



//获取地址值，向后台传值 format:provious-city-country
function getRegion(){
    return  document.getElementById('s_province').value + "-" +
    document.getElementById('s_city').value + "-" +
    document.getElementById('s_country').value;
}


