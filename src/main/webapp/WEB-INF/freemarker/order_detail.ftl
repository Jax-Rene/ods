<html>
<head>
    <title>提交我的订餐信息</title>
    <script src="${absoluteContextPath}/js/order.js"></script>
    <script src="${absoluteContextPath}/js/count_time.js"></script>
</head>
<body>

<div>
    <div style="text-align: center;margin-top:40px">
        <h1 style="color: #A57F59;font-size: 40px">${group.groupName!""}的订餐</h1>
        订餐倒计时:&nbsp;<span id="restTime" style="color: red"></span>
        <input type="hidden" id="endTime" value="${order.orderEnd!""}"/>
    </div>

    <form id="submitOrder">
        <div style="font-size: 15px;
        text-align: center">
            套餐名:<input type="text" id="orderName"/>
            单价:<input type="text" id="singlePrice" data-rules="{number:true}"/>
            数量:<input type="text" id="orderNumber" data-rules="{number:true}"/>
            <input type="submit" class="button button-primary button-middle" value="提交"/>
        </div>
        <input type="hidden" id="orderPrice"/>
        <input type="hidden" value="${order.orderUrl}" id="orderUrl"/>
        <input type="hidden" value="${order.orderId}" id="orderId"/>
    </form>
</div>

<h2 style="text-align: center">最新订餐页面</h2>

<div>
    <iframe id="orderContent" src="${order.orderUrl}" scrolling="true" width="100%" height="80%"></iframe>
</div>


<script type="text/javascript">
    BUI.use('bui/form', function (Form) {

        new Form.Form({
            srcNode: '#submitOrder'
        }).render();
    });
</script>
</body>
</html>