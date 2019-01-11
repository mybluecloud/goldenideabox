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

  <link href="${request.contextPath}/global/plugins/bootstrap/css/bootstrap.min.css"
        rel="stylesheet" type="text/css"/>

  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN PAGE LEVEL PLUGINS -->

  <!-- END PAGE LEVEL PLUGINS -->
  <!-- BEGIN THEME GLOBAL STYLES -->
  <link href="${request.contextPath}/global/css/components.css" rel="stylesheet"
        id="style_components" type="text/css"/>
  <link href="${request.contextPath}/global/css/plugins.min.css" rel="stylesheet" type="text/css"/>
  <!-- END THEME GLOBAL STYLES -->
  <!-- BEGIN PAGE LEVEL STYLES -->
  <link href="${request.contextPath}/pages/css/login.css" rel="stylesheet" type="text/css"/>
  <!-- END PAGE LEVEL STYLES -->
  <!-- BEGIN THEME LAYOUT STYLES -->
  <!-- END THEME LAYOUT STYLES -->

</head>
<!-- END HEAD -->

<body class=" login">
<!-- BEGIN : LOGIN PAGE 5-1 -->
<div class="user-login">
  <div class="row bs-reset">
    <div class="col-md-6 bs-reset mt-login-bsfix">
      <div class="login-bg"
           style="background-image:url(${request.contextPath}/pages/img/bg1.jpg)">
        <img class="login-logo" src="${request.contextPath}/pages/img/logo.login.png"/></div>
    </div>
    <div class="col-md-6 login-container bs-reset mt-login-bsfix">
      <div class="login-content">
        <h1>萃智知识产权管理平台</h1>
        <p> 分配及管理和跟踪案件。第一时间获取案件的最新动态。 </p>
          <form action="${request.contextPath}/login" class="login-form" method="post">
          <#if msg?exists>
            <div class="alert alert-danger">

              <span>${msg}</span>
            </div>
          </#if>
          <div class="row">
            <div class="col-xs-4">
              <input class="form-control form-control-solid placeholder-no-fix form-group"
                     type="text" autocomplete="off" placeholder="账户"  name="username"
                     required/></div>
            <div class="col-xs-4">
              <input class="form-control form-control-solid placeholder-no-fix form-group"
                     type="password" autocomplete="off" placeholder="密码"
                     name="password" required/></div>
            <div class="col-xs-2">
              <img type="image" src="${request.contextPath}/captcha/kaptcha.jpg" id="codeImage" onclick="chageCode()"
                   style="cursor:pointer;" width="100" height="35"/></div>
            <div class="col-xs-2">
              <input class="form-control form-control-solid placeholder-no-fix form-group"
                     type="text" autocomplete="off" placeholder="验证码" id="captcha"
                     name="captcha" required/></div>
          </div>

          <div class="row">
            <div class="col-sm-4">
              <div class="rem-password">
                <label class="rememberme mt-checkbox mt-checkbox-outline">
                  <input type="checkbox" name="rememberMe" value="1" /> 记住我
                  <span></span>
                </label>
              </div>
            </div>
            <div class="col-sm-8 text-right">
              <div class="forgot-password">
                <a href="javascript:;" id="regedit" class="regedit">没有账户?</a>
              </div>
              <button class="btn green" type="submit">登录</button>
            </div>
          </div>
        </form>
        <!-- BEGIN FORGOT PASSWORD FORM -->
        <form id="regeditForm" action="${request.contextPath}/regedit" class="form regedit-form" method="post">
          <h3 style="color:#32c5d2!important">用户注册</h3>
          <div class="form-body">

            <div class="form-group form-md-line-input form-md-floating-label">
              <input type="text" class="form-control input-sm" name="account" id="account">
              <label for="account"> 用户名 </label>
              <span class="help-block">请输入用户名</span>
            </div>
            <div class="form-group form-md-line-input form-md-floating-label">
              <input type="text" class="form-control input-sm" name="name" id="name">
              <label for="name"> 姓名 </label>
              <span class="help-block">请输入姓名</span>
            </div>
            <div class="form-group form-md-line-input form-md-floating-label">
              <input type="password" class="form-control input-sm" name="password" id="password">
              <label for="password"> 密码 </label>
              <span class="help-block">请输入密码</span>
            </div>
            <div class="form-group form-md-line-input form-md-floating-label">
              <input type="password" class="form-control input-sm" name="confirm_password" id="confirm_password">
              <label for="confirm_password"> 确认密码 </label>
              <span class="help-block">请再次输入密码</span>
            </div>

            <div class="form-actions">
              <button type="button" id="back-btn" class="btn green btn-outline">返回</button>
              <button type="submit" class="btn btn-success uppercase pull-right">注册</button>
            </div>
          </div>
        </form>
        <!-- END FORGOT PASSWORD FORM -->
      </div>
      <div class="login-footer">
        <div class="row bs-reset">

          <div class="col-xs-7 bs-reset">
            <div class="login-copyright text-right">
              <p>2018 &copy; 萃智知识产权服务平台 &nbsp;|&nbsp;版权所有 &nbsp;|&nbsp;陕ICP备17002496号</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!-- END : LOGIN PAGE 5-1 -->

<!-- BEGIN CORE PLUGINS -->
<script src="${request.contextPath}/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"
        type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${request.contextPath}/global/plugins/jquery-validation/jquery.validate.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-validation/additional-methods.min.js"
        type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-validation/localization/messages_zh.min.js"
    type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/backstretch/jquery.backstretch.min.js"
        type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/pages/scripts/login.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<!-- END THEME LAYOUT SCRIPTS -->
<script type="text/javascript">
  var http_request = "${request.contextPath}";
  function chageCode(){
    document.getElementById("codeImage").src= http_request + "/captcha/kaptcha.jpg?"+Math.random();
  }
</script>
</body>

</html>