<html>
<head>
    <script src="${absoluteContextPath}/js/jquery-2.1.4.min.js"></script>
    <link rel="stylesheet" href="${absoluteContextPath}/css/jquery.Jcrop.css" type="text/css" />
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery.Jcrop.js"></script>
    <script type="text/javascript" src="${absoluteContextPath}/js/ajaxfileupload.js"></script>
<body>

<div class="hide" id="group">
    <form action="/createGroup" method="post" id="crop_form" enctype="multipart/form-data">
        <input type="hidden" id="target_x" name="targetX"/>
        <input type="hidden" id="target_y" name="targetY"/>
        <input type="hidden" id="target_w" name="targetW"/>
        <input type="hidden" id="target_h" name="targetH"/>
        请输入组名：<input type="text" name="newGroupName"/>${newGroupError !""}


        您可以选择是否上传小组头像:<input type="file" name="newGroupIcon" id="newGroupIcon"/><br/>

        <div id="big-pic">
            <img src="/img/no-img.png" alt="上传的图片" width="100%" id="target_img"/>
        </div>

        <div id="preview-pane">
            <div class="preview-container">
                <img src="/img/no-img.png" class="jcrop-preview" alt="图片预览" width="100%" id="preview"/>
            </div>
        </div>
        <input type="button" id="submitGroup" value="确认提交"/>
    </form>
</div>


<script>

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

    $('#target_img').Jcrop({
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

    $('#newGroupIcon').on('change', function () {
        $.ajaxFileUpload({
            url: 'restoreTempPic',//处理图片脚本
            secureuri: false,
            fileElementId: 'newGroupIcon',//file控件id
            dataType: 'json',
            success: function (data) {
                if(data!=null){
                    var url = 'img/temp/' + data.trim();
                    $("#target_img, #preview").html('<img src="img/temp/'+data.trim() +'">');

                    $('#target_img').attr('src',url);
                    $('#preview').attr('src',url);
                }
            },
            error: function (data) {
                alert("失败!");
            }
        });
    });

</script>


</body>
</head>
</html>