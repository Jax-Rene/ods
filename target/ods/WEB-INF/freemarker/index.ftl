<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>ods首页</title>
    <script src="${absoluteContextPath}/js/index.js" type="text/javascript"></script>
    <script src="${absoluteContextPath}/js/bui.js" type="text/javascript"></script>
    <script src="${absoluteContextPath}/js/photo_wall.js" type="text/javascript"></script>
    <link rel="stylesheet" href="${absoluteContextPath}/css/jquery.Jcrop.css" type="text/css" />
    <script type="text/javascript" src="${absoluteContextPath}/js/jquery.Jcrop.js"></script>
    <script type="text/javascript" src="${absoluteContextPath}/js/ajaxfileupload.js"></script>

    <style>
        .jcrop-holder #preview-pane {
            display: block;
            position: absolute;
            z-index: 2000;
            top: 10px;
            right: -280px;
            padding: 6px;
            border: 1px rgba(0,0,0,.4) solid;
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
            width: 250px;
            height: 250px;
            overflow: hidden;
        }

        #big-pic{
            width : 400px;
            height: 400px;
        }
    </style>

</head>
<body>
<div id="banner">
    <div class="container">
        <div id="orders">
            <div class="orders">
                <div class="order-header">
                    <div class="order-details">
                        <h1>今日订单</h1>
							<span>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
								<div class="triangle"></div>
							</span>
                    </div>
                </div>
            </div>
        </div>
        <div id="my_groups">
            <div class="my-groups span8 offset14">
                <h1>我的小组</h1>

                <div class="my-group"><a href="#">我的第1个小组</a></div>
                <div class="my-group"><a href="#">我的第2个小组</a></div>
                <div class="my-group"><a href="#">我的第3个小组</a></div>
                <div class="my-group"><a href="#">我的第4个小组</a></div>
                <div class="my-group"><a href="#">我的第5个小组</a></div>
            </div>
        </div>
    </div>
</div>
</div>
<div class="groups-header">
    <div class="container">
        <h1>所有的小组</h1>
        <span><a href="javascript:void(0)" id="createGroup">创建小组</a> </span>
    </div>
</div>
<div id="groups">
    <div class="container">
        <div class="img-container">
            <div class="img-table">
                <div class="next"><img src="/img/right.png"></div>
                <div class="pre"><img src="/img/left.png"></div>
            </div>
            <div class="page">
            </div>
        </div>
    </div>
</div>

<div class="hide" id="group">
    <form action="/createGroup" method="post" id="crop_form" enctype="multipart/form-data">
        <input type="hidden" id="target_x" name="targetX" />
        <input type="hidden" id="target_y" name="targetY" />
        <input type="hidden" id="target_w" name="targetW" />
        <input type="hidden" id="target_h" name="targetH" />


        <div>
        请输入组名：<input type="text" name="newGroupName"/>${newGroupError !""}
        您可以选择是否上传小组头像:<input type="file" name="newGroupIcon" id="newGroupIcon"/><br/>
            <div id="big-pic">
                <img src="/img/no-img.png" alt="图片预览" width="100%" id="target"/>
            </div>

            <div id="preview-pane">
                <div class="preview-container">
                    <img src="/img/no-img.png" class="jcrop-preview" alt="图片预览" width="100%" id="preview"/>
                </div>
            </div>
        </div>
        <#--<input type="button" id="submitGroup" value="确认提交"/>-->
        <input type="submit"  value="确认提交"/>
    </form>
</div>

</body>
</html>
