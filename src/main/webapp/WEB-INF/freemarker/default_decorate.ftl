<!-- 这个装饰页面包含用户模块 - 登陆、注册 -->
<html>
<head>
    <title>${title}</title>
    <script src="${absoluteContextPath}/js/jquery-2.1.4.min.js"></script>
    <script src="${absoluteContextPath}/js/bui.js"></script>
    <script src="${absoluteContextPath}/js/message.js"></script>

    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/dpl.css">
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/bui.css">
    <link rel="stylesheet" type="text/css" href="${absoluteContextPath}/css/ods.css">
${head}
</head>
<body>
<#include "head.ftl"/>
${body}
<#include "foot.ftl"/>
</body>
</html>