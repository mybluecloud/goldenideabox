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
  <link href="${request.contextPath}/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN PAGE LEVEL PLUGINS -->
  <link href="${request.contextPath}/global/plugins/typeahead/typeahead.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css"
        rel="stylesheet" type="text/css"/>

  <link href="${request.contextPath}/global/plugins/datatables/css/dataTables.bootstrap.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/datatables/css/fixedColumns.bootstrap.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/datatables/css/scroller.bootstrap.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/datatables/css/responsive.bootstrap.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/datatables/css/select.dataTables.min.css"
        rel="stylesheet" type="text/css"/>
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
  <style>
    .group-checkable {
      margin-left: 3px !important;
      margin-bottom: 1px !important;
      vertical-align: middle !important;
      width: 12px !important;
      height: 12px !important;
    }
  </style>
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
                <h1>案件导入
                </h1>
              </div>

            </div>
          </div>
          <!-- END PAGE HEAD-->
          <!-- BEGIN PAGE CONTENT BODY -->

          <div class="page-content">
            <div class="container">
              <div class="page-content-inner">


                  <div class="portlet light " id="form_wizard_1">
                    <div class="portlet-body form">
                      <div class="form-wizard">
                        <div class="form-body">
                          <ul class="nav nav-pills nav-justified steps">
                            <li>
                              <a href="#tab1" data-toggle="tab" class="step">
                                <span class="number"> 1 </span>
                                <span class="desc">
                                                                                    <i class="fa fa-check"></i> 开始 </span>
                              </a>
                            </li>

                            <li>
                              <a href="#tab2" data-toggle="tab" class="step active">
                                <span class="number"> 2 </span>
                                <span class="desc">
                                                                                    <i class="fa fa-check"></i> 模板配置 </span>
                              </a>
                            </li>
                            <li>
                              <a href="#tab3" data-toggle="tab" class="step">
                                <span class="number"> 3 </span>
                                <span class="desc">
                                                                                    <i class="fa fa-check"></i> 数据配置 </span>
                              </a>
                            </li>
                            <li>
                              <a href="#tab4" data-toggle="tab" class="step">
                                <span class="number"> 4 </span>
                                <span class="desc">
                                                                                    <i class="fa fa-check"></i> 数据比较 </span>
                              </a>
                            </li>
                            <li>
                              <a href="#tab5" data-toggle="tab" class="step">
                                <span class="number"> 5 </span>
                                <span class="desc">
                                                                                    <i class="fa fa-check"></i> 完成 </span>
                              </a>
                            </li>
                          </ul>
                          <div id="bar" class="progress progress-striped" role="progressbar">
                            <div class="progress-bar progress-bar-success"></div>
                          </div>
                          <div class="tab-content">

                            <div class="tab-pane active" id="tab1">

                              <div class="fileinput fileinput-new" data-provides="fileinput">
                                <div class="input-group input-large">
                                  <div class="form-control uneditable-input input-fixed input-medium" >
                                    <i class="fa fa-file fileinput-exists"></i>&nbsp;
                                    <span class="fileinput-filename"> </span>
                                    <input type="hidden" id="file" value="">
                                  </div>
                                  <span id="btn-upload" class="btn btn-success fileinput-button ">
                                    <span>选择文件</span>
                                    <input id="fileupload" type="file" name="files[]">
                                  </span>
                                </div>
                              </div>

                            </div>
                            <div class="tab-pane" id="tab2">
                              <form class="form-horizontal" >
                                <div class="form-group">
                                  <label class="control-label col-md-3">模板
                                  </label>
                                  <div class="col-md-4">
                                    <select class="form-control " id="template">
                                      <#if templates?exists>
                                        <#list templates as template>
                                          <option value="${template.id}">${template.name}</option>
                                        </#list>
                                      </#if>
                                    </select>
                                  </div>
                                </div>
                                <div class="form-group">
                                  <label class="control-label col-md-3">工作表
                                  </label>
                                  <div class="col-md-4">
                                    <select class="form-control " id="sheet">
                                    </select>
                                  </div>
                                </div>
                              </form>
                            </div>
                            <div class="tab-pane" id="tab3">

                              <table class="table table-bordered table-striped"
                                     style="background-color:#e5e5e5">
                                <tbody id="property">
                                </tbody>
                              </table>
                            </div>
                            <div class="tab-pane" id="tab4">
                              <table id="patentlist" class="table table-striped table-bordered nowrap" style="width:100%">
                              </table>
                            </div>
                            <div class="tab-pane" id="tab5">

                            </div>
                          </div>
                        </div>
                        <div class="form-actions">

                            <div class="col-md-offset-5 col-md-7">

                              <button class="btn btn-outline green button-next"> 下一步
                                <i class="fa fa-angle-right"></i>
                              </button>
                              <a href="javascript:;" class="btn green button-submit"> 完成
                                <i class="fa fa-check"></i>
                              </a>
                            </div>

                        </div>
                      </div>

                    </div>
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
<script src="${request.contextPath}/global/plugins/jquery.bootstrap.wizard.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>

<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.ui.widget.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.iframe-transport.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload-validate.js"
        type="text/javascript"></script>

<script src="${request.contextPath}/global/plugins/datatables/js/jquery.dataTables.js" type="text/javascript" language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.bootstrap.min.js" type="text/javascript" language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.fixedColumns.min.js" type="text/javascript"
        language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/scroller.bootstrap.min.js" type="text/javascript" language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.scroller.min.js" type="text/javascript" language="javascript"></script>

<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.responsive.min.js" type="text/javascript" language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/responsive.bootstrap.min.js" type="text/javascript" language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.select.js" type="text/javascript" language="javascript"></script>

<script src="${request.contextPath}/global/plugins/sweetalert2/sweetalert2.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/pages/scripts/import.js" type="text/javascript"></script>

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