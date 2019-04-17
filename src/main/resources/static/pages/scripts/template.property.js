Array.prototype.in_array = function (e) {
  var r = new RegExp(',' + e + ',');
  return (r.test(',' + this.join(this.S) + ','));
};
Array.prototype.indexOf = function (val) {
  for (var i = 0; i < this.length; i++) {
    if (this[i] == val) {
      return i;
    }
  }
  return -1;
};
Array.prototype.remove = function (val) {
  var index = this.indexOf(val);
  if (index > -1) {
    this.splice(index, 1);
  }
};

function insertHtml(attr_id) {
  var str = '<div id="div_' + attr_id + '"  class="col-md-6" style="line-height: 1;">'
      + '  <input type="hidden" id="' + attr_id + '" value="0" class="form-control"/>'
      + '  <div class="mt-repeater-input" style="width: 250px;">'
      + '    <label class="control-label" id="attr_lable_' + attr_id + '"><strong>属性' + attr_id + '</strong></label>'
      + '    <br/>'
      + '    <input name="title" type="text" id="attr_name_' + attr_id + '" class="form-control mt-repeater-input-inline" placeholder="属性名称"/></div>'
      + '  <div class="mt-repeater-input">'
      + '    <label class="control-label"><strong>属性类型</strong></label>'
      + '    <select name="type" id="attr_type_' + attr_id + '" class="form-control" onchange="TemplateProperty.changeType(' + attr_id + ')">'
      + '      <option value="0" selected>文本</option>'
      + '      <option value="1">富文本</option>'
      + '      <option value="2">列表</option>'
      + '      <option value="3">文件</option>'
      + '      <option value="4">数值</option>'
      + '      <option value="5">日期</option>'
      + '      <option value="6">计算</option>'
      + '      <option value="7">系统配置</option>'
      + '    </select>'
      + '  </div>'
      + '  <div class="mt-repeater-input" id="rule_' + attr_id + '" style="display: none">'
      + '    <label class="control-label"><strong>规则</strong></label>'
      + '    <div style="display:table-cell;width: 110px;">'
      + '    <select name="rule-1" id="rule_f_' + attr_id + '" class="form-control"/>'
      + '    </div>'
      + '    <div style="display:table-cell;width: 100px;">'
      + '    <select name="rule-sign" id="rule_sign_' + attr_id + '" class="form-control">'
      + '      <option value="0">+</option>'
      + '      <option value="1">-</option>'
      + '      <option value="2">&times;</option>'
      + '      <option value="3">&divide;</option>'
      + '      <option value="4">天数差</option>'
      + '    </select>'
      + '    </div>'
      + '    <div style="display:table-cell;width: 110px;">'
      + '    <select name="rule-2" id="rule_s_' + attr_id + '" class="form-control"/>'
      + '    </div>'
      + '  </div>'
      + '  <div class="mt-repeater-input" id="select_' + attr_id + '"  style="display: none">'
      + '    <label class="control-label" id="select_lable_' + attr_id + '"><strong>选项</strong></label>'
      + '    <div style="display:table-cell;">'
      + '    <input type="text" id="select_value_' + attr_id + '" class="form-control mt-repeater-input-inline"'
      + '        placeholder="使用英文分号分隔添加"/></div>'
      + '    <div style="display:table-cell;">'
      + '    <button id="b_' + attr_id + '" type="button" class="btn btn-info" data-toggle="confirmation" onclick="TemplateProperty.review(' + attr_id
      + ')"   >预览</button>'
      + '    </div>'
      + '  </div>'
      + '  <div class="mt-repeater-input">'
      + '    <button type="button" class="btn btn-circle btn-danger mt-repeater-delete" '
      + 'id="attr_del_' + attr_id + '" onclick="TemplateProperty.delProperty(' + attr_id + ')">'
      + '      <i class="fa fa-close"></i>删除</button>'
      + '  </div>'

      + '</div>';

  return str;
};

var TemplateProperty = function () {
  var count = 1;
  var atts_4 = new Array();
  var atts_5 = new Array();
  var atts_6 = new Array();
  var ids = new Array();
  var state = 0;

  return {
    init: function () {
      var id = $("#templateID").val();
      if (id != "0") {
        $.ajax({
          url: http_request + "/getTemplate",
          data: {
            "id": id
          }, success: function (data) {
            state = data.info.state;
            var values = new Array();
            $("#name").val(data.info.name);
            $("#description").val(data.info.description);
            var atts = data.atts;
            for (var i = 0; i < atts.length; i++) {
              if (atts[i].type == 6) {
                var rule = new Object();
                rule.id = atts[i].sortId;
                rule.value = atts[i].value;
                values.push(rule);
              }
              TemplateProperty.readProperty(atts[i].name, atts[i].type, atts[i].sortId, atts[i].value);
            }
            for (var i = 0; i < values.length; i++) {
              $("#rule_f_" + values[i].id).append("<option value='0'>系统时间</option>");
              $("#rule_s_" + values[i].id).append("<option value='0'>系统时间</option>");
              for (var j = 0; j < atts_4.length; j++) {
                $("#rule_f_" + values[i].id).append("<option value='" + atts_4[j] + "'>属性" + atts_4[j] + "</option>");
                $("#rule_s_" + values[i].id).append("<option value='" + atts_4[j] + "'>属性" + atts_4[j] + "</option>");
              }
              var rules = values[i].value.split(";");
              $("#rule_f_" + values[i].id).val(rules[0]);
              $("#rule_sign_" + values[i].id).val(rules[1]);
              $("#rule_s_" + values[i].id).val(rules[2]);
            }

            if (state == 2) {
              for (var i = 0; i < atts.length; i++) {
                $('#attr_name_'+atts[i].sortId).attr("disabled","disabled");
                $('#attr_type_'+atts[i].sortId).attr("disabled","disabled");
                $('#rule_f_'+atts[i].sortId).attr("disabled","disabled");
                $('#rule_sign_'+atts[i].sortId).attr("disabled","disabled");
                $('#rule_s_'+atts[i].sortId).attr("disabled","disabled");
                $('#select_value_'+atts[i].sortId).attr("disabled","disabled");

                $('#attr_del_'+atts[i].sortId).attr("disabled","disabled");
              }
            }

          }
        });
      }

    },

    readProperty: function (name, type, sortId, value) {
      $("#group-a").append(insertHtml(sortId));
      $("#attr_name_" + sortId).val(name);
      $("#attr_type_" + sortId).val(type);
      if (state == 1) {
        $("#attr_del_" + sortId).attr("disabled", true);
      }

      switch (type) {
        case 2:
          $("#select_" + sortId).show();
          $("#select_value_" + sortId).val(value);

          break;
        case 4:
          atts_4.push(sortId);
          break;
        case 5:
          atts_4.push(sortId);
          atts_5.push(sortId);
          break;
        case 6:
          $("#rule_" + sortId).show();

          atts_6.push(sortId);
          break;
        default:

      }

      ids.push(sortId);
      count = sortId + 1;
    },
    addProperty: function () {
      $("#group-a").append(insertHtml(count));
      var id = count;
      ids.push(id);
      ++count;
    },
    changeType: function (attr_id) {
      var type = $("#attr_type_" + attr_id).val();
      switch (type) {
        case  "2":
          $("#rule_" + attr_id).hide();
          $("#select_" + attr_id).show();

          break;
        case "4":

          if (!atts_4.in_array(attr_id)) {
            atts_4.push(attr_id);
            atts_4.sort();
          }
          if (atts_5.in_array(attr_id)) {
            atts_5.remove(attr_id);
          }
          if (atts_6.in_array(attr_id)) {
            atts_6.remove(attr_id);
          }

          for (var j = 0; j < atts_6.length; j++) {
            $("#rule_f_" + atts_6[j]).append("<option value='" + attr_id + "'>属性" + attr_id + "</option>");
            $("#rule_s_" + atts_6[j]).append("<option value='" + attr_id + "'>属性" + attr_id + "</option>");
          }
          $("#rule_" + attr_id).hide();
          $("#select_" + attr_id).hide();
          break;
        case "5":

          if (!atts_5.in_array(attr_id)) {
            atts_5.push(attr_id);
            atts_5.sort();
          }
          if (!atts_4.in_array(attr_id)) {
            atts_4.push(attr_id);
            atts_4.sort();
          }
          if (atts_6.in_array(attr_id)) {
            atts_6.remove(attr_id);

          }

          for (var j = 0; j < atts_6.length; j++) {
            $("#rule_f_" + atts_6[j]).append("<option value='" + attr_id + "'>属性" + attr_id + "</option>");
            $("#rule_s_" + atts_6[j]).append("<option value='" + attr_id + "'>属性" + attr_id + "</option>");
          }

          $("#rule_" + attr_id).hide();
          $("#select_" + attr_id).hide();
          break;
        case "6":
          for (var j = 0; j < atts_6.length; j++) {
            $("#rule_f_" + atts_6[j] + " option[value='" + attr_id + "']").remove();
            $("#rule_s_" + atts_6[j] + " option[value='" + attr_id + "']").remove();
          }
          if (!atts_6.in_array(attr_id)) {
            atts_6.push(attr_id);
            atts_6.sort();
          }
          if (atts_5.in_array(attr_id)) {
            atts_5.remove(attr_id);
          }
          if (atts_4.in_array(attr_id)) {
            atts_4.remove(attr_id);
          }

          $("#rule_f_" + attr_id).empty();
          $("#rule_s_" + attr_id).empty();
          $("#rule_f_" + attr_id).append("<option value='0'>系统时间</option>");
          $("#rule_s_" + attr_id).append("<option value='0'>系统时间</option>");
          for (var j = 0; j < atts_4.length; j++) {
            $("#rule_f_" + attr_id).append("<option value='" + atts_4[j] + "'>属性" + atts_4[j] + "</option>");
            $("#rule_s_" + attr_id).append("<option value='" + atts_4[j] + "'>属性" + atts_4[j] + "</option>");
          }

          $("#rule_" + attr_id).show();
          $("#select_" + attr_id).hide();
          break;
        default:
          if (atts_4.in_array(attr_id)) {
            atts_4.remove(attr_id);
          }
          if (atts_5.in_array(attr_id)) {
            atts_5.remove(attr_id);
          }
          if (atts_6.in_array(attr_id)) {
            atts_6.remove(attr_id);
          }
          $("#rule_" + attr_id).hide();
          $("#select_" + attr_id).hide();
      }

    },
    delProperty: function (attr_id) {
      $("#div_" + attr_id).remove();

      if (atts_5.in_array(attr_id)) {
        atts_5.remove(attr_id);
      }
      if (atts_6.in_array(attr_id)) {
        atts_6.remove(attr_id);
      }
      if (atts_4.in_array(attr_id)) {
        atts_4.remove(attr_id);
      }
      for (var j = 0; j < atts_6.length; j++) {
        $("#rule_f_" + atts_6[j] + " option[value='" + attr_id + "']").remove();
        $("#rule_s_" + atts_6[j] + " option[value='" + attr_id + "']").remove();
      }
      ids.remove(attr_id);
    },
    save: function () {
      var id = $("#templateID").val();
      var name = $("#name").val();
      var description = $("#description").val();
      var atts = [];
      for (var num = 0; num < ids.length; num++) {
        var property = {};
        property.name = $("#attr_name_" + ids[num]).val();
        property.type = $("#attr_type_" + ids[num]).val();
        property.sortId = ids[num];
        switch (property.type) {
          case "2":
            property.value = $("#select_value_" + ids[num]).val();
            break;
          case "6":
            property.value = $("#rule_f_" + ids[num]).val() + ";" + $("#rule_sign_" + ids[num]).val() + ";" + $("#rule_s_" + ids[num]).val();
            break;
        }

        atts.push(property)
      }

      var data = JSON.stringify(atts);

      $.ajax({
        url: http_request + "/saveTemplate",
        method: "POST",
        dataType: "json",
        data: {
          "id": id,
          "name": name,
          "description": description,
          "data": data
        }, success: function (data) {
          //window.location.href="/template";
          notify('', '保存 ' + data + ' 成功', '', '');

        }
      });
    },
    review: function (attr_id) {

      $('#b_' + attr_id).popover('destroy');
      // var values = $("#select_value_"+ attr_id).val().split(";");
      // $("#options").empty();
      // for (var i = 0;i < values.length;i++){
      //   $("#options").append("<option value='"+i+"'>"+values[i]+"</option>");
      // }
      // $("#select_dialog").modal();
      var options = '';
      var values = $("#select_value_" + attr_id).val().split(";");
      for (var i = 0; i < values.length; i++) {
        options = options + '<option value="' + i + '">' + values[i] + '</option>';
      }

      var template = [
        '<div class="popover" >',
        '<div class="popover-content"></div>',
        '</div>'
      ].join("");

      var content = [
        '<div style="line-height: 1;"><select id="options" class="form-control">' + options
        + '</select></div>'
      ].join("");

      $('#b_' + attr_id).popover({
        html: true,
        trigger: "click",
        placement: 'top',
        content: content,
        template: template
      });
      //$('[data-toggle="confirmation"]').popover('toggle');
      $('#b_' + attr_id).popover('toggle');
    }
  };

}();

jQuery(document).ready(function () {
  TemplateProperty.init();

});
