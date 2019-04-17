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
  <link href="${request.contextPath}/global/plugins/datatables/css/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet"
        type="text/css"/>
  <!-- END PAGE LEVEL PLUGINS -->
  <!-- BEGIN THEME GLOBAL STYLES -->
  <link href="${request.contextPath}/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css"/>
  <link href="${request.contextPath}/global/css/plugins.css" rel="stylesheet" type="text/css"/>
  <!-- END THEME GLOBAL STYLES -->
  <!-- BEGIN THEME LAYOUT STYLES -->
  <link href="${request.contextPath}/layouts/layout/backstage/css/layout.css" rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/layouts/layout/backstage/css/themes/default.css" rel="stylesheet" type="text/css"
        id="style_color"/>
  <!-- END THEME LAYOUT STYLES -->
  <style>
    .div_point-1 {
      background-image: url("${request.contextPath}/layouts/layout/backstage/img/click_1.png");
      background-position: center;
      background-size: 32px 32px;
    }

    .div_point-2 {
      background-image: url("${request.contextPath}/layouts/layout/backstage/img/click_2.png");
      background-position: center;
      background-size: 32px 32px;
    }

    .div_point-3 {
      background-image: url("${request.contextPath}/layouts/layout/backstage/img/click_3.png");
      background-position: center;
      background-size: 32px 32px;
    }

    .div_icon-point {
      position: absolute;
      width: 32px;
      height: 32px;
      cursor: pointer;
      background-repeat: no-repeat;
    }

    .table-actions-wrapper {
      padding-bottom: 20px
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
                      id="cpquerylist">
                  </table>


                  <!-- 创建任务列表 begin-->
                  <div id="task_dialog" class="portlet box blue-hoki modal fade">
                    <div class="portlet-title">
                      <div class="caption" id="title">任务配置
                        <i class="fa fa-gift"></i>
                      </div>

                    </div>
                    <div class="portlet-body form">
                      <form id="taskInfo" action="#" class="form-horizontal">
                        <div class="form-body">
                          <input id="task_id" type="hidden" class="form-control" value="0">
                          <div class="form-group">
                            <label class="col-md-3 control-label">账号</label>
                            <div class="col-md-6">
                              <input id="account" name="account" type="text" class="form-control">
                            </div>
                            <label for="account"></label>
                          </div>
                          <div class="form-group">
                            <label class="col-md-3 control-label">密码</label>
                            <div class="col-md-6">
                              <input id="password" name="password" type="password" class="form-control">
                            </div>
                            <label for="password"></label>
                          </div>
                          <div class="form-group">
                            <label class="col-md-3 control-label">模板</label>
                            <div class="col-md-6">
                              <select id="template" name="template" class="form-control" required>
                                <option value="">请选择</option>
                              </select>
                            </div>
                            <label for="template"></label>
                          </div>
                          <div class="form-group">
                            <label class="col-md-3 control-label">匹配字段</label>
                            <div class="col-md-6">
                              <select id="matchFiled" name="matchFiled" class="form-control" required>
                                <option value="">请选择</option>
                              </select>
                            </div>
                            <label for="matchFiled"></label>
                          </div>
                          <div class="form-group">
                            <label class="col-md-3 control-label">匹配字符</label>
                            <div class="col-md-6">
                              <input id="matchChar" name="matchChar" type="text" class="form-control" placeholder="多个条件可用竖线隔开，比如a|b">
                            </div>
                            <label for="matchChar"></label>
                          </div>
                          <div class="form-group">
                            <label class="col-md-3 control-label">申请号字段</label>
                            <div class="col-md-6">
                              <select id="anFiled" name="anFiled" class="form-control" required>
                                <option value="">请选择</option>
                              </select>
                            </div>
                            <label for="anFiled"></label>
                          </div>
                          <div class="form-group">
                            <label class="col-md-3 control-label">官方状态字段</label>
                            <div class="col-md-6">
                              <select id="statusFiled" name="statusFiled" class="form-control" required>
                                <option value="">请选择</option>
                              </select>
                            </div>
                            <label for="statusFiled"></label>
                          </div>


                        </div>
                        <div class="form-actions">
                          <div class="row">
                            <div class="col-md-offset-4 col-md-8">
                              <button type="submit" onclick="saveTask()"
                                      class="btn btn-circle blue-madison">确定
                              </button>
                              <button type="button" data-dismiss="modal"
                                      class="btn btn-circle default">取消
                              </button>
                            </div>
                          </div>
                        </div>
                      </form>

                    </div>
                  </div>
                  <!--创建任务列表 end-->
                  <!--显示验证码 begin-->
                  <div id="captcha_dialog" class="portlet box blue-hoki modal fade">
                    <div class="portlet-body">
                      <div class="row">
                        <div class="col-md-8 ">
                          <div id="imgyzm_dz" style=" bottom: 30px; ">
                            <img id="jcaptchaimage" src="" style="border-radius: 4px;">

                            <div id="reload_dz" title="刷新" style="position: absolute; bottom: 260px; left: 280px; height: 0px;">
                              <img class="img_reload_dz" src="${request.contextPath}/layouts/layout/backstage/img/reload.png"
                                   style="border-radius: 4px;">
                            </div>
                          </div>
                          <div class="selectyzm_tips_dz" style="line-height: 35px;position:relative;">
                            <span id="selectyzm_text_dz" style="color: rgb(144, 144, 144);"></span>
                            <input type="hidden" id="id">
                          </div>
                        </div>
                        <div class="col-md-4 ">
                          <div class="md-radio-inline">
                            <div class="md-radio has-info">
                              <input type="radio" id="wholeCase" name="caseRadio" class="md-radiobtn" value="true" checked>
                              <label for="wholeCase">
                                <span></span>
                                <span class="check"></span>
                                <span class="box"></span> 全部案件 </label>
                            </div>
                            <div class="md-radio has-error">
                              <input type="radio" id="failCase" name="caseRadio" class="md-radiobtn" value="false" >
                              <label for="failCase">
                                <span></span>
                                <span class="check"></span>
                                <span class="box"></span> 失败案件 </label>
                            </div>
                          </div>
                          <div class="form-group form-md-checkboxes">

                            <div class="md-checkbox-list">
                              <div class="md-checkbox has-info">
                                <input type="checkbox" id="application" disabled class="md-check" checked>
                                <label for="application">
                                  <span></span>
                                  <span class="check"></span>
                                  <span class="box"></span> 申请信息 </label>
                              </div>
                              <div class="md-checkbox has-error">
                                <input type="checkbox" id="review" class="md-check">
                                <label for="review">
                                  <span></span>
                                  <span class="check"></span>
                                  <span class="box"></span> 审查信息 </label>
                              </div>
                              <div class="md-checkbox has-warning">
                                <input type="checkbox" id="cost" class="md-check">
                                <label for="cost">
                                  <span></span>
                                  <span class="check"></span>
                                  <span class="box"></span> 费用信息 </label>
                              </div>
                              <div class="md-checkbox has-success">
                                <input type="checkbox" id="post" class="md-check">
                                <label for="post">
                                  <span></span>
                                  <span class="check"></span>
                                  <span class="box"></span> 发文信息 </label>
                              </div>
                              <div class="md-checkbox has-info">
                                <input type="checkbox" id="announce" class="md-check">
                                <label for="announce">
                                  <span></span>
                                  <span class="check"></span>
                                  <span class="box"></span> 公布公告 </label>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>


                    </div>
                  </div>
                  <!--显示验证码 end-->
                </div>
              </div>
              <div class="portlet light">
                <div class="portlet-title">
                  <div class="caption font-green-sharp">
                    <i class="icon-share font-green-sharp"></i>
                    <span class="caption-subject bold uppercase"> 查询记录</span>

                  </div>
                </div>
                <div class="portlet-body">
                  <div class="table-container">
                    <table
                        class="table table-striped table-bordered table-hover table-checkable order-column"
                        id="recordlist">
                      <thead></thead>
                      <tbody></tbody>
                      <tfoot>
                      <tr>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                      </tr>
                      </tfoot>
                    </table>
                  </div>

                </div>
              </div>

              <div class="note note-info col-md-12">
                    <#if logs?exists>
                      <#list logs as log>
                              <label>${(log.createTime?string("yyyy-MM-dd HH:mm:ss"))!}&nbsp;&nbsp;${log.content}</label><br>
                      </#list>
                    </#if>
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
<script src="${request.contextPath}/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-validation/jquery.validate.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-validation/additional-methods.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/jquery-validation/localization/messages_zh.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>

<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/common.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/base64.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/pages/scripts/cpquery.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js" type="text/javascript"></script>
<script src="${request.contextPath}/layouts/global/scripts/message.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script>
  var http_request = "${request.contextPath}";
  var datas = [];
  <#if templates??>
    <#list templates as template>
    var data = {};
    data.id = "${template.id}";
    data.name = "${template.name}";
    var property = {};
      <#if template.properties??>
        <#list template.properties as pty>
        property["${pty.sortId}"] = "${pty.name}";
        </#list>
      </#if>

    data.properties = property;
    datas[data.id] = data;
    </#list>
  </#if>

</script>
</body>

</html>