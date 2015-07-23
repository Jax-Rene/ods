$(document).ready(function () {
   $('#changename').click(function(){
       var url = 'changeNickName?newName=' + $('#newname').val() +'&groupId=' +$('#groupid').val();
       $.get(url,function(data){
          if(data==true){
              alert('恭喜您,修改昵称成功!');
          }else
            alert('很抱歉由于未知错误,更改失败!');
       });
   });

    $('#addmember').click(function () {
        var url = 'inviteMember?memberName=' + $('#membername').val() + '&groupId=' + $('#groupid').val();
        $.get(url, function (data) {
             if(data==true){
                 alert('已向该用户发送邀请通知,请等待对方回应');
             }else
                alert('邀请失败,请检查所输入用户名是否存在!');
        });
    });

    $('#order').submit(function(){
        if(confirm('是否要提交订单,系统将为所有组员发送邮件消息!')){
            //获取订餐类型
            var values = $("[name='ordertype']");
            for(var i=0;i<values.length;i++){
                if(values[i].checked)
                    var orderType = i;
            }
            var url = $(this).attr('action') + "?orderType=" + orderType;
            $(this).attr('action',url);
            return true;
        }
    });


});