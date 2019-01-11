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
  <link href="${request.contextPath}/global/plugins/datatables/css/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css" />
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
                <h1>专利审查信息自动查询
                </h1>
              </div>
              <!-- END PAGE TITLE -->
            </div>
          </div>
          <!-- END PAGE HEAD-->
          <!-- BEGIN PAGE CONTENT BODY -->

          <div class="page-content">
            <div class="container-fluid">
              <div class="portlet light ">
                <div class="portlet-title">
                  <div class="caption font-blue-madison">
                    <i class="icon-settings font-blue-madison"></i>
                    <span class="caption-subject bold uppercase">专利审查任务</span>
                  </div>
                  <div class="actions">
                    <div class="btn-group btn-group-devided" data-toggle="buttons">

                      <button id="addTask" type="button" class="btn  blue-madison btn-outline btn-circle btn-sm"
                              data-toggle="modal">
                        <i class="fa fa-qrcode"></i> 添加任务
                      </button>


                    </div>
                  </div>
                </div>
                <div class="portlet-body">


                  <table
                      class="table table-striped table-bordered table-hover table-checkable order-column"
                      id="recordlist">
                  </table>

                </div>
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
<script src="${request.contextPath}/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${request.contextPath}/global/plugins/datatables/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.bootstrap.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/pages/scripts/patent.record.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script>
  var http_request = "${request.contextPath}";

</script>
</body>

</html>