function setPictureWall() {
    /*gloval variable*/
    //allGroup = json.img;
    groupNum = allGroup.length;
    col_num = 4;
    row_num = 4;
    count = groupNum / col_num;
    current = 0;
    page_num = ((groupNum % (row_num * col_num)) == 0) ? (groupNum / (row_num * col_num)) : (parseInt(groupNum / (row_num * col_num)) + 1);
    cur_page = 0;
    /*init*/
    screen_length = $(window).width();
    for (var i = 0; i < page_num; i++) {
        var content = '<span class=\"ring\"></span>';
        if (i == cur_page) {
            content = '<span class=\"ring circle\"></span>';
        }
        $('.page').append(content);
    }

    /*img-show*/
    for (var i = 0; (i < col_num * row_num) && (i < groupNum); i++) {
        var content =
            '<a href=\'getGroupInfo?groupId='+ allGroup[i].id + '\'><div class=\"img-cell\">'
            + '<img src=\'img\\icon\\' + allGroup[i].groupIcon + '\'/>'
            + '<div class=\"img-details\">'
            + '<div class="text-details">'
            + '<p>' + allGroup[i].groupName + '</p>'
            + '<div>'
            + '<span class=\"congratulation\">' + allBossName[i] + '</span>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div></a>';

        $('.img-table').append(content);
    }
    current = i;

    /*hover event listener*/
    $('.img-table').on('mouseenter', '.img-cell', function () {
        var cur_left = $(this).offset().left;
        var cur_top = $(this).offset().top;
        var cur_width = $(this).width();
        if ((screen_length - cur_left - cur_width) < (cur_width)) {
            $(this).children('.img-details').css({
                'animation': 'slide-right 0.5s linear 0s 1 alternate',
                '-moz-animation': 'slide-right 0.5s linear 0s 1 alternate',
                '-webkit-animation': 'slide-right 0.5s linear 0s 1 alternate',
                '-o-animation': 'slide-right 0.5s linear 0s 1 alternate',
                'left': '-100%',
            });
        }
        $(this).children('img').css({'opacity': '1.0'});
        $(this).children('.img-details').css("display", "block");


    });
    $('.img-table').on('mouseleave', '.img-cell', function () {
        $(this).children('.img-details').css("display", "none");
        $(this).children('img').css({'opacity': '0.4'});
    });

    /*next page  event listener*/
    $('.next').click(function () {
        var page_circle = $('.page .ring');
        if (page_num > 1) {
            $(page_circle[cur_page++]).removeClass('circle');
            $(page_circle[cur_page]).addClass('circle');
        }
        if (current < groupNum) {
            $('.img-cell').remove();
            for (var i = current; (i < (current + col_num * row_num) && (i < groupNum)); i++) {
                var content =
                    '<div class=\"img-cell\">'
                    + '<div class=\"cover\">' + '</div>'
                    + '<img src=\"' + allGroup[i].img_url + '\">'
                    + '<div class=\"img-details\">'
                    + '<div class="text-details">'
                    + '<p>姓名：' + allGroup[i].name + '</p>'
                    + '<p>公司：' + allGroup[i].company + '</p>'
                    + '<div><img  class=\"dot\" src=\"./img/dot.jpg\">' + '<span class=\"congratulation\">' + allGroup[i].congratulation + '</span>' + '<img src=\"./img/dot.jpg\"></div>'
                    + '</div>'
                    + '</div>'
                    + '</div>';
                $('.img-table').append(content);
            }
        }
        current = i;
        if (current > 0)    $('.pre').css("display", "block");
        if (current == groupNum) $('.next').css("display", "none");
    });

    /*previous page event listener*/
    $('.pre').click(function () {
        var page_circle = $('.page .ring');
        if (cur_page > 0) {
            $(page_circle[cur_page]).removeClass('circle');
            cur_page--;
            $(page_circle[cur_page]).addClass('circle');
        }
        current = cur_page * (row_num * col_num);
        if (current == 0) $('.pre').css("display", "none");
        if (cur_page < (page_num - 1)) $('.next').css("display", "block");
        $('.img-cell').remove();
        for (var i = current; i < (current + (row_num * col_num)); i++) {
            var content =
                '<div class=\"img-cell\">'
                + '<div class=\"cover\">' + '</div>'
                + '<img src=\"' + allGroup[i].img_url + '\">'
                + '<div class=\"img-details\">'
                + '<div class="text-details">'
                + '<p>姓名：' + allGroup[i].name + '</p>'
                + '<p>公司：' + allGroup[i].company + '</p>'
                + '<div><img  class=\"dot\" src=\"./img/dot.jpg\">' + '<span class=\"congratulation\">' + allGroup[i].congratulation + '</span>' + '<img src=\"./img/dot.jpg\"></div>'
                + '</div>'
                + '</div>'
                + '</div>';
            $('.img-table').append(content);
        }
        current = i;
    });
}
