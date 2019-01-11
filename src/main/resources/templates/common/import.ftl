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
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css"
        rel="stylesheet" type="text/css"/>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/simple-line-icons/2.4.0/css/simple-line-icons.min.css" rel="stylesheet"
        type="text/css"/>
  <link href="${request.contextPath}/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
  <!-- END GLOBAL MANDATORY STYLES -->
  <!-- BEGIN PAGE LEVEL PLUGINS -->
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload.css"
        rel="stylesheet" type="text/css"/>
  <link href="${request.contextPath}/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css"
        rel="stylesheet" type="text/css"/>
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

    ul {
      width: 200px;

      list-style: none;
    }

    ul li {
      list-style: none;
      width: 200px;
      height: 25px;
      text-align: center;
      float: left;
      border: 2px solid #999;
      margin: 0 0 -2px -2px;
      position: relative;
      z-index: 0;
    }
    .title {

      list-style: none;
      width: 200px;
      height: 25px;
      text-align: center;
      float: left;
      border: 2px solid #999;
      margin: 0 0 -2px 38px;
      position: relative;
      z-index: 0;
    }
    .prop-handle {
      background-color: #65d4c5;
    }

    .title-handle {
      background-color: #9acb6f;
    }

  </style>
</head>
<!-- END HEAD -->

<body class="page-container-bg-solid">
<div class="page-wrapper">

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
                <div class="col-md-12">
                  <select class="form-control input-sm" id="template">
                          <#if templates?exists>
                            <#list templates as template>
                              <option value="${template.id}">${template.name}</option>
                            </#list>
                          </#if>
                  </select>
                </div>

              </div>
              <!-- END PAGE TITLE -->

            </div>
          </div>
          <!-- END PAGE HEAD-->
          <!-- BEGIN PAGE CONTENT BODY -->

          <div class="page-content-inner">
            <div class="container-fluid">
              <div class="portlet light ">
                <div class="portlet-title ">
                  <span id="btn-upload" class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>选择文件</span>
                    <input id="fileupload" type="file" name="files[]"  multiple>
                  </span>
                  <button id="import" type="button" class="btn  btn-success " style="display: none">
                    <i class="fa fa-sign-in"></i> 开始导入
                  </button>
                </div>
                <div class="portlet-body">
                  <div id="files" ></div>
                  <div style="float: left; ">
                    <div class="title">属性</div>
                    <ul id="ul-prop">
                    </ul>
                  </div>
                  <div style=" float: left; ">
                    <div class="title">excel</div>
                    <ul id="ul-title">
                    </ul>
                  </div>


                </div>
              </div>

            </div>
          </div>


          <!-- END PAGE CONTENT BODY -->
          <!-- END CONTENT BODY -->
        </div>
        <!-- END CONTENT -->

      </div>
      <!-- END CONTAINER -->
    </div>
  </div>

</div>


<!-- BEGIN CORE PLUGINS -->
<script src="${request.contextPath}/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${request.contextPath}/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->

<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${request.contextPath}/global/scripts/app.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${request.contextPath}/global/plugins/sortable/Sortable.min.js"
        type="text/javascript"></script>
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
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${request.contextPath}/layouts/layout/backstage/scripts/layout.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script type="text/javascript">
  jQuery(document).ready(function () {
    var sortable;
    var file;


    $('#fileupload').fileupload({
      url: "${request.contextPath}/test/fileupload",
      autoUpload: false,
      dataType: 'json',
      maxFileSize: 524288000,
      formData: {
        id: $("#template").val()
      },
      add: function (e, data) {
        data.submit();
      },
      done: function (e, data) {
        //console.info(data);
        $('#files').addClass('note note-success');
        $('#files').append('<p>拖拽改变excel列对应的属性</p>');
        $('#btn-upload').hide();
        $('#import').show();
        var porp = data.result.porp;
        var title = data.result.title;
        file = data.result.file;
        for (var i in porp) {

          $("#ul-prop").append('<li id="' + porp[i].sortId + '" data-id="' + porp[i].sortId + '" class="prop-handle">'
              + porp[i].name + '</li>');
        }

        for (var i in title) {
          $("#ul-title").append('<li id="' + title[i].sortId + '" data-id="' + title[i].sortId + '" class="title-handle">'
              + title[i].name + '</li>');
        }

        var list = document.getElementById("ul-title");
        sortable = Sortable.create(list);
      }
    });

    $('#fileupload').bind('fileuploadsubmit', function (e, data) {
      // The example input, doesn't have to be part of the upload form:

      data.formData = {id: $("#template").val()};

    });





    $('#import').on('click',function () {
      var order = sortable.toArray().join(',');
      $.ajax({
        url: "${request.contextPath}/test/importData",
        data: {
          "id":$("#template").val(),
          "file": file,
          "order": order
        },
        dataType: 'json',
        success: function (data) {
          $('#files').empty();
          $('#files').append('<p>导入完成</p>');
        }
      });
    });

  });



</script>
</body>

</html>