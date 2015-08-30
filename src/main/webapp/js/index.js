$(document).ready(function () {

    //加载所有组信息
    url = '/getGroupInformation';
    $.get(url, function (data, status) {
        if (status == 'success' && data != "") {
            var obj = eval(data);

            allGroup = obj.allGroup;
            allBossName = obj.allBossName;
            setPictureWall();
            lastOrder = obj.lastOrder;
            console.log(obj.lastOrder);
            myGroup  = obj.myGroup;

        }
    });
    //today order
    var table = $('<table></table>'),
         tr = $('<tr></tr>'),
         //tdNikeName = $('<td></td>'),
         tdOrderName = $('<td></td>'),
         tdOrderNumber = $('<td></td>'),
         tdOrderPrice = $('<td></td>');
    //for(var i = 0; i < lastOrder.length; i ++) {
        var nikeName = lastOrder.nickName,
            orderNumber = lastOrder.orderNumber,
            orderName = lastOrder.orderName,
            orderPrice = lastOrder.orderPrice;
       // tdNikeName.append(nikeName);
        tdOrderNumber.append(orderNumber);
        tdOrderName.append(orderName);
        tdOrderPrice.append(orderPrice);
        tr.append(tdOrderName).append(tdOrderNumber).append(tdOrderPrice);
        table.appendTo(tr);
        $('.order-content').append(table);
    //}


    //my-groups

    function getMyGroups() {
        /*gloval variable*/

        var groupNum = myGroup.length,
            perPageNum = 5,
            current = 0,
            page_num = ((groupNum % perPageNum) == 0) ? (groupNum / perPageNum) : (parseInt(groupNum / perPageNum) + 1),
            cur_page = 0;

        for (var i = 0; i < page_num; i++) {
            var content = '<span class=\"ring\"></span>';
            if (i == cur_page) {
                content = '<span class=\"ring circle\"></span>';
            }
            $('.my-groups .page').append(content);
        }

        /*img-show*/
        for (var i = 0; (i < perPageNum) && (i < groupNum); i++) {
            var groupName = myGroup[i].groupName;
            var groupId = myGroup[i].groupId;
            var groupLink = '/getGroupInfo?groupId=' + myGroup[i].groupId;
            var content = '<div class="my-group"> <a href=\"' + groupLink[i] + '\" >' + groupName[i] + '</a> </div>';
            $('.my-groups-content').append(content);
        }
        current = i;

        /*next page  event listener*/
        $('.my-groups .next').click(function () {
            var page_circle = $('.my-groups .page .ring');
            if (page_num > 1) {
                $(page_circle[cur_page++]).removeClass('circle');
                $(page_circle[cur_page]).addClass('circle');
            }
            if (current < groupNum) {
                $('.my-groups-content').remove();
                for (var i = current; (i < (current + perPageNum) && (i < groupNum)); i++) {
                    var groupName = myGroup[i].groupName;
                    var groupId = myGroup[i].groupId;
                    var groupLink = '/getGroupInfo?groupId=' + myGroup[i].groupId;
                    var content = '<div class="my-group"> <a href=\"' + groupLink[i] + '\" >' + groupName[i] + '</a> </div>';
                    $('.my-groups-content').append(content);
                }
            }
            current = i;
            if (cur_page > 0)    $('.my-groups-content .pre').css("display", "block");
            if (cur_page == page_num - 1) $('.my-groups-content .next').css("display", "none");
        });

        /*previous page event listener*/
        $('.my-groups-content .pre').click(function () {
            var page_circle = $('.my-groups-content .page .ring');
            if (cur_page > 0) {
                $(page_circle[cur_page]).removeClass('circle');
                cur_page--;
                $(page_circle[cur_page]).addClass('circle');
            }
            current = cur_page * (perPageNum);
            if (cur_page == 0) $('.my-groups-content .pre').css("display", "none");
            if (cur_page < (page_num - 1)) $('.my-groups-content .next').css("display", "block");
            $('.my-groups-content').remove();
            for (var i = current; i < (current + (row_num * col_num)); i++) {
                var groupName = myGroup[i].groupName;
                var groupId = myGroup[i].groupId;
                var groupLink = '/getGroupInfo?groupId=' + myGroup[i].groupId;
                var content = '<div class="my-group"> <a href=\"' + groupLink[i] + '\" >' + groupName[i] + '</a> </div>';
                $('.my-groups-content').append(content);
            }
            current = i;
        });
    }


});
