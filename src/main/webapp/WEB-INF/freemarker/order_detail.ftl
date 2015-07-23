<html>
<head>
    <title>提交我的订餐信息</title>
</head>
<body>
<form id="submitOrder" method="post" action="/submitOrder">
套餐名:<input type="text" name="orderName"/>
价格:<input type="number" name="orderPrice" value="0" min="0" step="0.1"/>
数量:<input type="number" name="orderNum" value="1" min="1" step="1"/>
<input type="submit" value="提交"/>
</form>

<hr/>
<h1>今日订餐页面</h1>

<#include "${order.orderUrl}"/>
</body>
</html>