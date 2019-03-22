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
  <link href="${request.contextPath}/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet"
        type="text/css"/>
  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN PAGE LEVEL PLUGINS -->
  <link href="${request.contextPath}/global/plugins/typeahead/typeahead.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-editable/bootstrap-editable/css/bootstrap-editable.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/bootstrap-wysihtml5/bootstrap-wysihtml5.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/address/address.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jstree/themes/default/style.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/flipbook/css/basic.css" rel="stylesheet"
        type="text/css"/>

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
            <input type="hidden" id="orderID" value="${orderID}"/>
          </#if>
          <div class="page-content-inner">
            <div class="container-fluid">
              <div class="page-content-inner">
                <div class="portlet light portlet-fit ">
                  <div class="portlet-title tabbable-line">
                    <div class="caption">
                      <i class="icon-pin font-yellow-crusta"></i>
                      <span class="caption-subject bold font-yellow-crusta uppercase"> 案件信息 </span>
                    </div>
                    <#if applicationNumber??>
                    <input type="hidden" id="applicationNumber" value="${applicationNumber}">
                    <ul id="tabs" class="nav nav-tabs">
                      <li class="active">
                        <a href="#portlet_tab_base" data-toggle="tab"> 基本信息 </a>
                      </li>
                      <li>
                        <a id="application" href="#portlet_tab_application" data-toggle="tab"> 申请信息 </a>
                      </li>
                      <li>
                        <a id="review" href="#portlet_tab_review" data-toggle="tab"> 审查信息 </a>
                      </li>
                      <li>
                        <a id="cost" href="#portlet_tab_cost" data-toggle="tab"> 费用信息 </a>
                      </li>
                      <li>
                        <a id="post" href="#portlet_tab_post" data-toggle="tab"> 发文信息 </a>
                      </li>
                      <li>
                        <a id="announce" href="#portlet_tab_announce" data-toggle="tab"> 公布公告 </a>
                      </li>
                    </ul>
                    </#if>
                  </div>
                  <div class="portlet-body">
                    <div class="tab-content">
                      <div class="tab-pane active" id="portlet_tab_base">
                        <form class="form-inline">
                          <span class=""><strong> 模板 </strong></span>
                          <#if id??>
                          <select id="template" style="background-color: grey;" class="form-control" disabled>
                          <#else>
                          <select id="template" class="form-control">
                          </#if>
                              <#if templates?exists>
                                <#list templates as template>
                                  <#if patent??>
                                    <#if template.id == patent.templateId>
                                      <option value="${template.id}" selected>${template.name}</option>
                                    <#else>
                                      <option value="${template.id}">${template.name}</option>
                                    </#if>
                                  <#else>
                                    <option value="${template.id}">${template.name}</option>
                                  </#if>

                                </#list>
                              </#if>
                          </select>
                          <button id="enable" class="btn red pull-right" type="button">编辑</button>
                        </form>
                        <hr>
                        <table id="patent" class="table table-bordered table-striped"
                               style="background-color:#e5e5e5">
                          <tbody id="property">
                          </tbody>
                        </table>
                        <div class="note note-info col-md-6">
                        <#if records?exists>
                          <#list records as record>
                                  <label>${(record.updateTime?string("yyyy-MM-dd HH:mm:ss"))!}&nbsp;&nbsp;${record.reocrd}</label><br>
                          </#list>
                        </#if>
                        </div>
                      </div>
                      <#if applicationNumber??>
                        <#include "patent_cpquery.ftl">
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
<script src="${request.contextPath}/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${request.contextPath}/global/plugins/typeahead/handlebars.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/typeahead/typeahead.bundle.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jstree/jstree.min.js" type="text/javascript"></script>

<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/bootstrap-wysihtml5/wysihtml5-0.3.0.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/bootstrap-wysihtml5/bootstrap-wysihtml5.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/bootstrap-wysihtml5/locales/bootstrap-wysihtml5.zh-CN.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/bootstrap-editable/js/bootstrap-editable.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/address/address.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-editable/inputs-ext/wysihtml5/wysihtml5.js" type="text/javascript"></script>

<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.iframe-transport.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-file-upload/js/jquery.fileupload-validate.js" type="text/javascript"></script>

<script src="${request.contextPath}/global/plugins/flipbook/js/turn.js"
        type="text/javascript"></script>


<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/pages/scripts/patent.js" type="text/javascript"></script>
<script src="${request.contextPath}/pages/scripts/patent.cpquery.js" type="text/javascript"></script>
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