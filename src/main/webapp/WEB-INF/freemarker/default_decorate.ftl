<!-- 这个装饰页面包含用户模块 - 登陆、注册 -->
<html>
<head>
    <title>${title}</title>
    <script src="${absoluteContextPath}/js/jquery-2.1.4.min.js"></script>
    <script src="http://code.jquery.com/jquery-migrate-1.2.1.js"></script>
    <script src="${absoluteContextPath}/js/bui.js"></script>
    <script src="${absoluteContextPath}/js/message.js"></script>
    <script src="http://g.alicdn.com/bui/seajs/2.3.0/sea.js"></script>
    <#--<script src="http://g.alicdn.com/bui/bui/1.1.21/config.js"></script>-->

    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/ods.css">
    <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
    <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">

${head}
</head>
<body>
<#include "head.ftl"/>
${body}
<#include "foot.ftl"/>
<script>
    //搜索小组
//    $('#search').click(function () {
//        $.get('searchGroup?groupName=' + $('#searchGroup').val(), function (data) {
//            if(data!=-1)
//                window.location.href = '/getGroupInfo?groupId=' + data;
//            else
//                alert('没有找到该组,请检查输入是否正确!');
//        });
//    });
    data = -1;
    $('#searchGroup').hover(function () {
        $('#searchGroup').keydown(function (event) {
            if(event.keyCode==13) {
                var isFocus=$("#searchGroup").is(":focus");
                if(isFocus) {
                    $.get('searchGroup?groupName=' + $('#searchGroup').val(), function (data) {
                        if (data > 0)
                            window.location.href = '/getGroupInfo?groupId=' + data;
                        else if (data == -1) {
                            alert('没有找到该组,请检查输入是否正确!');
                            data = -2;
                        }
                    });
                }
            }
        });
    })
</script>
</body>
</html>