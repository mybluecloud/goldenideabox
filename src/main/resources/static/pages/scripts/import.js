var titles;
var config;
var properties;
var bSave = false;
var rows = [];
var table;
var FormWizard = function () {

  return {
    //main function to initiate the module
    init: function () {
      if (!jQuery().bootstrapWizard) {
        return;
      }

      $('#fileupload').fileupload({
        url: http_request + "/special/fileupload",
        autoUpload: false,
        dataType: 'json',
        maxFileSize: 524288000,

        add: function (e, data) {
          $('.fileinput-filename').empty();
          $('.fileinput-filename').append(data.files[0].name);
          $('.button-next').data(data);
          //data.submit();
        },
        done: function (e, data) {

          $('#file').val(data.result.file);
          $.each(data.result.sheets, function (index, sheet) {
            $('#sheet').append('<option value="' + index + '">' + sheet + '</option>');
          });
          App.unblockUI('#form_wizard_1');
        }
      });

      var handleTitle = function (tab, navigation, index) {
        var total = navigation.find('li').length;
        var current = index + 1;
        // set done steps
        jQuery('li', $('#form_wizard_1')).removeClass("done");
        var li_list = navigation.find('li');
        for (var i = 0; i < index; i++) {
          jQuery(li_list[i]).addClass("done");
        }

        if (current >= total) {
          $('#form_wizard_1').find('.button-next').hide();
          $('#form_wizard_1').find('.button-submit').show();

        } else {
          $('#form_wizard_1').find('.button-next').show();
          $('#form_wizard_1').find('.button-submit').hide();
        }
        App.scrollTo($('.page-title'));
      }

      var handelTemplate = function () {
        App.blockUI({
          target: '#form_wizard_1',
          boxed: true
        });

        $('.button-next').data().submit();

      };

      var handleTemplateConfig = function () {
        App.blockUI({
          target: '#form_wizard_1',
          boxed: true
        });
        $.ajax({
          url: http_request + "/special/parseTemplate",
          data: {
            "templateID": $("#template").val(),
            "sheetID": $("#sheet").val(),
            "file": $('#file').val()
          },
          error: function (XMLHttpRequest, textStatus, errorThrown) {

            App.unblockUI('#form_wizard_1');
            Swal(
              '设置错误!',
              '请刷新页面重新导入',
              'error'
            )
          },
          success: function (data) {

            titles = data.titles;
            properties = data.props;
            config = data.config;

            $.each(data.props, function (index, prop) {
              if (index % 2 == 0) {
                $("#property").append("<tr></tr>");
              }
              if (prop.type != 6) {

                var code = '<td width="15%">' + prop.name
                  + '</td><td width="30%">'
                  + '<select class="form-control" id="title-' + prop.sortId + '" onchange="changeOption(this);"></select></td>';

                $("#property").children('tr:last-child').append(code);
              }

            });

            renderSelect(null);

            App.unblockUI('#form_wizard_1');
          }
        });
      };

      var tableInit = function (data) {

        var select = [{
          "title": '<input type="checkbox" class="group-checkable" onchange="checkAll(this)" >',
          width: "20px",
          "defaultContent": ""
        }];

        var respon = [{
          "title": '操作',
          width: "20px",
          "defaultContent": ""
        }];
        $.merge(data.columns, respon);
        $.merge(select, data.columns);

        table = $('#patentlist').DataTable({
          data: data.datas,
          ordering: false,
          columns: select,
          columnDefs: [{
            orderable: false,
            className: 'select-checkbox',
            targets: 0
          }, {
            className: 'control',
            orderable: false,
            targets: -1
          }],
          select: {
            style: 'multi',
            selector: 'td:first-child'
          },
          responsive: {
            details: {
              type: 'column',
              target: -1,
              renderer: function (api, rowIdx, columns) {

                var patent = api.rows().data()[rowIdx].patent;
                if (patent == undefined) {
                  return '';
                }
                var patentData = JSON.parse(patent).props;

                var indexs = patentData.map(function (prop, index) {
                  return prop.sortId;
                });

                var data = $.map(columns, function (col, i) {

                  var index = indexs.indexOf(api.columns([i]).dataSrc().join(''));
                  if (index < 0) {
                    if ((col.data == undefined || col.data == null || col.data == '') || i < 2) {

                      return '';
                    } else {
                      var code = '<tr data-dt-row="' + col.rowIndex + '" data-dt-column="' + col.columnIndex + '">' +
                        '<td style="width: 20%;">' + col.title + ':' + '</td> ' +
                        '<td style="width: 40%;">' + col.data + '</td>' +
                        '<td style="width: 40%;"></td>' +
                        '</tr>';
                      return code;
                    }

                  } else {
                    var prpo = patentData[index];
                    var value = '';
                    if (prpo != undefined) {
                      value = prpo.value;
                    }

                    if ((value == undefined || value == null || value == '') &&
                      (col.data == undefined || col.data == null || col.data == '') ||
                      $.trim(value) == $.trim(col.data)) {

                      return '';
                    }
                    var code = '<tr data-dt-row="' + col.rowIndex + '" data-dt-column="' + col.columnIndex + '">' +
                      '<td style="width: 20%;">' + col.title + ':' + '</td> ' +
                      '<td style="width: 40%;">' + col.data + '</td>' +
                      '<td style="width: 40%;">' + value + '</td>' +
                      '</tr>';
                    return code;
                  }

                }).join('');

                return data ?
                  $('<table class="table table-striped table-bordered nowrap"/>').append(data) :
                  false;
              }
            }
          },

          language: {
            "url": http_request + "/global/plugins/datatables/Chinese.json"
          }
        });

      };

      var handleDataConfig = function () {
        App.blockUI({
          target: '#form_wizard_1',
          boxed: true
        });
        $.ajax({
          url: http_request + "/special/parseData",
          type: "POST",
          data: {
            "templateID": $("#template").val(),
            "sheetID": $("#sheet").val(),
            "file": $('#file').val(),
            "config": JSON.stringify(config),
            "save": bSave,
            "filter": 2
          },
          success: function (data) {
            //console.info(data);

            tableInit(data);
            App.unblockUI('#form_wizard_1');
          }
        });
      };

      var handleDataSubmit = function () {
        App.blockUI({
          target: '#form_wizard_1',
          boxed: true
        });
        var datas = table.rows({selected: true}).data();
        var patents = [];
        for (var i = 0; i < datas.length; i++) {
          var patent = {};
          patent.row = datas[i].id;
          if (datas[i].patent == undefined) {
            patent.id = 0;
          } else {
            patent.id = JSON.parse(datas[i].patent).id;
          }

          patents.push(patent);
        }
        if (patents.length > 0) {
          $.ajax({
            url: http_request + "/special/importData",
            type: "POST",
            data: {
              "templateID": $("#template").val(),
              "sheetID": $("#sheet").val(),
              "file": $('#file').val(),
              "config": JSON.stringify(config),
              "patents": JSON.stringify(patents)
            },
            success: function (data) {
              App.unblockUI('#form_wizard_1');

              if (data.error == undefined) {
                Swal(
                  '导入完成!',
                  '',
                  'success'
                )
              } else {
                Swal(
                  '导入失败!',
                  '请按F12在Console中查看错误信息，并告知管理员',
                  'error'
                )
              }

            }
          });
        }

      };

      // default form wizard
      $('#form_wizard_1').bootstrapWizard({
        'nextSelector': '.button-next',
        'previousSelector': '.button-previous',
        onTabClick: function (tab, navigation, index, clickedIndex) {
          return false;

        },
        onNext: function (tab, navigation, index) {

          if (index == 1) {
            handelTemplate();
          } else if (index == 2) {
            handleTemplateConfig();
          } else if (index == 3) {
            handleDataConfig();
          } else if (index == 4) {
            var state = handleDataSubmit();
            console.info(state);
          }

          handleTitle(tab, navigation, index);
        },
        onPrevious: function (tab, navigation, index) {
          return false;
        },
        onTabShow: function (tab, navigation, index) {
          var total = navigation.find('li').length;
          var current = index + 1;
          var $percent = (current / total) * 100;
          $('#form_wizard_1').find('.progress-bar').css({
            width: $percent + '%'
          });
        }
      });

      $('#form_wizard_1').find('.button-previous').hide();
      $('#form_wizard_1 .button-submit').click(function () {
        window.location.reload();

      }).hide();

    }

  };

}();

var checkAll = function (obj) {

  var checked = $(obj).is(':checked');
  if (checked) {
    table.rows().select();
  } else {
    table.rows().deselect();
  }

};

var renderSelect = function (obj) {

  $('#property select').each(function () {
    if (this != obj) {
      $(this).empty();
    }

  });

  $.each(config, function (index, rule) {

    if (obj != null) {
      var id = $(obj).attr('id').substr(6);
      if (id != rule.sortId) {
        $('#title-' + rule.sortId).append('<option>' + rule.name + '</option>')

        titles.remove(rule.name);
      }
    } else {
      if (titles.indexOf(rule.name) >= 0) {
        $('#title-' + rule.sortId).append('<option>' + rule.name + '</option>')
        titles.remove(rule.name);
      }

    }

  });

  $('#property select').each(function () {
    if (this != obj) {
      var select = this;
      $(select).append('<option></option>');
      $.each(titles, function (index, title) {
        $(select).append('<option>' + title + '</option>');
      });
    }

  });
}

var changeOption = function (obj) {
  bSave = true;
  var id = parseInt($(obj).attr('id').substr(6));
  var value = $(obj).val();

  titles.remove(value);

  console.info(config);
  $.each(config, function (index, rule) {

    if (index >= config.length) {
      return false;
    }
    if (rule.sortId == id) {

      titles[titles.length] = rule.name;
      config.splice(index, 1);
      return false;
    }

  });
  console.info(config);
  var rule = {};
  rule.sortId = id;
  rule.name = value;
  config[config.length] = rule;
  console.info(config);
  setTimeout(function () {
    renderSelect(obj);
  }, 0);

};
jQuery(document).ready(function () {

  FormWizard.init();
});