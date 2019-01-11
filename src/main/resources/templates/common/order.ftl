<!DOCTYPE html>
<html lang="en">
<!-- BEGIN HEAD -->

<head>
  <meta charset="utf-8"/>
  <title>萃智知识产权管理平台</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta content="width=device-width, initial-scale=1" name="viewport"/>
  <meta content="萃智知识产权管理平台" name="description"/>
  <meta content="" name="author"/>
  <!-- BEGIN GLOBAL MANDATORY STYLES -->
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/simple-line-icons/2.4.0/css/simple-line-icons.min.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN PAGE LEVEL PLUGINS -->

  <!-- END PAGE LEVEL PLUGINS -->
  <!-- BEGIN THEME GLOBAL STYLES -->
  <link href="${request.contextPath}/global/css/components.css" rel="stylesheet" id="style_components" type="text/css"/>
  <link href="${request.contextPath}/global/css/plugins.css" rel="stylesheet" type="text/css"/>
  <!-- END THEME GLOBAL STYLES -->
  <!-- BEGIN THEME LAYOUT STYLES -->
  <link href="${request.contextPath}/layouts/layout/backstage/css/layout.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/layouts/layout/backstage/css/themes/default.css" rel="stylesheet" type="text/css"
        id="style_color"/>
  <!-- END THEME LAYOUT STYLES -->

  <style>

    ul {
      width: 200px;

      list-style: none;
    }

    ul li {
      list-style: none;
      width: 200px;
      height: 25px;
      text-align: center;
      float: left;
      border: 2px solid #999;
      margin: 0 0 -2px -2px;
      position: relative;
      z-index: 0;
    }
    .title {

      list-style: none;
      width: 200px;
      height: 25px;
      text-align: center;
      float: left;
      border: 2px solid #999;
      margin: 0 0 -2px 38px;
      position: relative;
      z-index: 0;
    }
    .prop-handle {
      background-color: #65d4c5;
    }

    .title-handle {
      background-color: #9acb6f;
    }

  </style>
</head>
<!-- END HEAD -->

<body class="page-container-bg-solid">
<div class="page-wrapper">

  <div class="page-wrapper-row full-height">
    <div class="page-wrapper-middle">
      <!-- BEGIN CONTAINER -->
      <div class="page-container">
        <!-- BEGIN CONTENT -->
        <div class="page-content-wrapper">
          <!-- BEGIN CONTENT BODY -->
          <!-- BEGIN PAGE HEAD-->

          <!-- END PAGE HEAD-->
          <!-- BEGIN PAGE CONTENT BODY -->

          <div class="page-content-inner">
            <div class="container-fluid">
              <div class="portlet light ">
                <div class="portlet-title ">
                  <div class="note note-info">
                    <p>批量生成虚拟订单，每次最多1000个。</p>
                  </div>
                </div>
                <div class="portlet-body">
                  <input id="num" type="text" >
                  <button id="begin" type="button" class="btn  btn-success " >
                    <i class="fa fa-sign-in"></i> 开始
                  </button>
                </div>
              </div>

            </div>
          </div>


          <!-- END PAGE CONTENT BODY -->
          <!-- END CONTENT BODY -->
        </div>
        <!-- END CONTENT -->

      </div>
      <!-- END CONTAINER -->
    </div>
  </div>

</div>


<!-- BEGIN CORE PLUGINS -->
<script src="${request.contextPath}/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->

<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->

<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script type="text/javascript">
  jQuery(document).ready(function () {

    $('#begin').on('click',function () {
      $.ajax({
        url: "${request.contextPath}/test/makeOrders",
        data: {
          "num":$("#num").val()
        },
        dataType: 'json',
        success: function (data) {

          $('.portlet-body').empty();
          $('.portlet-body').append('<p>'+data+'</p>');
        }
      });
    });

  });



</script>
</body>

</html>