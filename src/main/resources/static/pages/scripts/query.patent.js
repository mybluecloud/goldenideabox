var table = null;
var config = {};
config.id = 0;
config.templateId = 0;
config.columns = {};
config.data = "";
config.order = "";
config.visible = "";
config.left = 0;
config.right = 0;
config.filter = "";
config.pageLength = 10;
config.search = "";
var appoint = false;
var sortable;
var cell = {};
cell.handle = null;

var QueryPatent = function () {

  function loadTableConfig(columns) {



    //设置配置modal内容
    var visible = config.visible.split(',');
    var searchable = config.search.split(',');
    $("#propertys").empty();

    for (var i = 0; i < columns.length; i++) {
      var display = '';
      if (config.visible.length == 0 || visible.in_array(columns[i].data)) {
        display = 'checked';
      }

      var search = '';
      if (config.search.length == 0 || searchable.in_array(columns[i].data)) {
        search = 'checked';
      }

      $("#propertys").append("<tr id='" + columns[i].data + "' data-id='" + columns[i].data + "'><td>" + columns[i].title + "</td>"
          + "<td><input  type='checkbox' class='display' " + display + "></td>"
          + "<td><input  type='checkbox' class='search' " + search + "></td></tr>");


    }

    var list = document.getElementById("propertys");
    sortable = Sortable.create(list);

    $("#fixed_left").val(config.left);
    $("#fixed_right").val(config.right);

    $('.scroller').slimScroll({
      height: '400px'
    });

    //清空table内容
    if (table != null) {
      table.destroy();
      $('#patentlist').empty();
    }
    var select = [{
      "data": null,
      "title": '<input type="checkbox" class="group-checkable" onchange="checkable()" >',
      orderable: false,
      width: "1%",
      render: function (data, type, full, meta) {

        return '<input type="checkbox" class="checkboxes" value="'
            + data.id + '" >';
      }
    }];

    var columnData = [];
    for (var index = 0; index < columns.length; index++) {

      var data = {
        "data": columns[index].data,
        "title": columns[index].title,
        "defaultContent": columns[index].defaultContent,
        render: function( data, type, full, meta ) {
          if(data){
            if(data.length>15){
              return "<span title='"+data+"'>"+ data.substr(0, 12) + "...</span>";
            }else{
              return data;
            }
          }else{
            return "";
          }
        }

      };
      columnData.push(data);
    }

    $.merge(select, columnData);

    var action = [{
      "data": null,
      "title": "操作",
      orderable: false,
      render: function (data, type, full, meta) {

        var action1 = "<a href='" + http_request + "/patent/patent?id=" + data.id
            + "' class='btn btn-outline btn-circle btn-sm blue' target='_blank'><i class='fa fa-edit'></i>编辑</a>";

        return action1;
      }
    }];
    $.merge(select, action);
    var offset = 1;
    if (config.left > 0) {
      var orderlst = config.order.split(',');
      var visiblelst = config.visible.split(',');
      var visibleNum = 0;
      for (var i in orderlst) {
        if (visiblelst.indexOf(orderlst[i]) < 0) {
          offset+=1;
        } else {
          if (visibleNum >= config.left) {
            break;
          }
          visibleNum+=1;
        }
      }
    }
    var h = $(window).height() - 300;
    table = $('#patentlist').DataTable({
      ajax: {
        url: http_request + "/patent/patents",
        type: "post",
        dataSrc: "data",
        data: {
          "configId": config.id,
          "templateId": config.templateId,
          "filter": config.filter,
          "appoint":appoint
        }
      },
      columns: select,
      processing: true,
      serverSide: true,
      deferRender:    true,
      scrollY:        h,
      scrollX:        true,
      scrollCollapse: true,
      pageLength: config.pageLength,
      //fixedHeader: true,
      fixedColumns: { //固定列的配置项
        leftColumns: config.left + offset,
        rightColumns: config.right + 1
      },
      language: {
        "url": http_request + "/global/plugins/datatables/Chinese.json"
      }
    });
    $('#patentlist tbody').on( 'dblclick', 'td', function () {

      if (cell.handle != null) {
        $(cell.handle).empty();
        $(cell.handle).text(cell.value);
      }

      var index = table.cell( this ).index();
      var row = table.row( index.row ).node();

      if (index.column == 0) {
        return;
      }

      cell.handle = this;
      cell.value = $(this).text();
      cell.data = table.column( index.column ).dataSrc();
      cell.id = $(row).find('.checkboxes').val();

      var html = '';
      switch (config.columns[cell.data].type) {
        case 0:

          html = '<textarea class="form-control" >'+$(this).text()+'</textarea>';
          break;
        case 2:

          html = '<select class="form-control" value="'+$(this).text()+'">';
          var values = config.columns[cell.data].value.split(";");

          for (var i = 0; i < values.length; i++) {
            if ($(this).text().trim() == values[i].trim()) {
              html = html + '<option value="' + values[i] + '" selected = "selected">' + values[i] + '</option>';
            } else {
              html = html + '<option value="' + values[i] + '">' + values[i] + '</option>';
            }

          }
          html = html + '</select>';
          break;
        case 4:

          break;
        case 5:

          html = '<input type="text" class="form-control  input-sm date-picker" value="'+$(this).text()+'">';
          break;
      }
      if (html.length > 0) {
        var content = [
          '<form class="form ">'
          + '<div style="line-height: 1;">'
          + '<div  style="display: table-cell;">'
          + html
          + '</div>'
          + '<div style="display: table-cell;">'
          + '<button type="button" class="btn blue "  onclick="updateValue(this);"><i class="fa fa-check"></i></button></div>'
          + '<div style="display: table-cell;">'
          + '<button type="button" class="btn default" onclick="closeEdit();"><i class="fa fa-times"></i></button>'
          + '</div></div></form>'
        ].join("");


        $(this).empty();
        $(this).append(content);
        //$(this).find('input').focus();

        if (jQuery().datepicker) {
          $('.date-picker').datepicker({
            language: "zh-CN",
            autoclose: true
          });

        }
      }



    } );


    $('#patentlist').on('page.dt', function () {
      if ($('.group-checkable').prop("checked")) {
        $('.group-checkable').prop("checked", false);
      }
    });
  };

  function loadPatentList() {
    $.ajax({
      url: http_request + "/patent/patentTable",
      data: {
        "configId": config.id,
        "templateId": config.templateId,
        "filter": config.filter,
        "order": config.order,
        "visible": config.visible,
        "left": config.left,
        "right": config.right,
        "pageLength": config.pageLength,
        "search": config.search
      },
      dataType: 'json',
      success: function (data) {

        for(var i in data.columns) {
          config.columns[data.columns[i].data]=data.columns[i];
        }

        //config.columns = data.columns;
        config.order = data.order;
        config.visible = data.visible;
        config.left = data.left;
        config.right = data.right;
        config.filter = data.filter;
        config.pageLength = data.pageLength;
        config.search = data.search;
        $("#sql").val(config.filter);
        loadTableConfig(data.columns);

      }
    });
  };

  function search() {
    $('#search').on('click', function () {
      config.filter = $("#sql").val();
      loadPatentList();
    });
  };

  function initBookmark() {
    var template = [
      '<div class="popover" style="width: auto;max-width: none !important;">',

      '<div class="popover-title"></div>',
      '<div class="popover-content"></div>',
      '</div>'
    ].join("");

    var title = ['<h4 class ="popover-title">输入标签名称</h4>'].join("");
    var content = [
      '<form class="form ">'
      + '<div style="line-height: 1;">'
      + '<div  style="display: table-cell;">'
      + '<input id="lable_name" type="text" class="form-control" style="width: 200px">'
      + '</div>'
      + '<div style="display: table-cell;">'
      + '<button type="button" class="btn blue " data-apply="popover" onclick="saveLable()"><i class="fa fa-check"></i></button></div>'
      + '<div style="display: table-cell;">'
      + '<button type="button" class="btn default" data-dismiss="popover" onclick="cancelLable()"><i class="fa fa-times"></i></button>'
      + '</div></div></form>'
    ].join("");

    $('#bookmark').popover({
      html: true,
      trigger: "click",
      placement: 'top',
      title: title,
      content: content,
      template: template
    });


    var recontent = [
      '<form class="form ">'
      + '<div style="line-height: 1;">'
      + '<div  style="display: table-cell;">'
      + '<input id="lable_saveas" type="text" class="form-control" style="width: 200px">'
      + '</div>'
      + '<div style="display: table-cell;">'
      + '<button type="button" class="btn blue " data-apply="popover" onclick="saveAsLable()"><i class="fa fa-check"></i></button></div>'
      + '<div style="display: table-cell;">'
      + '<button type="button" class="btn default" data-dismiss="popover" onclick="cancelLable()"><i class="fa fa-times"></i></button>'
      + '</div></div></form>'
    ].join("");

    $('#saveas').popover({
      html: true,
      trigger: "click",
      placement: 'top',
      title: title,
      content: recontent,
      template: template
    });

    $('#save').on('click', function () {
      $.ajax({
        url: http_request + "/patent/saveLable",
        data: {
          "configId": config.id,
          "configname": null,
          "templateId": config.templateId,
          "filter": config.filter,
          "order": config.order,
          "visible": config.visible,
          "left": config.left,
          "right": config.right,
          "pageLength":table.page.info().length
        },
        dataType: 'json',
        success: function (data) {
          notify('', '保存标签成功！', '', '');
        }
      });
    });

    $('#delete').on('click', function () {
      swal({
        title:'确定删除该标签？',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(function (result) {

        if (result.value) {
          $.ajax({
            url: http_request + "/patent/deleteLable",
            data: {
              "configId": config.id
            },
            dataType: 'json',
            success: function (data) {

              notify('', '删除标签成功！', '', '');
              window.location.reload(true);
            }
          });
        }
      });

    });

    $('#export').on('click', function () {
      swal({
        title:'确定导出该标签数据？',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(function (result) {

        if (result.value) {
          $.ajax({
            url: http_request + "/patent/export",
            data: {
              "configId": config.id,
              "templateId": config.templateId,
              "appoint":appoint
            },
            dataType: 'json',
            success: function (data) {

              $('<form method="get" action="'+http_request +  data + '"></form>').appendTo('body').submit().remove();

            }
          });
        }
      });

    });
  };

  function initTabs() {
    $('#tabs').on('click', 'li', function () {
      //console.info($(this).children("input[name='filter']").val());
      config.id = $(this).children("input[name='id']").val();
      if (config.id != 0) {
        $("#template").attr("disabled", "disabled").css("background-color", "grey");
        config.templateId = $(this).children("input[name='templateId']").val();
        $('#bookmark').css('display', 'none');
        $('#saveas').css('display', 'inline-block');
        $('#save').css('display', 'inline-block');
        $('#delete').css('display', 'inline-block');

      } else {
        $("#template").removeAttr("disabled").css("background-color", "");
        config.templateId = $('#template').val();
        $('#bookmark').css('display', 'inline-block');
        $('#saveas').css('display', 'none');
        $('#save').css('display', 'none');
        $('#delete').css('display', 'none');
      }
      $('#template').val(config.templateId);

      if (jQuery("#advance_search").is(":visible")) {
        $("#advance_search").toggle();
      }

      config.order = "";
      config.visible = "";
      config.left = 0;
      config.right = 0;
      config.filter = "";
      config.search = "";

      loadPatentList();

    });
  }

  function initTemplateChange() {
    $('#template').change(function () {
      config.templateId = $('#template').val();
      config.order = "";
      config.visible = "";
      config.left = 0;
      config.right = 0;
      config.filter = "";
      loadPatentList();
    });
  }

  function initTable() {
    if (!jQuery().dataTable) {
      return;
    }
    if (document.getElementById("appoint")) {
      appoint = true;
    }
    config.templateId = $('#template').val();
    loadPatentList();
  }

  function apply() {
    $('#apply-style').on('click', function () {
      var visible = new Array();
      var order = sortable.toArray();
      config.order = order.join(',');
      config.left = $("#fixed_left").val();
      config.right = $("#fixed_right").val();
      jQuery("#propertys .display").each(function () {
        if (jQuery(this).is(":checked")) {
          visible.push($(this).parent().parent().prop("id"));
        }
      });

      config.visible = visible.join(',');
      var search = new Array();
      jQuery("#propertys .search").each(function () {
        if (jQuery(this).is(":checked")) {
          search.push($(this).parent().parent().prop("id"));
        }
      });

      config.search = search.join(',');
      loadPatentList();
    });

  }

  return {

    init: function () {
      initTable();
      initTemplateChange();
      initTabs();
      search();
      initBookmark();
      apply();

    }
  };

}();


function insertHtml(attr) {
  var str = "";
  switch (attr.type) {
    case 0:
    case 1:
    case 3:
    case 7:
      str = '<div class="col-md-6 margin-top-10">'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign1-' + attr.sortId + '" class="form-control input-sm">'
          + '              <option value="and">AND</option>'
          + '              <option value="or">OR</option>'
          + '            </select>'
          + '          </div>'
          + '          <label class="control-label ">' + attr.name + '</label>'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign2-' + attr.sortId + '" class="form-control input-sm">'
          + '              <option value="like">包含</option>'
          + '              <option value="not like">不包含</option>'
          + '            </select>'
          + '          </div>'
          + '          <div class="form-group form-md-line-input">'
          + '            <input id="condition-' + attr.sortId + '" type="text" class="form-control input-sm" placeholder="条件">'
          + '          </div>'
          + '          <button type="button" class="btn btn-sm  btn-circle blue-dark pull-right" onClick="addCondition(' + attr.sortId + ',0,\''
          + attr.name + '\')">'
          + '            <i class="fa fa-plus"></i>'
          + '          </button>'
          + '        </div>';
      break;
    case 2:
      var option = '';
      var values = attr.value.split(";");

      for (var i = 0; i < values.length; i++) {
        option = option + '<option value="' + values[i] + '">' + values[i] + '</option>';
      }
      str = '<div class="col-md-6 margin-top-10">'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign1-' + attr.sortId + '" class="form-control input-sm">'
          + '              <option value="and">AND</option>'
          + '              <option value="or">OR</option>'
          + '            </select>'
          + '          </div>'
          + '          <label class="control-label ">' + attr.name + '</label>'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign2-' + attr.sortId + '" class="form-control input-sm">'
          + '              <option value="=">等于</option>'
          + '              <option value="<>">不等于</option>'
          + '            </select>'
          + '          </div>'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="condition-' + attr.sortId + '"  class="form-control input-sm">' + option
          + '            </select>'
          + '          </div>'
          + '          <button type="button" class="btn btn-sm  btn-circle blue-dark pull-right" onClick="addCondition(' + attr.sortId + ',2,\''
          + attr.name + '\')">'
          + '            <i class="fa fa-plus"></i>'
          + '          </button>'
          + '        </div>';
      break;

    case 4:
      str = '<div class="col-md-6 margin-top-10">'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign1-' + attr.sortId + '" class="form-control input-sm">'
          + '              <option value="and">AND</option>'
          + '              <option value="or">OR</option>'
          + '            </select>'
          + '          </div>'
          + '          <label class="control-label ">' + attr.name + '</label>'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign2-' + attr.sortId + '" class="form-control input-sm">'
          + '              <option value="=">=</option>'
          + '              <option value="<">&lt;</option>'
          + '              <option value=">">&gt;</option>'
          + '            </select>'
          + '          </div>'
          + '          <div class="form-group form-md-line-input">'
          + '            <input id="condition-' + attr.sortId + '" type="text" class="form-control input-sm" placeholder="条件">'
          + '          </div>'
          + '          <button type="button" class="btn btn-sm  btn-circle blue-dark pull-right" onClick="addCondition(' + attr.sortId + ',4,\''
          + attr.name + '\')">'
          + '            <i class="fa fa-plus"></i>'
          + '          </button>'
          + '        </div>';
      break;
    case 5:
      str = '<div class="col-md-6 margin-top-10">'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign1-' + attr.sortId + '"  class="form-control input-sm">'
          + '              <option value="and">AND</option>'
          + '              <option value="or">OR</option>'
          + '            </select>'
          + '          </div>'
          + '          <label class="control-label ">' + attr.name + '</label>'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign2-' + attr.sortId + '"  class="form-control input-sm">'
          + '              <option value="=">=</option>'
          + '              <option value="<">&lt;</option>'
          + '              <option value=">">&gt;</option>'
          + '            </select>'
          + '          </div>'
          + '          <div class="form-group form-md-line-input">'
          + '            <input id="condition-' + attr.sortId + '" type="text" class="form-control  input-sm date-picker" placeholder="条件">'
          + '          </div>'
          + '          <button type="button" class="btn btn-sm  btn-circle blue-dark pull-right" onClick="addCondition(' + attr.sortId + ',5,\''
          + attr.name + '\')">'
          + '            <i class="fa fa-plus"></i>'
          + '          </button>'
          + '        </div>';
      break;
    case 6:
      str = '<div class="col-md-6 margin-top-10">'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign1-' + attr.sortId + '"  class="form-control input-sm">'
          + '              <option value="and">AND</option>'
          + '              <option value="or">OR</option>'
          + '            </select>'
          + '          </div>'
          + '          <label class="control-label ">' + attr.name + '</label>'
          + '          <div class="form-group form-md-line-input">'
          + '            <select id="sign2-' + attr.sortId + '"  class="form-control input-sm">'
          + '              <option value="=">=</option>'
          + '              <option value="<">&lt;</option>'
          + '              <option value=">">&gt;</option>'
          + '            </select>'
          + '          </div>'
          + '          <div class="form-group form-md-line-input">'
          + '            <input id="condition-' + attr.sortId + '" type="text"  class="form-control input-sm" placeholder="条件">'
          + '          </div>'
          + '          <button type="button" class="btn btn-sm  btn-circle blue-dark pull-right" onClick="addCondition(' + attr.sortId + ',6,\''
          + attr.name + '\')">'
          + '            <i class="fa fa-plus"></i>'
          + '          </button>'
          + '        </div>';
      break;
    default:
      str = "";
  }

  return str;
}

function insertTitleHtml(type) {

  var title = "";
  switch (type) {
    case 0:
      title = "文本";
      break;
    case 1:
      title = "富文本";
      break;
    case 2:
      title = "列表";
      break;
    case 3:
      title = "文件";
      break;
    case 4:
      title = "数值";
      break;
    case 5:
      title = "日期";
      break;
    case 6:
      title = "计算";
      break;
    case 7:
      title = "系统配置";
      break;
    default:
      title = "";
  }

  var str = '<label class=" col-md-12 margin-top-10  font-blue-madison font-lg bold uppercase">' + title + '</label>';
  return str;
}

function addCondition(id, type, name) {
  var value = $("#sql").val();
  switch (type) {
    case 0:
    case 1:
    case 3:
    case 7:
      if (value.length == 0) {
        value = "  r." + id + "/*" + name + "*/  " + $("#sign2-" + id).val() + "  \"%" + $("#condition-" + id).val() + "%\"";
      } else {
        value = value + "  " + $("#sign1-" + id).val() + "  r." + id + "/*" + name + "*/  " + $("#sign2-" + id).val() + "  \"%" + $("#condition-"
            + id).val() + "%\"";
      }
      break;
    case 2:

      if (value.length == 0) {
        value = "  r." + id + "/*" + name + "*/  " + $("#sign2-" + id).val() + "  \"" + $("#condition-" + id).val() + "\"";
      } else {
        value = value + "  " + $("#sign1-" + id).val() + "  r." + id + "/*" + name + "*/  " + $("#sign2-" + id).val() + "  \"" + $("#condition-"
            + id).val() + "\"";
      }
      break;
    case 5:
      if (value.length == 0) {
        value = "  to_days(r." + id + ")/*" + name + "*/  " + $("#sign2-" + id).val() + "  to_days(\"" + $("#condition-" + id).val() + "\")";
      } else {
        value = value + "  " + $("#sign1-" + id).val() + "  to_days(r." + id + ")/*" + name + "*/  " + $("#sign2-" + id).val() + "  to_days(\""
            + $("#condition-"
                + id).val() + "\")";
      }
      break;
    case 4:
    case 6:

      if (value.length == 0) {
        value = "  r." + id + "/*" + name + "*/  " + $("#sign2-" + id).val() + "  " + $("#condition-" + id).val();
      } else {
        value = value + "  " + $("#sign1-" + id).val() + "  r." + id + "/*" + name + "*/  " + $("#sign2-" + id).val() + "  " + $("#condition-"
            + id).val();
      }
      break;
  }

  $("#sql").val(value);
}

function advanceSearch() {
  //console.info(jQuery("#advance_search").is(":visible") );
  if (jQuery("#advance_search").is(":hidden")) {
    $.ajax({
      url: http_request + "/patent/search",
      data: {
        "templateId": config.templateId
      },
      dataType: 'json',
      success: function (data) {
        var searchable = config.search.split(',');
        var type = -1;
        for (var i = 0; i < data.length; i++) {
          if (config.search.length == 0 || searchable.in_array(data[i].sortId)) {
            if (data[i].type != type) {
              type = data[i].type;
              $("#condition").append(insertTitleHtml(type));

            }

            $("#condition").append(insertHtml(data[i]));
          }

        }

        if (jQuery().datepicker) {
          $('.date-picker').datepicker({
            language: "zh-CN",
            autoclose: true
          });

        }
        $("#advance_search").toggle();
      }
    });
  } else {
    $("#condition").empty();
    $("#advance_search").toggle();
  }

}

function saveLable() {
  config.pageLength = table.page.info().length;
  $.ajax({
    url: http_request + "/patent/saveLable",
    data: {
      "configId": config.id,
      "configname": $("#lable_name").val(),
      "templateId": config.templateId,
      "filter": config.filter,
      "order": config.order,
      "visible": config.visible,
      "left": config.left,
      "right": config.right,
      "pageLength":config.pageLength,
      "search":config.search
    },
    dataType: 'json',
    success: function (data) {

      var tab = '<li>'
          + '<a href="#" data-toggle="tab">' + $("#lable_name").val() + '</a>'
          + '<input type="hidden" name="id" value="' + data.id + '"/>'
          + '<input type="hidden" name="templateId" value="' + data.templateId + '"/>'
          + '</li>';
      $('#tabs').append(tab);

      notify('', '添加标签' + $("#lable_name").val() + '成功！', '', '');

      $('#bookmark').popover('hide');

    }
  });
}

function saveAsLable() {
  config.pageLength = table.page.info().length;
  $.ajax({
    url: http_request + "/patent/saveLable",
    data: {
      "configId": 0,
      "configname": $("#lable_saveas").val(),
      "templateId": config.templateId,
      "filter": config.filter,
      "order": config.order,
      "visible": config.visible,
      "left": config.left,
      "right": config.right,
      "pageLength":config.pageLength,
      "search":config.search
    },
    success: function (data) {

      var tab = '<li>'
        + '<a href="#" data-toggle="tab">' + $("#lable_saveas").val() + '</a>'
        + '<input type="hidden" name="id" value="' + data.id + '"/>'
        + '<input type="hidden" name="templateId" value="' + data.templateId + '"/>'
        + '</li>';
      $('#tabs').append(tab);

      notify('', '添加标签' + $("#lable_saveas").val() + '成功！', '', '');

      $('#saveas').popover('hide');

    }
  });
}

function cancelLable() {

  $('#bookmark').popover('hide');
  $('#saveas').popover('hide');
}

function updateValue(td) {

  var value = cell.value;

  switch (config.columns[cell.data].type) {
    case 0:
      value = $(td).parent().parent().find('textarea').val();
      break;
    case 2:
      value = $(td).parent().parent().find('select option:selected').text();
      break;
    case 4:

      break;
    case 5:
      value = $(td).parent().parent().find('input').val();
      break;
  }

  $.ajax({
    url: http_request + "/patent/edit",
    data: {
      "name": cell.data,
      "pk": cell.id,
      "value": value
    },
    success: function (data) {

      $(cell.handle).empty();
      $(cell.handle).text(value);
      cell.value = value;
    }
  });


}
function closeEdit() {
  $(cell.handle).empty();
  $(cell.handle).text(cell.value);
}

function displaySwitch() {


  var checked = $('.group-display').is(':checked');

  jQuery(".display").each(function () {

    if (checked) {

      $(this).prop("checked", true);
    } else {

      $(this).prop("checked", false);
    }
  });
};

function searchSwitch() {


  var checked = $('.group-search').is(':checked');

  jQuery(".search").each(function () {

    if (checked) {

      $(this).prop("checked", true);
    } else {

      $(this).prop("checked", false);
    }
  });
};
jQuery(document).ready(function () {
  QueryPatent.init();
});
