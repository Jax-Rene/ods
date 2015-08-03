<html>
<head>
    <title>提交我的订餐信息</title>
    <script src="${absoluteContextPath}/js/order.js"></script>
</head>
<body>

<div>
<form id="submitOrder">
套餐名:<input type="text" id="orderName"/>
单价:<input type="number" id="singlePrice" value="0" min="0" step="0.1"/>
数量:<input type="number" id="orderNumber" value="1" min="1" step="1"/>
    <input type="hidden" id="orderPrice"/>
    <input type="hidden" value="${order.orderUrl}" id="orderUrl"/>
    <input type="hidden" value="${order.orderId}" id="orderId"/>
    <input type="submit" value="提交"/>
</form>
</div>

<hr/>
<h1>今日订餐页面</h1>
<div>
<iframe id="orderContent" src="" scrolling="true" width="80%" height="80%"></iframe>
</div>

</body>
</html>