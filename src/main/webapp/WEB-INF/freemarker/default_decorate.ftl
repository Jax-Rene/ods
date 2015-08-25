<!-- 这个装饰页面包含用户模块 - 登陆、注册 -->
<html>
<head>
    <title>${title}</title>
    <script src="${absoluteContextPath}/js/jquery-2.1.4.min.js"></script>
    <script src="http://code.jquery.com/jquery-migrate-1.2.1.js"></script>
    <script src="${absoluteContextPath}/js/bui.js"></script>
    <script src="${absoluteContextPath}/js/message.js"></script>
    <script src="http://g.alicdn.com/bui/seajs/2.3.0/sea.js"></script>
    <script src="http://g.alicdn.com/bui/bui/1.1.21/config.js"></script>

    <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet">
    <link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/ods.css">
${head}
</head>
<body>
<#include "head.ftl"/>
${body}
<#include "foot.ftl"/>
</body>
</html>