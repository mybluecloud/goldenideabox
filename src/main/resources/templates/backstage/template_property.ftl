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
  <link href="${request.contextPath}/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"
        type="text/css"/>
  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN PAGE LEVEL PLUGINS -->
  <link href="${request.contextPath}/global/plugins/typeahead/typeahead.css" rel="stylesheet" type="text/css" />
  <!-- END PAGE LEVEL PLUGINS -->
  <!-- BEGIN THEME GLOBAL STYLES -->
  <link href="${request.contextPath}/global/css/components.css" rel="stylesheet"
        id="style_components" type="text/css"/>
  <link href="${request.contextPath}/global/css/plugins.css" rel="stylesheet" type="text/css"/>
  <!-- END THEME GLOBAL STYLES -->
  <!-- BEGIN THEME LAYOUT STYLES -->
  <link href="${request.contextPath}/layouts/layout/backstage/css/layout.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/layouts/layout/backstage/css/themes/default.css" rel="stylesheet"
        type="text/css" id="style_color"/>
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
                  <#if id??>
                    <small>编辑模板</small>
                  <#else>
                    <small>新建模板</small>
                  </#if>
                </h1>
              </div>
              <!-- END PAGE TITLE -->

            </div>
          </div>
          <!-- END PAGE HEAD-->
          <!-- BEGIN PAGE CONTENT BODY -->

          <div class="page-content-inner">
            <div class="container-fluid">
              <div class="portlet box blue-dark">
                <div class="portlet-title">
                  <div class="caption">
                    <i class="fa fa-gift"></i>模板数据
                  </div>
                </div>
                <div class="portlet-body form">
                  <div class="form-body">
                    <div class="form-group">
                      <form id="template" class="mt-repeater form" method="post">
                        <h3 class="mt-repeater-title">模板信息</h3>
                        <div class="form-group col-md-12">
                          <div class="mt-item">
                            <#if id??>
                            <input type="hidden" id="templateID" value="${id}"/>
                            <#else>
                            <input type="hidden" id="templateID" value="0"/>
                            </#if>

                            <div class="control-group col-md-4">
                              <label class="control-label"><strong>名称</strong></label>
                              <input type="text" id="name" class="form-control"
                                     placeholder="模板名称"/></div>
                            <div class="control-group col-md-4">
                              <label class="control-label"><strong>描述</strong></label>
                              <textarea id="description" class="form-control" rows="2"
                                        placeholder="模板描述"></textarea>
                            </div>
                          </div>
                        </div>
                        <h3 class="mt-repeater-title">模板属性</h3>
                        <div id="group-a">

                        </div>
                        <div class="">
                          <button type="button" onclick="TemplateProperty.addProperty()"
                                  class="btn btn-circle btn-success mt-repeater-add">
                            <i class="fa fa-plus"></i> 添加
                          </button>
                        </div>
                        <div class="form-actions">
                          <div class="row">
                            <div class="col-md-offset-4 col-md-8">
                              <button type="button" class="btn btn-circle blue-madison"
                                      onclick="TemplateProperty.save()">保存
                              </button>
                              <a href="${request.contextPath}/template" type="button"
                                 class="btn btn-circle grey-salsa btn-outline">
                                取消
                              </a>
                            </div>
                          </div>
                        </div>
                      </form>
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${request.contextPath}/global/plugins/typeahead/handlebars.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/typeahead/typeahead.bundle.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-notify/bootstrap-notify.min.js"
        type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/layouts/global/scripts/common.js"
        type="text/javascript"></script>

<script src="${request.contextPath}/pages/scripts/template.property.js"
        type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/message.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script type="text/javascript">
  var http_request = "${request.contextPath}";
</script>
</body>

</html>