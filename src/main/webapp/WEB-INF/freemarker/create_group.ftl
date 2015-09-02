<html>
<head>
    <script src="${absoluteContextPath}/js/jquery-2.1.4.min.js"></script>
    <link rel="stylesheet" href="${absoluteContextPath}/css/jquery.Jcrop.css" type="text/css"/>
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery.Jcrop.js"></script>
    <script type="text/javascript" src="${absoluteContextPath}/js/ajaxfileupload.js"></script>

    <style>
        /* Apply these styles only when #preview-pane has
         been placed within the Jcrop widget */
        .jcrop-holder #preview-pane {
            display: block;
            position: absolute;
            z-index: 2000;
            top: 10px;
            right: -280px;
            padding: 6px;
            border: 1px rgba(0, 0, 0, .4) solid;
            background-color: white;

            -webkit-border-radius: 6px;
            -moz-border-radius: 6px;
            border-radius: 6px;

            -webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
            -moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
            box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
        }

        /* The Javascript code will set the aspect ratio of the crop
           area based on the size of the thumbnail preview,
           specified here */
        #preview-pane .preview-container {
            width: 200px;
            height: 200px;
            overflow: hidden;
        }

    </style>
</head>
<body>
<div id="group"
     style='padding-top: 50px;font-family: "微软雅黑", "Yuppy TC Regular", "幼圆", "黑体";font-size: 16px;height:400px'>
    <form id="crop_form" action="createGroup" method="post" enctype="multipart/form-data">
        <input type="hidden" id="target_x" name="targetX" value=""/>
        <input type="hidden" id="target_y" name="targetY" value=""/>
        <input type="hidden" id="target_w" name="targetW" value=""/>
        <input type="hidden" id="target_h" name="targetH" value=""/>

        <div class="offset11" style="width: 30%;line-height: 50px">
            <div>
                小组名称：<input type="text" name="newGroupName" id="newGroupName" class="input-normal"
                            value="${newGroupName!""}"/>
                <span style="color: red">${newGroupError !""}</span>
            </div>
            <div>
            <span>小组头像(可选)<input type="file" name="newGroupIcon" style="width: 80px" id="newGroupIcon"/></span>
            <input type="button" id="createGroup" class="button button-primary button-middle" value="确认提交"/>
            </div>
            <div>
            <span style="color: red;text-align: center">${fileError !""}</span>
            </div>
        </div>
        <input type="hidden" name="currentPic" id="currentPic"/>

    <#if fileSrc?exists>
        <div id='upimg' class="offset8">
            <img id="target_img" src="${fileSrc!""}"/>

            <div id="preview-pane">
                <div class="preview-container">
                    <img class="jcrop-preview" id="preview" src="${fileSrc!""}"/>
                </div>
            </div>
        </div>
    </#if>
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
        allowResize: false,
        allowMove: true,
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
        $('#crop_form').attr('action', 'restoreTempPic');
        $('#crop_form').submit();
    });


    $('#createGroup').click(function () {
        if ($('#newGroupName').val() == '') {
            alert('组名不能为空,请重新输入!');
        } else if ($('#target_img').attr('src') == undefined) {
            //没有图片直接上传
            $('#crop_form').attr('action', 'createGroup');
            $('#crop_form').submit();
        } else {
            $('#currentPic').val($('#preview').attr('src').substring(9));
            $('#crop_form').submit();
        }
    });

</script>


</body>
</html>