<div class="wrapper">
    <nav class="navbar">
        <div class=" navbar-collapse">
            <ul >
                <li><a href="index.html">今日订单</a></li>
                <li class="active"><a href="#">我的小组</a></li>
            </ul>
            <ul class="navbar-right">
                <li><input type="text" id="searchGroup" placeholder="输入搜索小组的名字"/>
                    <input type="button" id="search" value="搜索小组"/></li>
                <li>您好,${curUser.userName!""}</li>
                <li><a href="#">个人中心</a></li>
                <li class="message"><a href="javascript:void(0)">消息<span class="badge" id="newsMessageNum"></span></a>
                    <div class="message-show">
                        <div class="message-triangle"></div>
                        <div class="message-details"></div>
                    </div>
                </li>
                <li><a href="/loginOut">注销</a></li>
            </ul>
        </div>
    </nav>
</div>
<#-- 这里是消息模块 -->
    <#--<div id="message">-->
        <#--<a href="#" id="getallmessage">查看所有消息</a>-->
    <#--</div>-->




