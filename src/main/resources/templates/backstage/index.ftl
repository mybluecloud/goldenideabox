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
  <link href="${request.contextPath}/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
  <link href="${request.contextPath}/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
  <link href="${request.contextPath}/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN PAGE LEVEL PLUGINS -->
  <link href="${request.contextPath}/global/plugins/typeahead/typeahead.css" rel="stylesheet" type="text/css" />
  <link href="${request.contextPath}/global/plugins/icheck/skins/square/blue.css" rel="stylesheet" type="text/css"/>
  <link rel="stylesheet" href="${request.contextPath}/global/plugins/amcharts/plugins/export/export.css" type="text/css" media="all" />
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

</head>
<!-- END HEAD -->

<body class="page-container-bg-solid">
<div class="page-wrapper">
            <#include "header.ftl">
  <div class="page-wrapper-row full-height">
    <div class="page-wrapper-middle">
      <!-- BEGIN CONTAINER -->
      <div class="page-container">
        <!-- BEGIN CONTENT -->
        <div class="page-content-wrapper">
          <!-- BEGIN CONTENT BODY -->
          <!-- BEGIN PAGE HEAD-->
          <div class="page-head">
            <div class="container-fluid">
              <!-- BEGIN PAGE TITLE -->
              <div class="page-title">
                <h1>首页
                </h1>
              </div>
              <!-- END PAGE TITLE -->
              <div class="page-toolbar">
                <div class="btn-group btn-theme-panel">
                  <a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
                    <i class="icon-settings"></i>
                  </a>
                  <div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click">
                    <div class="col-md-12 col-sm-12 col-xs-12 seperator">
                      <h3>添加模块</h3>
                      <ul class="theme-settings">

                      </ul>
                      <div class="col-md-offset-4 col-md-8" style="margin-top: 20px;">
                        <button type="button" data-dismiss="dropdown" class="btn btn-circle blue-madison" id="btnAddModel">添加
                        </button>

                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- END PAGE HEAD-->
          <!-- BEGIN PAGE CONTENT BODY -->

          <div class="page-content">
            <div class="container-fluid">
              <div class="page-content-inner">
              </div>
            </div>
          </div>


          <!-- END PAGE CONTENT BODY -->
          <!-- END CONTENT BODY -->
        </div>
        <!-- END CONTENT -->
        <#include "../common/sidebar.ftl">
      </div>
      <!-- END CONTAINER -->
    </div>
  </div>
            <#include "footer.ftl">
</div>


<!-- BEGIN CORE PLUGINS -->
<script src="${request.contextPath}/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${request.contextPath}/global/plugins/typeahead/handlebars.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/typeahead/typeahead.bundle.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/icheck/icheck.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/amcharts/amcharts.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/amcharts/serial.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/amcharts/pie.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/amcharts/plugins/export/export.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/amcharts/themes/light.js"></script>
<script src="${request.contextPath}/global/plugins/sortable/Sortable.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
  <script src="${request.contextPath}/pages/scripts/index.back.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/message.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script>
  var http_request = "${request.contextPath}";

  <#if lastLoginTime??>
  console.info("${lastLoginTime}");
  Cookies.set('lastLoginTime', "${lastLoginTime}");
  Cookies.set('updateMsgTime', "${lastLoginTime}");
  </#if>

</script>
</body>

</html>