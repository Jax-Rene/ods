$(document).ready(function () {
    //加载未读消息 ,定时5秒刷新一次
    setInterval(notifyMessage, '5000');
    function notifyMessage() {
        var url = '/notifyMessage';
        $.get(url, function (data) {
            $('#newsMessageNum').text(data);
        });
    }

    $('.message').on('click', function () {
        //删除div内的表、重建
        $('.message-details').empty();
        $('.message-show').slideToggle('fast');
        var url = '/getRencentMessage';
        $.get(url, function (data) {
            var table = $("<table border=\"0\">");
            table.appendTo($(".message-details"));
            var obj = eval(data); //解析json
            $(obj).each(function (index) {
                var tr = $("<tr></tr>");
                tr.appendTo(table);
                var val = obj[index];
                //加入信息内容
                var td = $("<td>" + "<span style='color:#d7bd94'>" + "[" + val.messageFrom + "] " + "</span>"  + val.messageTime + "</td>");
                td.appendTo(tr);
                //加入信息时间
                var tr2 = $("<tr class='tr2'></tr>");
                tr2.appendTo(table);
                td = $("<td>" + val.messageContent + "</td>");
                td.appendTo(tr2);
                //判断消息类型
                if (val.messageType == 1) { //类型为邀请
                    //已处理的
                    if (val.messageResult == 1)
                        td = $("<td><font color='grey'>已接受</font></td>");
                    else if (val.messageResult == -1)
                        td = $("<td><font color='grey'>已拒绝</font></td>");
                    else {
                        var groupId = val.messageGroupId;
                        var messageId = val.messageId;
                        var hrefval1 = "acceptJoin?groupId=" + groupId + "&messageId=" + messageId;
                        var hrefval2 = "refuseJoin?groupId=" + groupId + "&messageId=" + messageId;
                        td = $("<td><a href='/" + hrefval1 + "'><font color='#5cb85c'>接受</font></a> &nbsp; <a href='/" + hrefval2 + "'><font color='#d9534f'>拒绝</font></a></td>");
                    }
                    td.appendTo(tr2);
                } else if (val.messageType == 2) { //类型为请求
                    //已处理的
                    if (val.messageResult == 1)
                        td = $("<td><font color='grey'>已同意</font></td>");
                    else if (val.messageResult == -1)
                        td = $("<td><font color='grey'>已拒绝</font></td>");
                    else {
                        var groupId = val.messageGroupId;
                        var messageId = val.messageId;
                        var hrefval1 = "agreeJoin?messageId=" + messageId;
                        var hrefval2 = "disagreeJoin?messageId=" + messageId;
                        td = $("<td><a href='/" + hrefval1 + "'><font color='#5cb85c'>接受</font></a>  &nbsp; " +
                            "<a href='/" + hrefval2 + "'><font color='#d9534f'>拒绝</font></a></td>");
                    }
                    td.appendTo(tr2);
                }
            });
        });

    });


    //监视接受、拒绝操作
    $('.message-show').on("click" , "a" ,function(){
        //判断是接受的还是拒绝的
        var url = $(this).attr('href');
        debugger;
        if(url.indexOf('acceptJoin') == 1) {
                $.get(url, function (data) {
                    if(data==true){
                        alert('恭喜您,加入小组成功!');
                    }
                });
        }else{
            $.get(url, function (data) {
                if(data==true){
                    if(url.indexOf('agreeJoin') == 1)
                         alert('您已同意对方加入小组!');
                    else if(url.indexOf('refuseJoin') == 1)
                        alert('您已拒绝加入小组!');
                    else if(url.indexOf('disagreeJoin') == 1)
                        alert('您已拒绝对方加入小组!');
                }else
                    alert('未知错误,请联系管理员!');
            });
        }
        //ajax异步传输后，设置href为空不跳转
        $(this).attr('href','javascript:void(0)');
    });


});