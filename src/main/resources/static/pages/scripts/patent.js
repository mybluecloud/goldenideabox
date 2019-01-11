var Patent = function () {

  var jqXHR;

  return {
    init: function () {
      $.fn.editable.defaults.mode = 'inline';
      $.fn.editable.defaults.inputclass = 'form-control';

      if ($("#patentID") != 0) {
        $.ajax({
          url: http_request + "/patent/patentproperty",
          data: {
            "id": $("#patentID").val(),
          },
          success: function (data) {


            //console.log(data);
            var index = Object.keys(data.propertys);
            var map = {};
            for (var i = 0; i < data.documents.length; i++) {
              var value = data.documents[i];
              var key = value['id'];
              map[key] = value;
            }

            Patent.loadTemplate(index, data.propertys, map);
          }
        });
      } else {

        this.loadTemplate();
      }
      $('#template').change(function () {

        Patent.loadTemplate("", "");
      });

    },
    loadTemplate: function (index, propertys, documents) {

      $("#property").empty();
      $.ajax({
        url: http_request + "/patent/templateproperty",
        data: {
          "id": $("#template").val(),
          "patentID": $("#patentID").val()
        },
        success: function (data) {

          for (var i = 0; i < data.length; i++) {
            var code;
            var value = "";

            if (index.indexOf(data[i].sortId) != -1) {
              value = propertys[data[i].sortId].value;
            }
            if (i % 2 == 0) {
              $("#property").append("<tr></tr>");
            }
            switch (data[i].type) {
              case 0:
                //文本

                code = "<td width='15%'>" + data[i].name
                    + "</td><td width='35%'>"
                    + "<a href='#' id='" + data[i].sortId + "' "
                    + "data-type='text' >" + value + "</a></td>";

                $("#property").children("tr:last-child").append(code);

                $('#' + data[i].sortId).editable({
                  url: http_request + "/patent/edit",
                  emptytext: "空",
                  pk: $("#patentID").val()
                });
                break;

              case 1:
                //富文本
                code = "<td width='15%'>" + data[i].name
                    + "<a href='#' id='pencil" + data[i].sortId + "'>"
                    + "<i class='icon-pencil' style='padding-right: 5px'></i>[编辑]</a></td><td width='35%'>"
                    + "<div id='" + data[i].sortId + "' data-type='wysihtml5' data-toggle='manual' "
                    + "data-placement='top' >" + value + "</div></td>";

                $("#property").children("tr:last-child").append(code);
                $('#' + data[i].sortId).editable({
                  url: http_request + "/patent/edit",
                  pk: $("#patentID").val(),
                  emptytext: "空",
                  showbuttons: 'right'
                });
                $('#pencil' + data[i].sortId).click({id: data[i].sortId},
                    function (e) {
                      e.stopPropagation();
                      e.preventDefault();
                      $('#' + e.data.id).editable('toggle');
                    });
                break;
              case 2:
                //列表
                code = "<td width='15%'>" + data[i].name
                    + "</td><td width='35%'>"
                    + "<a href='#' id='" + data[i].sortId + "' "
                    + "data-type='select' data-value=''>" + value + "</a></td>";

                $("#property").children("tr:last-child").append(code);

                var values = data[i].value.split(";");
                var source = [];
                for (var j = 0; j < values.length; j++) {
                  var option = {};
                  option.value = values[j];
                  option.text = values[j];
                  source.push(option);
                }
                $('#' + data[i].sortId).editable({
                  url: http_request + "/patent/edit",
                  pk: $("#patentID").val(),
                  prepend: "未选择",
                  inputclass: 'form-control',
                  source: source
                });
                break;
              case 3:
                //文件
                code = "<td width='15%'>" + data[i].name
                    + "</td><td width='35%'>"
                    + "<div id='files" + data[i].sortId
                    + "' class='files'></div>"
                    + "<span class='btn btn-sm blue fileinput-button'><i class=\"fa fa-file-o\"></i>"
                    + "<span>选择</span><input id='" + data[i].sortId
                    + "' type='file' name='files[]' multiple></span>"
                    + "<span id='progress" + data[i].sortId + "'></span>"
                    + "<button id='stop" + data[i].sortId
                    + "' style='display: none'><i class='glyphicon glyphicon-stop'></i></button></td>";

                $("#property").children("tr:last-child").append(code);

                var lst = value.split(",");
                for (var j = 0; j < lst.length; j++) {
                  if (lst[j].length > 0) {

                    $('#files' + data[i].sortId).append("<p><a href='" + http_request + "/patent/download?id=" + lst[j] + "' >"
                        + documents[lst[j]].name + "</a>"
                        + "<span id='" + data[i].sortId + "-" + documents[lst[j]].name
                        + "'><a id='del-" + data[i].sortId + '-' + lst[j]
                        + "'><i class='glyphicon glyphicon-trash'></i></a></span></p>");
                    $("#del-" + data[i].sortId + '-' + lst[j]).on('click', {
                      filename: documents[lst[j]].name,
                      fileId: lst[j],
                      sortId: data[i].sortId
                    }, function (event) {

                      $("#" + this.id).parent().parent().remove();
                      $.ajax({
                        url: http_request + "/patent/deletefile",
                        data: {
                          "id": event.data.fileId,
                          "sortId": event.data.sortId,
                          "patentId": $("#patentID").val()
                        },
                        success: function (data) {

                        }
                      });

                    });
                  }
                }

                $('#' + data[i].sortId).fileupload({
                  url: http_request + "/patent/upload",
                  autoUpload: false,
                  dataType: 'json',
                  maxFileSize: 524288000,
                  formData: {
                    sortId: data[i].sortId,
                    patentId: $("#patentID").val()
                  },
                  add: function (e, data) {

                    $('#files' + this.id).append("<p>" + data.files[0].name
                        + "<span id='" + this.id + "-" + data.files[0].name
                        + "'>上传中...</span></p>");
                    jqXHR = data.submit();
                  },
                  done: function (e, data) {

                    document.getElementById(this.id + "-" + data.result.file).parentNode.innerHTML =
                        '<a href="' + http_request + '/patent/download?id=' + data.result.id + '">' + data.result.file
                        + '</a><span id="' + this.id + '-' + data.result.file + '"><a id="del-' + this.id
                        + '-' + data.result.id + '"><i class="glyphicon glyphicon-trash"></i></a></span>';


                    $("#del-" + this.id + '-' + data.result.id).on('click', {
                      filename: data.result.file,
                      fileId: data.result.id,
                      sortId: this.id
                    }, function (event) {

                      $("#" + this.id).parent().parent().remove();
                      $.ajax({
                        url: http_request + "/patent/deletefile",
                        data: {
                          "id": event.data.fileId,
                          "sortId": event.data.sortId,
                          "patentId": $("#patentID").val()
                        },
                        success: function (data) {

                        }
                      });

                    });

                  },
                  progressall: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#progress' + this.id).text(progress + '%');
                    if (progress != 100) {
                      $('#stop' + this.id).show();
                    } else {
                      $('#stop' + this.id).hide();
                      $('#progress' + this.id).text('');
                    }
                  }
                }).error(function (jqXHR, textStatus, errorThrown) {
                  // console.info(errorThrown);
                  // console.info(textStatus);
                  if (errorThrown === 'abort') {
                    $('#progress' + data[i].sortId).text("文件上传已取消");

                  }
                });
                $('#stop' + data[i].sortId).click(function (e) {
                  jqXHR.abort();
                  $('#' + this.id.replace('stop', 'progress')).text("文件上传已取消");
                  $('#' + this.id).hide();
                });

                break;
              case 4:
                //数值
                code = "<td width='15%'>" + data[i].name
                    + "</td><td width='35%'>"
                    + "<a href='#' id='" + data[i].sortId + "' "
                    + "data-type='text' >" + value + "</a></td>";

                $("#property").children("tr:last-child").append(code);
                $('#' + data[i].sortId).editable({
                  url: http_request + "/patent/edit",
                  emptytext: "空",
                  pk: $("#patentID").val()
                });
                break;
              case 5:
                //日期

                code = "<td width='15%'>" + data[i].name
                    + "</td><td width='35%'>"
                    + "<a href='#' id='" + data[i].sortId + "' "
                    + "data-type='date' data-viewformat='yyyy-mm-dd' data-placement='left'>" + value + "</a></td>";

                $("#property").children("tr:last-child").append(code);
                $('#' + data[i].sortId).editable({
                  url: http_request + "/patent/edit",
                  pk: $("#patentID").val(),
                  emptytext: "未设置",
                  datepicker: {
                    language: "zh-CN"
                  }
                });
                break;
              case 6:
                //计算
                break;

            }

          }
        }
      });
    }
  };

}();

jQuery(document).ready(function () {
  Patent.init();
});