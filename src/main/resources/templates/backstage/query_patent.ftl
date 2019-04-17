<!DOCTYPE html>
<html lang="en">
<!-- BEGIN HEAD -->

<head>
  <meta http-equiv="Content-type" content="text/html; charset=utf-8">
  <meta name="viewport" content="width=device-width,initial-scale=1">
  <title>萃智知识产权管理平台</title>
  <!-- BEGIN GLOBAL MANDATORY STYLES -->
  <link href="${request.contextPath}/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"
        type="text/css"/>
  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN PAGE LEVEL PLUGINS -->
  <link href="${request.contextPath}/global/plugins/typeahead/typeahead.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/datatables/css/dataTables.bootstrap.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/datatables/css/fixedColumns.bootstrap.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/datatables/css/scroller.bootstrap.min.css"
        rel="stylesheet" type="text/css"/>

  <link href="${request.contextPath}/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-modal/css/bootstrap-modal.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/sweetalert2/sweetalert2.min.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/bootstrap-wysihtml5/bootstrap-wysihtml5.css"
        rel="stylesheet" type="text/css"/>
  <!-- END PAGE LEVEL PLUGINS -->
  <!-- BEGIN THEME GLOBAL STYLES -->
  <link href="${request.contextPath}/global/css/components.min.css" rel="stylesheet"
        id="style_components" type="text/css"/>
  <link href="${request.contextPath}/global/css/plugins.css" rel="stylesheet" type="text/css"/>
  <!-- END THEME GLOBAL STYLES -->
  <!-- BEGIN THEME LAYOUT STYLES -->
  <link href="${request.contextPath}/layouts/layout/backstage/css/layout.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/layouts/layout/backstage/css/themes/default.css" rel="stylesheet"
        type="text/css" id="style_color"/>
  <!-- END THEME LAYOUT STYLES -->
<style>
  div.dataTables_wrapper {
    width: 100%;
    margin: 0 auto;
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
                <h1>案件管理

                </h1>
              </div>
              <!-- END PAGE TITLE -->

            </div>
          </div>
          <!-- END PAGE HEAD-->
          <!-- BEGIN PAGE CONTENT BODY -->

          <div class="page-content-inner">
            <div class="container-fluid">
              <div class="portlet box blue-hoki">
                <div class="portlet-title ">
                  <div class="actions pull-left">
                    <a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;"> </a>
                  </div>
                  <div class="caption ">
                   案件列表
                  </div>
                  <ul id="tabs" class="nav nav-tabs">
                    <li class="active">
                      <a href="#" data-toggle="tab">默认</a>
                      <input type="hidden" name="id" value="0"/>
                      <input type="hidden" name="templateId" value=""/>

                    </li>
                    <#if configs?exists>
                      <#list configs as config>
                        <li>
                          <a href="#" data-toggle="tab">${config.configname}</a>
                          <input type="hidden" name="id" value="${config.id}"/>
                          <input type="hidden" name="templateId" value="${config.templateId}"/>
                        </li>
                      </#list>
                    </#if>


                  </ul>

                </div>
                <div class="portlet-body">
                  <@shiro.hasPermission name="/patent/appoint">
                  <input type="hidden" id="appoint" value="0"/>
                  </@shiro.hasPermission>
                  <div class="table-toolbar">
                    <div class="row">
                      <div class="col-md-2">
                        <select class="form-control input-sm" id="template">
                          <#if templates?exists>
                            <#list templates as template>
                              <option value="${template.id}">${template.name}</option>
                            </#list>
                          </#if>
                        </select>
                      </div>

                      <div class="col-md-10">
                        <div class="btn-group pull-right">
                          <a type="button" class="btn  blue-madison btn-outline btn-circle btn-sm"
                             data-toggle="modal" href="#config_dialog">
                            <i class="fa fa-cogs"></i> 配置 </a>
                          <a type="button" class="btn  blue-madison btn-outline btn-circle btn-sm"
                             href="javascript:;" onclick="advanceSearch()">
                            <i class="fa fa-filter"></i> 高级查询 </a>

                          <button id="bookmark" type="button" class="btn  blue-madison btn-outline btn-circle btn-sm"
                                  data-toggle="popover"><i class="fa fa-bookmark"></i> 添加标签
                          </button>
                          <button id="saveas" type="button" class="btn  blue-madison btn-outline btn-circle btn-sm"
                                  style="display: none" data-toggle="popover"><i class="fa fa-edit"></i> 另存为
                          </button>
                          <button id="save" type="button" class="btn  blue-madison btn-outline btn-circle btn-sm"
                                  style="display: none"><i class="fa fa-save"></i> 保存
                          </button>
                          <button id="delete" type="button" class="btn  blue-madison btn-outline btn-circle btn-sm"
                                  style="display: none" data-toggle="popover"><i class="fa fa-close"></i> 删除
                          </button>
                          <button id="export" type="button" class="btn  blue-madison btn-outline btn-circle btn-sm">
                            <i class="fa fa-share"></i> 导出
                          </button>

                        </div>
                      </div>
                    </div>
                    <div class="row" style="display: none" id="advance_search">
                      <div class="col-md-12">
                        <div class="portlet">

                          <div class="portlet-body form">
                            <form id="template" class="form-inline " method="post">
                              <div class="form-body">

                                <div class="form-group col-md-12">
                                  <div>
                                      <textarea id="sql" class="col-md-10 "
                                                rows="3"></textarea>
                                  </div>
                                  <div class=" col-md-2">
                                    <br>
                                    <button id="search" type="button" class="btn btn-circle blue-madison"
                                            onclick=""><i
                                        class="fa fa-search"> 查询 </i>
                                    </button>
                                  </div>
                                </div>
                                <div id="condition">

                                </div>


                              </div>
                            </form>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <table
                      class="table table-striped table-bordered table-hover order-column text-nowrap " style="width:100%"
                      id="patentlist">
                  </table>


                  <!-- 表格配置对话框 begin-->
                  <div id="config_dialog" class="portlet box blue-hoki modal fade"
                       data-backdrop="static" data-keyboard="false">
                    <div class="portlet-title">
                      <div class="caption" id="title">
                        <i class="fa fa-gift">案件显示格式设置</i>
                      </div>

                    </div>
                    <div class="portlet-body">
                      <div class="note note-success">
                        <p>拖动属性可以改变排列顺序</p>
                        <p>勾选查询字段可以设置需要显示高级查询的属性</p>
                      </div>

                      <div class="scroller" data-always-visible="1"
                           data-rail-visible="1" data-rail-color="blue" data-handle-color="red">
                        <table class="table table-striped table-bordered ">
                          <thead>
                          <tr>
                            <th>属性名</th>
                            <th><input type='checkbox' class='group-display' onchange="displaySwitch()"> 显示</th>
                            <th><input type='checkbox' class='group-search' onchange="searchSwitch()"> 查询</th>
                          </tr>
                          </thead>
                          <tbody id="propertys">

                          </tbody>
                        </table>
                      </div>
                      <form action="#" class="form-horizontal">
                        <div class="form-body">
                          <div class="form-group col-md-offset-1">
                            <label class="col-md-3 control-label">固定左列数</label>
                            <div class="col-md-3">
                              <input id="fixed_left" type="text" class="form-control">
                            </div>
                            <label class="control-label col-md-3">固定右列数</label>
                            <div class="col-md-3">
                              <input id="fixed_right" type="text" class="form-control">
                            </div>
                          </div>

                        </div>
                        <div class="form-actions">
                          <div class="row">
                            <div class="col-md-offset-4 col-md-8">
                              <button id="apply-style" type="submit" data-dismiss="modal"
                                      class="btn btn-circle blue-madison">设置
                              </button>
                              <button type="button" data-dismiss="modal"
                                      class="btn btn-circle default">关闭
                              </button>
                            </div>
                          </div>
                        </div>
                      </form>

                    </div>

                  </div>
                  <!-- 表格配置对话框 end-->

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

            <#include "footer.ftl">
</div>


<!-- BEGIN CORE PLUGINS -->
<script src="${request.contextPath}/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${request.contextPath}/global/plugins/typeahead/handlebars.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/typeahead/typeahead.bundle.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/jquery.dataTables.js" type="text/javascript" language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.bootstrap.min.js" type="text/javascript" language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.fixedColumns.min.js" type="text/javascript"
        language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/scroller.bootstrap.min.js" type="text/javascript" language="javascript"></script>
<script src="${request.contextPath}/global/plugins/datatables/js/dataTables.scroller.min.js" type="text/javascript" language="javascript"></script>


<script src="${request.contextPath}/global/plugins/bootstrap-notify/bootstrap-notify.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/sortable/Sortable.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.iframe-transport.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/sweetalert2/sweetalert2.min.js" type="text/javascript"></script>

<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/pages/scripts/query.patent.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/message.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script type="text/javascript">
  var http_request = "${request.contextPath}";

</script>
</body>

</html>