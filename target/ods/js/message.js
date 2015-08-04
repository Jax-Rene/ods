$(document).ready(function(){
    //加载未读消息 ,定时5秒刷新一次
    setInterval(notifyMessage,'5000');
    function notifyMessage(){
    var url = '/notifyMessage';
    $.get(url, function(data) {
        $('#newsMessageNum').text(data);
    });}
    $('.message').on('click', function () {
        $('.message-show').empty(); //删除div内的表、重建
        $('.message-show').slideToggle('fast');
            var url = '/getRencentMessage';
            $.get(url,function(data){
                var table=$("<table border=\"0\">");
                table.appendTo($(".message-show"));
                var obj = eval(data); //解析json
                $(obj).each(function (index) {
                    var tr=$("<tr></tr>");
                    tr.appendTo(table);
                    var val = obj[index];
                    //加入信息内容
                    var td = $("<td>" + "[" + val.messageFrom +"] " + val.messageContent +"</td>");
                    td.appendTo(tr);
                    //加入信息时间
                    td = $("<td>" + val.messageTime +"</td>");
                    td.appendTo(tr);
                    //判断消息类型
                    if(val.messageType == 1) { //类型为邀请
                        //已处理的
                        if(val.messageResult == 1)
                            td = $("<td><font color='grey'>已接受</font></td>");
                        else if (val.messageResult == -1)
                            td = $("<td><font color='grey'>已拒绝</font></td>");
                        else{
                            var groupId = val.messageGroupId;
                            var messageId = val.messageId;
                            var hrefval1 = "acceptJoin?groupId=" + groupId + "&messageId=" + messageId;
                            var hrefval2 = "refuseJoin?groupId=" + groupId + "&messageId=" + messageId;
                            td = $("<td><a href=" + hrefval1 +">接受</a> &nbsp; <a href=" + hrefval2 + ">拒绝</a>" );
                        }
                        td.appendTo(tr);
                    }else if(val.messageType == 2){ //类型为请求
                        //已处理的
                        if(val.messageResult == 1)
                            td = $("<td><font color='grey'>已同意</font></td>");
                        else if (val.messageResult == -1)
                            td = $("<td><font color='grey'>已拒绝</font></td>");
                        else{
                            var groupId = val.messageGroupId;
                            var messageId = val.messageId;
                            var hrefval1 = "agreeJoin?messageId=" + messageId;
                            var hrefval2 = "disagreeJoin?messageId=" + messageId;
                            td = $("<td><a href=" + hrefval1 +">接受</a> &nbsp; <a href=" + hrefval2 + ">拒绝</a>" );
                        }
                        td.appendTo(tr);
                    }
                });
            });

    });

    //$('#newsMessage').click(function(){
    //    $('#message').empty(); //删除div内的表、重建
    //    var url = '/getRencentMessage';
    //    $.get(url,function(data){
    //        var table=$("<table border=\"0\">");
    //        table.appendTo($("#message"));
    //        var obj = eval(data); //解析json
    //        $(obj).each(function (index) {
    //            var tr=$("<tr></tr>");
    //            tr.appendTo(table);
    //            var val = obj[index];
    //            //加入信息内容
    //            var td = $("<td>" + "[" + val.messageFrom +"] " + val.messageContent +"</td>");
    //            td.appendTo(tr);
    //            //加入信息时间
    //            td = $("<td>" + val.messageTime +"</td>");
    //            td.appendTo(tr);
    //            //判断消息类型
    //            if(val.messageType == 1) { //类型为邀请
    //                //已处理的
    //                if(val.messageResult == 1)
    //                    td = $("<td><font color='grey'>已接受</font></td>");
    //                else if (val.messageResult == -1)
    //                    td = $("<td><font color='grey'>已拒绝</font></td>");
    //                else{
    //                    var groupId = val.messageGroupId;
    //                    var messageId = val.messageId;
    //                    var hrefval1 = "acceptJoin?groupId=" + groupId + "&messageId=" + messageId;
    //                    var hrefval2 = "refuseJoin?groupId=" + groupId + "&messageId=" + messageId;
    //                    td = $("<td><a href=" + hrefval1 +">接受</a> &nbsp; <a href=" + hrefval2 + ">拒绝</a>" );
    //                }
    //                td.appendTo(tr);
    //            }else if(val.messageType == 2){ //类型为请求
    //                //已处理的
    //                if(val.messageResult == 1)
    //                    td = $("<td><font color='grey'>已同意</font></td>");
    //                else if (val.messageResult == -1)
    //                    td = $("<td><font color='grey'>已拒绝</font></td>");
    //                else{
    //                    var groupId = val.messageGroupId;
    //                    var messageId = val.messageId;
    //                    var hrefval1 = "agreeJoin?messageId=" + messageId;
    //                    var hrefval2 = "disagreeJoin?messageId=" + messageId;
    //                    td = $("<td><a href=" + hrefval1 +">接受</a> &nbsp; <a href=" + hrefval2 + ">拒绝</a>" );
    //                }
    //                td.appendTo(tr);
    //            }
    //        });
    //    });
    //});

    ////监视接受、拒绝操作
    //$('#message a').on("click",function(){
    //    //判断是接受的还是拒绝的
    //    var url = $(this).attr('href');
    //    if(url.indexOf('acceptJoin') == 0) { //这个方法必须要有昵称
    //        var name = prompt('请输入您要在该组中显示的昵称', '');
    //        if(name != ''){
    //            url += '&nickName=' + name;
    //            $.get(url, function (data) {
    //                if(data==true){
    //                    alert('恭喜您,加入小组成功!');
    //                }
    //            });
    //        }
    //    }else{
    //        $.get(url, function (data) {
    //            if(data==true){
    //                if(url.indexOf('agreeJoin') == 0)
    //                     alert('您已同意对方加入小组!');
    //                else if(url.indexOf('refuseJoin') == 0)
    //                    alert('您已拒绝加入小组!');
    //                else if(url.indexOf('disagreeJoin') == 0)
    //                    alert('您已拒绝对方加入小组!');
    //            }else
    //                alert('未知错误,请联系管理员!');
    //        });
    //    }
    //    //ajax异步传输后，设置href为空不跳转
    //    $(this).attr('href','javascript:void(0)');
    //});


});