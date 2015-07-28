<html>
<head>
    <title>ods首页</title>
    <script src="${absoluteContextPath}/js/index.js" type="text/javascript"></script>
</head>
<body>
<div>
    你好! ${curUser.userName!""}
</div>


<#-- 这里显示小组信息 分为我创建的组 以及 我所归属的组(包含自己创建的)-->
<div id="group">
    <a href="#">创建小组</a>
    <!-- 点击后出现下面内容的窗口 -->
    <form action="/createGroup" method="POST"
          enctype="multipart/form-data" >
        请输入组名：<input type="text" name="newGroupName" /><br/>
        您可以选择是否上传小组头像:<input type="file" name="newGroupIcon"/><br/>
        <input type="submit" value="确认"/>${newGroupError !""}
    </form>

    加入小组:<input type="text" id="searchGroup"/><input type="button" id="search" value="搜索小组"/>

    <!-- 创建小组失败提示 -->
    ${newGroupError!""}
    <span id="mygroup"></span>
</div>




</body>
</html>
