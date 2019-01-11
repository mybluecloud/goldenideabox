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
  <link href="${request.contextPath}/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css"
      rel="stylesheet" type="text/css"/>
  <link
      href="${request.contextPath}/global/plugins/bootstrap-editable/bootstrap-editable/css/bootstrap-editable.css"
      rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/bootstrap-wysihtml5/bootstrap-wysihtml5.css"
        rel="stylesheet" type="text/css"/>
  <link
      href="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/address/address.css"
      rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css"
        rel="stylesheet" type="text/css"/>
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
        type="text/css"
        id="style_color"/>
  <!-- END THEME LAYOUT STYLES -->
  <script type="text/javascript">
    var http_request = "${request.contextPath}";
  </script>
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
                  <#if id??>
                    <small>编辑案件</small>
                  <#else>
                    <small>新建案件</small>
                  </#if>
                </h1>
              </div>
              <!-- END PAGE TITLE -->

            </div>
          </div>
          <!-- END PAGE HEAD-->
          <!-- BEGIN PAGE CONTENT BODY -->
          <#if patent??>
            <input type="hidden" id="patentID" value="${patent.id?c}"/>
          <#else>
            <input type="hidden" id="patentID" value="0"/>
          </#if>
          <div class="page-content-inner">
            <div class="container-fluid">
              <div class="page-content-inner">
                <div class="portlet light portlet-fit ">
                  <div class="portlet-title form-group">
                    <label class=" col-md-1"><strong>模板</strong></label>
                    <div class="col-md-2">
                      <select id="template" class="form-control ">
                      <#if templates?exists>
                            <#list templates as template>
                              <option value="${template.id}">${template.name}</option>
                            </#list>
                      </#if>
                      </select>

                    </div>

                  <#--<button id="enable" class="btn green">查看/编辑</button>-->
                  </div>
                  <div class="portlet-body">
                    <table class="table table-bordered table-striped"
                           style="background-color:#e5e5e5">
                      <tbody id="property">
                      </tbody>
                    </table>
                    <div class="note note-info col-md-6">
                    <#if records?exists>
                      <#list records as record>
                              <label >${(record.updateTime?string("yyyy-MM-dd HH:mm:ss"))!}&nbsp;&nbsp;${record.reocrd}</label><br>
                      </#list>
                    </#if>
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
<script src="${request.contextPath}/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>


<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/bootstrap-wysihtml5/wysihtml5-0.3.0.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/bootstrap-wysihtml5/bootstrap-wysihtml5.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/bootstrap-wysihtml5/locales/bootstrap-wysihtml5.zh-CN.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/bootstrap-editable/js/bootstrap-editable.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/address/address.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/wysihtml5.js" type="text/javascript"></script>

<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.iframe-transport.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload-validate.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/pages/scripts/patent.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/message.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->

</body>

</html>