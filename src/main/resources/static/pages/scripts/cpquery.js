var table,record;
var count_dz = 0;
var isRun = false;
var reload_status = false;
var CpQuery = function () {

  function initButtonEvent() {

    $('#taskInfo').validate({
      errorPlacement: function (error, element) {
        $(error).css('color', 'red');
        $(element).parent().parent()
        .find("label[for='" + element.attr("id") + "']")
        .append(error);

      },

      errorElement: "span",
      rules: {
        account: {
          required: true
        },
        password: {
          required: true
        },
        template: {
          required: true
        },
        matchFiled: {
          required: true
        },
        matchChar: {
          required: true
        },
        anFiled: {
          required: true
        },
        statusFiled: {
          required: true
        }
      }
    });

    $('#addTask').on('click', function () {

      initTaskInfo(null);
      $('#task_dialog').modal();
    });
    $('#reload_dz').on('click', function () {
      if (!reload_status) {
        reload_status = true;

        runTask($('#id').val(), true);
        reload_status = false;
      }

    });

    $('#jcaptchaimage').on('click', function () {
      var click_1 = document.getElementById("click_1_dz");
      var click_2 = document.getElementById("click_2_dz");
      var click_3 = document.getElementById("click_3_dz");
      var obj = this;
      if (count_dz == 0 && click_1 == null) {
        x = event.offsetX;
        y = event.offsetY;
        var oDiv = document.createElement('div');
        oDiv.style.left = x - 15 + 'px';
        oDiv.style.top = y - 15 + 'px';
        oDiv.id = 'click_1_dz';
        oDiv.className = 'div_point-1';
        oDiv.className += " " + 'div_icon-point';
        var reload = document.getElementById('reload_dz');
        reload.parentNode.insertBefore(oDiv, reload);
        click_1 = document.getElementById("click_1_dz");
      }
      if (count_dz == 1 && click_2 == null) {
        x1 = event.offsetX;
        y1 = event.offsetY;
        var oDiv = document.createElement('div');
        oDiv.style.left = x1 - 15 + 'px';

        oDiv.style.top = y1 - 15 + 'px';
        oDiv.id = 'click_2_dz';
        oDiv.className = 'div_point-2';
        oDiv.className += " " + 'div_icon-point';

        var reload = document.getElementById('reload_dz');
        reload.parentNode.insertBefore(oDiv, reload);
        click_2 = document.getElementById("click_2_dz");
      }
      if (count_dz == 2 && click_3 == null) {
        x2 = event.offsetX;
        y2 = event.offsetY;
        var oDiv = document.createElement('div');
        oDiv.style.left = x2 - 15 + 'px';
        oDiv.style.top = y2 - 15 + 'px';

        oDiv.id = 'click_3_dz';
        oDiv.className = 'div_point-3';
        oDiv.className += " " + 'div_icon-point';

        var reload = document.getElementById('reload_dz');
        reload.parentNode.insertBefore(oDiv, reload);
        click_3 = document.getElementById("click_3_dz");
      }

      if (count_dz >= 2 && click_3 != null) {
        var code = 'x' + ':' + x + ';' + 'y' + ':' + y + ';' + 'x1' + ':' + x1 + ';' + 'y1' + ':' + y1 + ';' + 'x2' + ':' + x2 + ';' + 'y2' + ':' + y2
            + ';';

        executeTask($('#id').val(), code);
      } else {
        count_dz = count_dz + 1;
      }
    });
  }

  function loadCpqueryList() {

    table = $('#cpquerylist').DataTable({
      "ajax": http_request + "/cpquery/cpqueryInfo",

      columns: [
        {
          "data": "id",
          "title": "任务编号"
        },
        {
          "data": "account",
          "title": "查询账户",
          render: function (data, type, full, meta) {
            return base64decode(data);
          }
        },
        {
          "data": "templateId",
          "title": '专利模板',
          render: function (data, type, full, meta) {
            return datas[data].name;
          }

        },
        {
          "data": null,
          "title": "匹配字段",
          render: function (data, type, full, meta) {
            return datas[data.templateId].properties[data.matchField];
          }
        },
        {
          "data": "matchChar",
          "title": "匹配字符"
        },
        {
          "data": null,
          "title": "申请号",
          render: function (data, type, full, meta) {
            return datas[data.templateId].properties[data.applicationNumberField];
          }
        },
        {
          "data": null,
          "title": "官方状态字段",
          render: function (data, type, full, meta) {
            return datas[data.templateId].properties[data.statusField];
          }
        },

        {
          "data": "status",
          "title": "状态",
          render: function (data, type, full, meta) {
            var str;
            switch (data) {
              case 0:
                str = '<span class="label label-sm label-success">停止</span>';
                break;
              case 1:
                isRun = true;
                str = '<span class="label label-sm label-info">运行</span>';
                break;
              default:
                str = '<span class="label label-sm label-warning">异常</span>';
            }

            return str;
          }
        },
        {
          "data": null,
          "title": "操作",
          render: function (data, type, full, meta) {

            var action;
            var action1 = "<button  id='task-" + data.id + "' onclick='runTask(" + data.id
                + ",false)' class='btn btn-outline btn-circle btn-sm blue' ><i class='fa fa-toggle-right'></i>启动任务</button>";

            var action2 = "<a href='" + http_request + "/cpquery/cpqueryResult?id=" + data.id
                + "' class='btn btn-outline btn-circle btn-sm blue'><i class='fa fa-envelope'></i>查看结果</a>";

            var action3 = "<a href='#' onclick='editTask(" + data.id
                + ")' class='btn btn-outline btn-circle btn-sm green'><i class='fa fa-edit'></i>编辑任务</a>";

            var action4 = "<a href='#' onclick='delTask(" + data.id
                + ")' class='btn btn-outline btn-circle btn-sm red'><i class='fa fa-remove'></i>删除任务</a>";

            var action5 = "<a href='#' onclick='stopTask()' class='btn btn-outline btn-circle btn-sm red'><i class='fa fa-stop'></i>停止任务</a>";

            switch (data.status) {
              case 0:
                action = action1 + action3 + action4;
                break;
              case 1:
                action = action5;
                break;
              case 1:
                action = action1 + action3 + action4;
                break;
              default:
                action = action1 + action3;
            }

            return action;
          }
        }
      ],
      language: {
        "url": http_request + "/global/plugins/datatables/Chinese.json"
      }
    });

    $('#cpquerylist')
    .on( 'init.dt', function () {
      var data = table.column( 0 ).data();

      for (var i = 0; i < data.length;i++) {
        $('#q_number').append('<option >'+data[i]+'</option>');
      }

      $('#q_number').selectpicker('refresh');
    } );
  }

  function loadRecordList() {

    record = $('#recordlist').DataTable({
      "ajax": http_request + "/cpquery/cpqueryResult",

      columns: [
        {
          "data": "cpqueryId",
          "title": '任务编号'
        },
        {
          "data": null,
          "title": "申请号",
          "width": "250px",
          render: function (data, type, full, meta) {

            var action = "<a href='" + http_request + "/patent/patent?id=" + data.patentId + "' target='_blank'>" + data.applicationNumber + "</a>";
            return action;
          }
        },
        {
          "data": "officialStatus",
          "title": '官方状态'
        },
        {
          "data": "createTime",
          "title": "创建时间",
          "width": "250px",
          render: function (data, type, full, meta) {
            var dt = new Date(data);
            return dt.format("yyyy-MM-dd hh:mm:ss");

          }
        },
        {
          "data": "updateTime",
          "title": "更新时间",
          "width": "250px",
          render: function (data, type, full, meta) {
            if (data == null) {
              return '';
            }
            var dt = new Date(data);
            return dt.format("yyyy-MM-dd hh:mm:ss");
          }
        },
        {
          "data": "status",
          "title": '状态',
          render: function (data, type, full, meta) {
            var str;
            switch (data) {
              case 0:
                str = '<span class="label label-sm label-info">未执行</span>';
                break;
              case 1:
                str = '<span class="label label-sm label-success">查询成功</span>';
                break;
              case 2:
                str = '<span class="label label-sm label-warning">查询不到案件</span>';
                break;
              case 3:
                str = '<span class="label label-sm label-default">官方状态为空</span>';
                break;

            }

            return str;
          }
        }
      ],
      order: [[3, "desc"]],
      language: {
        "url": http_request + "/global/plugins/datatables/Chinese.json"
      }
    });

    $('#recordlist')
    .on( 'init.dt', function () {
      var data = record.column( 3 ).data();
      var options = [];
      for (var i = 0; i < data.length;i++) {
        var dt = new Date(data[i]).format("yyyy-MM-dd");

        if (!options.in_array(dt)) {
          options.push(dt);
        }
      }

      for (var j = 0; j < options.length;j++) {

        $('#q_date').append('<option >'+options[j]+'</option>');
      }

      $('#q_date').selectpicker('refresh');



    } );
  }
  var handleBootstrapSelect = function() {



    $('#q_status').append('<option value="未执行">未执行</option>');
    $('#q_status').append('<option value="查询成功">查询成功</option>');
    $('#q_status').append('<option value="查询不到案件">查询不到案件</option>');
    $('#q_status').append('<option value="官方状态为空">官方状态为空</option>');


    $('.bs-select').selectpicker({
      iconBase: 'fa',
      tickIcon: 'fa-check'
    });

    $('.bs-select').on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue) {
      record
      .column( 0 )
      .search($('#q_number').val() )
      .column( 3 )
      .search($('#q_date').val() )
      .column( 5 )
      .search($('#q_status').val() )
      .draw();
    });
  }


  return {

    init: function () {
      initButtonEvent();
      loadCpqueryList();
      handleBootstrapSelect();
      loadRecordList();

    }
  };

}();

function initTaskInfo(data) {
  for (var tpl in datas) {
    if (isNumber(tpl)) {
      $('#template').append('<option value="' + datas[tpl].id + '" >' + datas[tpl].name + '</option>');
    }

  }

  if (data != null) {
    var properties = datas[data.templateId].properties;
    $('#matchFiled').empty();
    $('#anFiled').empty();
    $('#statusFiled').empty();
    for (var attr in properties) {

      $('#matchFiled').append('<option value="' + attr + '" >' + properties[attr] + '</option>');
      $('#anFiled').append('<option value="' + attr + '" >' + properties[attr] + '</option>');
      $('#statusFiled').append('<option value="' + attr + '" >' + properties[attr] + '</option>');
    }

    $('#template').val(data.templateId);
    $('#matchFiled').val(data.matchField);
    $('#matchChar').val(data.matchChar);
    $('#anFiled').val(data.applicationNumberField);
    $('#statusFiled').val(data.statusField);
    $('#account').val(base64decode(data.account));
    $('#password').val(base64decode(decodeURIComponent(data.password)));
  } else {
    $('#task_id').val(0);
    $('#account').val('');
    $('#password').val('');
  }

  $('#template').on('change', function () {

    if ($(this).val() != '') {
      var properties = datas[$(this).val()].properties;
      $('#matchFiled').empty();
      $('#anFiled').empty();
      $('#statusFiled').empty();
      for (var attr in properties) {

        $('#matchFiled').append('<option value="' + attr + '" >' + properties[attr] + '</option>');
        $('#anFiled').append('<option value="' + attr + '" >' + properties[attr] + '</option>');
        $('#statusFiled').append('<option value="' + attr + '" >' + properties[attr] + '</option>');
      }
    } else {
      $('#matchFiled').empty();
      $('#anFiled').empty();
      $('#statusFiled').empty();
    }

  })

}

function editTask(id) {

  $('#task_id').val(id);
  $.ajax({
    url: http_request + "/cpquery/getCpqueryInfo",
    data: {
      "id": id
    },
    success: function (data) {

      initTaskInfo(data);
      $('#task_dialog').modal();
    }
  });

}

function delTask(id) {

  $.ajax({
    url: http_request + "/cpquery/delCpqueryInfo",
    data: {
      "id": id
    },
    success: function (data) {
      table.ajax.reload();
    }
  });

}

function stopTask() {

  $.ajax({
    url: http_request + "/cpquery/stopCpquery",
    success: function (data) {
      alert('正在停止中，请稍后刷新页面');
    }
  });

}

function saveTask() {
  if ($('#taskInfo').validate().form()) {
    $.ajax({
      url: http_request + "/cpquery/saveCpqueryInfo",
      data: {
        "id": $('#task_id').val(),
        "templateId": $('#template').val(),
        "matchField": $('#matchFiled').val(),
        "matchChar": $('#matchChar').val(),
        "applicationNumberField": $('#anFiled').val(),
        "statusField": $('#statusFiled').val(),
        "account": base64encode($('#account').val()),
        "password": encodeURIComponent(base64encode($('#password').val()))
      },
      success: function (data) {
        $('#task_dialog').modal('toggle');
        table.ajax.reload();
      }
    });

  } else {
    //alert('字段不能为空');
  }

}

function runTask(id, reload) {

  if (isRun) {
    alert('当前已有任务在运行');
    return;
  }

  var click_1 = document.getElementById("click_1_dz");
  var click_2 = document.getElementById("click_2_dz");
  var click_3 = document.getElementById("click_3_dz");
  if (click_1 != null) {
    click_1.parentNode.removeChild(click_1);
  }
  if (click_2 != null) {
    click_2.parentNode.removeChild(click_2);
  }
  if (click_3 != null) {
    click_3.parentNode.removeChild(click_3);
  }
  count_dz = 0;

  $.ajax({
    url: http_request + "/cpquery/getJcaptcha",
    data: {
      "id": id
    },
    success: function (data) {

      switch (data.status) {
        case 0:
          $('#jcaptchaimage').attr("src", http_request + "/temp/" + data.imagePath);
          $('#selectyzm_text_dz').text(data.imageText.replace(new RegExp("&nbsp", "g"), " "));

          if (reload == false) {
            $('#captcha_dialog').modal();
            $('#id').val(id);
          }
          break;
        case 1:
          alert('当前已有任务正在运行');
          break;
        case 2:
          alert('请检查用户名密码是否正确。如果正确，有可能是网络阻塞，请稍后再试');
          break;
        default:

      }

    }
  });

}

function executeTask(id, code) {

  $.ajax({
    url: http_request + "/cpquery/executeCpquery", //ajax提交
    data: {
      'id': id,
      'code': code
    },
    success: function (data) {
      if (data.retcode == '000000') {

        $('#captcha_dialog').modal('hide')
        count_dz = 0;

        window.location.reload();
      } else {
        alert('效验失败，请刷新验证码重试');
      }

    }
  });
}

jQuery(document).ready(function () {
  CpQuery.init();
});
