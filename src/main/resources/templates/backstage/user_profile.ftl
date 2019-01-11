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
  <link href="${request.contextPath}/global/plugins/img-cropping/css/cropper.min.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/global/plugins/img-cropping/css/img-cropping.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/global/plugins/typeahead/typeahead.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/pages/css/profile.min.css" rel="stylesheet" type="text/css"/>
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

          <!-- BEGIN PAGE CONTENT BODY -->

          <div class="page-content">
            <div class="container">
              <div class="page-content-inner">
                <div class="profile">
                  <div class="tabbable-line tabbable-full-width">
                    <ul class="nav nav-tabs">

                      <li>
                        <a href="#tab_1" data-toggle="tab"> 个人资料 </a>
                      </li>
                      <li>
                        <a href="#tab_2" data-toggle="tab"> 首页配置 </a>
                      </li>
                    </ul>
                    <div class="tab-content">
                      <input id="userID" type="hidden" value="${user.id}">
                      <!--tab_1_2-->
                      <div class="tab-pane active" id="tab_1">
                        <div class="row profile-account">
                          <div class="col-md-3">
                            <ul class="ver-inline-menu tabbable list-unstyled profile-nav">

                              <li class="active">
                                <a data-toggle="tab" href="#tab_1-1">
                                  <i class="fa fa-cog"></i> 个人信息 </a>
                                <span class="after"> </span>
                              </li>
                              <li>
                                <a data-toggle="tab" href="#tab_1-2">
                                  <i class="fa fa-picture-o"></i> 修改头像 </a>
                              </li>
                              <li>
                                <a data-toggle="tab" href="#tab_1-3">
                                  <i class="fa fa-lock"></i> 修改密码 </a>
                              </li>

                            </ul>
                          </div>
                          <div class="col-md-9">
                            <div class="tab-content">
                              <div id="tab_1-1" class="tab-pane active">
                                <form role="form" action="${request.contextPath}/updateUserInfo" method="post">
                                  <div class="form-group">
                                    <label class="control-label">账户名</label>
                                    <input name="id" type="hidden" value="${user.id}">
                                    <input name="account" type="text" class="form-control" value="${user.account}"/></div>
                                  <div class="form-group">
                                    <label class="control-label">姓名</label>
                                    <input name="name" type="text" class="form-control" value="${user.name}"/></div>
                                  <div class="form-group">
                                    <label class="control-label">性别</label>
                                    <select name="sex" class="form-control">
                                      <option value="0" <#if user.sex == 0>selected</#if>>保密</option>
                                      <option value="1" <#if user.sex == 1>selected</#if>>男</option>
                                      <option value="2" <#if user.sex == 2>selected</#if>>女</option>
                                    </select></div>
                                  <div class="form-group">
                                    <label class="control-label">手机号码</label>
                                    <input name="phone" type="text" class="form-control" value="${user.phone}"/></div>
                                  <div class="form-group">
                                    <label class="control-label">电子邮件</label>
                                    <input name="email" type="text" class="form-control" value="${user.email}"/></div>
                                  <div class="form-group">
                                    <label class="control-label">微信号</label>
                                    <input name="wechat" type="text" class="form-control" value="${user.wechat}"/></div>
                                  <div class="form-group">
                                    <label class="control-label">个人简介</label>
                                    <textarea name="introduction" class="form-control"
                                              rows="3">${user.introduction}</textarea>
                                  </div>

                                  <div class="margiv-top-10">
                                    <input type="submit" class="btn green" value="修改">
                                  </div>
                                </form>
                              </div>
                              <div id="tab_1-2" class="tab-pane">
                                <div class="black-cloth" onclick="closeTailor(this)"></div>
                                <div class="tailoring-content">
                                  <div class="tailoring-content-one">
                                    <label title="上传图片" for="chooseImg" class="btn green choose-btn">
                                      <input type="file" accept="image/jpg,image/jpeg,image/png" name="file" id="chooseImg"
                                             class="hidden">选择图片
                                    </label>
                                    <div class="close-tailoring" onclick="closeTailor(this)">×</div>
                                  </div>
                                  <div class="tailoring-content-two">
                                    <div class="tailoring-box-parcel">
                                      <img id="tailoringImg">
                                    </div>
                                    <div class="preview-box-parcel">
                                      <div class="square previewImg"></div>
                                      <div class="circular previewImg"></div>
                                    </div>
                                  </div>
                                  <div class="tailoring-content-three">
                                    <button class="btn green cropper-reset-btn">复位</button>
                                    <button class="btn green cropper-rotate-btn">旋转</button>
                                    <button class="btn green cropper-scaleX-btn">换向</button>
                                    <button class="btn green sureCut" id="sureCut">确定</button>
                                  </div>
                                </div>
                              </div>
                              <div id="tab_1-3" class="tab-pane">
                                <#if error?exists>
                                  <div class="alert alert-danger">
                                    <span>${error}</span>
                                  </div>
                                </#if>
                                <form id="changePassword" role="form" action="${request.contextPath}/changePassword"
                                      method="post">
                                  <div class="form-group">
                                    <label class="control-label">当前密码</label>
                                    <input name="cur_password" type="password" class="form-control"/></div>
                                  <div class="form-group">
                                    <label class="control-label">新密码</label>
                                    <input name="password" type="password" class="form-control"/></div>
                                  <div class="form-group">
                                    <label class="control-label">重新输入新密码</label>
                                    <input name="confirm_password" type="password" class="form-control"/></div>
                                  <div class="margin-top-10">
                                    <input type="submit" class="btn green" value="修改">
                                  </div>
                                </form>
                              </div>


                              </div>
                            </div>
                          </div>
                          <!--end col-md-9-->
                        </div>

                      <!--end tab-pane-->
                      <div class="tab-pane" id="tab_2">
                        <div class="row profile-account">
                          <div class="col-md-3">
                            <ul class="ver-inline-menu tabbable list-unstyled profile-nav">

                              <li class="active">
                                <a data-toggle="tab" href="#tab_2-1">
                                  <i class="fa fa-cog"></i> 数据图表 </a>
                                <span class="after"> </span>
                              </li>
                              <li>
                                <a data-toggle="tab" href="#tab_2-2">
                                  <i class="fa fa-lock"></i> 数据监控 </a>
                              </li>

                            </ul>
                          </div>
                          <div class="col-md-9">
                            <div class="tab-content">
                              <div id="tab_2-1" class="tab-pane active">

                              </div>
                              <div id="tab_2-2" class="tab-pane">

                              </div>
                            </div>
                          </div>
                          <!--end col-md-9-->
                        </div>
                      </div>
                      <!--end tab-pane-->
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
<script src="${request.contextPath}/global/plugins/jquery-validation/jquery.validate.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-validation/additional-methods.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-validation/localization/messages_zh.min.js"
        type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/global/plugins/img-cropping/js/cropper.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/pages/scripts/user.profile.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/message.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script>
  var http_request = "${request.contextPath}";
</script>
</body>

</html>