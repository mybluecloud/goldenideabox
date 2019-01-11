<!DOCTYPE html>

<html lang="en">
<!-- BEGIN HEAD -->

<head>
  <meta charset="utf-8"/>
  <title>Metronic Admin Theme #4 | Blank Page Layout</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta content="width=device-width, initial-scale=1" name="viewport"/>
  <meta content="Preview page of Metronic Admin Theme #4 for blank page layout" name="description"/>
  <meta content="" name="author"/>
  <!-- BEGIN GLOBAL MANDATORY STYLES -->
  <link href="${request.contextPath}/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN THEME GLOBAL STYLES -->
  <link href="${request.contextPath}/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css"/>
  <link href="${request.contextPath}/global/css/plugins.min.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/typeahead/typeahead.css" rel="stylesheet" type="text/css" />
  <!-- END THEME GLOBAL STYLES -->
  <!-- BEGIN THEME LAYOUT STYLES -->
  <link href="${request.contextPath}/layouts/layout/frontstage/css/layout.min.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/layouts/layout/frontstage/css/themes/default.min.css" rel="stylesheet" type="text/css"
        id="style_color"/>

  <!-- END THEME LAYOUT STYLES -->
  <link rel="shortcut icon" href="favicon.ico"/>
</head>
<!-- END HEAD -->

<body class="page-container-bg-solid page-header-fixed page-sidebar-closed-hide-logo page-boxed page-header-fixed">
<!-- BEGIN HEADER -->
<#include "header.ftl">
<!-- END HEADER -->
<!-- BEGIN HEADER & CONTENT DIVIDER -->
<div class="clearfix"></div>
<div class="container">
  <!-- BEGIN CONTAINER -->
  <div class="page-container">
    <!-- BEGIN SIDEBAR -->
  <#include "menu.ftl">
    <!-- END SIDEBAR -->
    <!-- BEGIN CONTENT -->
    <div class="page-content-wrapper">
      <!-- BEGIN CONTENT BODY -->
      <div class="page-content">
        <!-- BEGIN PAGE HEAD-->
        <div class="page-head">
          <!-- BEGIN PAGE TITLE -->
          <div class="page-title">

          </div>
          <!-- END PAGE TITLE -->

        </div>
        <!-- END PAGE HEAD-->
        <!-- BEGIN PAGE BREADCRUMB -->

        <!-- END PAGE BREADCRUMB -->
        <!-- BEGIN PAGE BASE CONTENT -->
        <div class="note note-info">
          <p> A black page template with a minimal dependency assets to use as a base for any custom page you create </p>
        </div>
        <!-- END PAGE BASE CONTENT -->
      </div>
      <!-- END CONTENT BODY -->
    </div>
    <!-- END CONTENT -->
  <#include "../common/sidebar.ftl">
<#include "footer.ftl">
  </div>
  <!-- END CONTAINER -->
</div>
<!-- END HEADER & CONTENT DIVIDER -->



<!-- BEGIN CORE PLUGINS -->
<script src="${request.contextPath}/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/typeahead/handlebars.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/typeahead/typeahead.bundle.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/frontstage/scripts/layout.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/message.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script>
  var http_request ="${request.contextPath}";
</script>
</body>

</html>