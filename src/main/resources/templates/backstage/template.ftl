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
  <link href="${request.contextPath}/global/plugins/datatables/css/dataTables.bootstrap.min.css" rel="stylesheet"
        type="text/css"/>

  <link href="${request.contextPath}/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-multi-select/css/multi-select.css" rel="stylesheet" type="text/css" />
  <link href="${request.contextPath}/global/plugins/sweetalert2/sweetalert2.min.css" rel="stylesheet" type="text/css"/>
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
                <h1>模板管理

                </h1>
              </div>
              <!-- END PAGE TITLE -->

            </div>
          </div>
          <!-- END PAGE HEAD-->
          <!-- BEGIN PAGE CONTENT BODY -->

          <div class="page-content-inner">
            <div class="container-fluid">
              <div class="portlet light ">
                <div class="portlet-title">
                  <div class="caption font-blue-madison">
                    <i class="icon-settings font-blue-madison"></i>
                    <span class="caption-subject bold uppercase">我的模板</span>
                  </div>
                  <div class="actions">
                    <div class="btn-group btn-group-devided" >

                      <a id="template_new" type="button"
                         class="btn  blue-madison btn-outline btn-circle btn-sm"
                          href="${request.contextPath}/newTemplate">
                        <i class="fa fa-plus"></i> 新建模板 </a>

                    </div>
                  </div>
                </div>
                <div class="portlet-body">
                  <@shiro.hasPermission name="/template/review">
                  <input type="hidden" id="review" value="1"/>
                  </@shiro.hasPermission>
                  <table
                      class="table table-striped table-bordered table-hover table-checkable order-column"
                      id="templatelist">
                  </table>

                  <!-- 权限配置对话框 begin-->
                  <div id="permit_dialog" class="portlet box blue-hoki modal fade">
                    <div class="portlet-title">
                      <div class="caption" id="title">
                        <i class="fa fa-gear"></i>模板授权
                      </div>

                    </div>
                    <div class="portlet-body form">

                      <form action="#" class="form-horizontal">

                        <div class="form-body">
                          <div class="form-group">
                            <input  type="hidden" id="templateID" value="0" />
                            <div class="form-group">
                              <label class="control-label col-md-3">用户</label>
                              <div class="col-md-6">
                                <select multiple="multiple" class="multi-select" id="permit_multi_select" name="permit_multi_select[]" >
                                </select>

                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="form-actions">
                          <div class="row">
                            <div class="col-md-offset-4 col-md-8">
                              <button type="submit" data-dismiss="modal" class="btn btn-circle blue-madison"
                                      onclick="permit()">保存
                              </button>
                              <button type="button" data-dismiss="modal" class="btn btn-circle default">关闭
                              </button>
                            </div>
                          </div>
                        </div>
                      </form>

                    </div>
                  </div>
                  <!-- 权限配置对话框 end-->
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
<script src="${request.contextPath}/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${request.contextPath}/global/plugins/typeahead/handlebars.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/typeahead/typeahead.bundle.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/jquery.dataTables.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.bootstrap.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-notify/bootstrap-notify.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-modal/js/bootstrap-modal.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-multi-select/js/jquery.multi-select.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/sweetalert2/sweetalert2.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/pages/scripts/template.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/message.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script type="text/javascript">
  var http_request ="${request.contextPath}";
</script>
</body>

</html>