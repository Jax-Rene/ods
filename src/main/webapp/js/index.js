jQuery(function ($) {

    // Create variables (in this scope) to hold the API and image size
    var jcrop_api,
        boundx,
        boundy,

    // Grab some information about the preview pane
        $preview = $('#preview-pane'),
        $pcnt = $('#preview-pane .preview-container'),
        $pimg = $('#preview-pane .preview-container img'),

        xsize = $pcnt.width(),
        ysize = $pcnt.height();

    $('#target').Jcrop({
        onChange: updatePreview,
        onSelect: updatePreview,
        aspectRatio: 1
    }, function () {
        // Use the API to get the real image size
        var bounds = this.getBounds();
        boundx = bounds[0];
        boundy = bounds[1];
        // Store the API in the jcrop_api variable
        jcrop_api = this;

        // Move the preview into the jcrop container for css positioning
        $preview.appendTo(jcrop_api.ui.holder);
    });

    function updatePreview(c) {
        //存储裁剪的坐标值传到后台
        $("#target_x").val(c.x);
        $("#target_y").val(c.y);
        $("#target_w").val(c.w);
        $("#target_h").val(c.h);

        if (parseInt(c.w) > 0) {
            var rx = xsize / c.w;
            var ry = ysize / c.h;
            $pimg.css({
                width: Math.round(rx * boundx) + 'px',
                height: Math.round(ry * boundy) + 'px',
                marginLeft: '-' + Math.round(rx * c.x) + 'px',
                marginTop: '-' + Math.round(ry * c.y) + 'px'
            });
        }
    };

    $('#submitGroup').click(function () {
        $('#crop_form').submit();
    });

    $('#newGroupIcon').on('change', function () {
        $.ajaxFileUpload({
        url: 'restoreTempPic',//处理图片脚本
        secureuri: false,
        fileElementId: 'newGroupIcon',//file控件id
        dataType: 'json',
        success: function (data) {
                if(data!=null){
                    var url = 'img/temp/' + data.trim();
                    $('#target').attr('src',url);
                    $('#preview').attr('src',url);
                }
            },
            error: function (data) {
                alert("失败!");
            }
        });
    });



    //加载所有组信息
    url = '/getGroupInformation';
    $.get(url, function (data, status) {
        if (status == 'success' && data != "") {
            var obj = eval(data); //解析json
            allGroup = obj.allGroup;
            allBossName = obj.allBossName;
            setPictureWall();
        }
    });


    //搜索小组
    $('#search').click(function () {
        var name = null;
        while (name == undefined || name == null || name == '') {
            name = prompt('请输入您要在该组中显示的昵称', '');
        }
        var url = 'searchGroup?groupName=' + $('#searchGroup').val() + '&nickName=' + name;
        $.get(url, function (data) {
            alert(data);
            if (data == 0) {
                alert('您已经在该组中,请勿重复操作!');
            } else if (data == 1)
                alert('没有找到小组,请重新输入!');
            else
                alert('已提交加入小组的申请,请等待小组组长回应!');
        });
    });

    $('#createGroup').click(function () {
        $('#group').toggle('slow');
    });



});
