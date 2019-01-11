var table;
var Template = function () {

  return {

    loadTemplateList: function () {
      var review = false;
      if (document.getElementById("review")) {
        review = true;
      }
      table = $('#templatelist').DataTable({
        "ajax": {
          "url": http_request + "/getTemplates",
          "data": {
            "review": review
          }
        },
        columns: [

          {
            "data": "id",
            "title": "ID"
          },
          {
            "data": "name",
            "title": "名称"
          },
          {
            "data": "description",
            "title": "简介"
          },
          {
            "data": "state",
            "title": "状态",
            render: function (data, type, full, meta) {
              var str;
              switch (data) {
                case 1:
                  str = '<span class="label label-sm label-danger">审核中</span>';
                  break;
                case 2:
                  str = '<span class="label label-sm label-primary">公开</span>';
                  break;

                default:
                  str = '<span class="label label-sm label-default">待审核</span>';
              }

              return str;
            }
          },
          {
            "data": null,
            "title": "创建者",
            render: function (data, type, full, meta) {

              return data.creator.name;
            }
          },
          {
            "data": "time",
            "title": "创建时间",
            render: function (data, type, full, meta) {
              var dt = new Date(data);
              return dt.format("yyyy-MM-dd");
            }
          },
          {
            "data": null,
            "title": "操作",
            render: function (data, type, full, meta) {
              //console.info(data);
              if (data.state == 0) {
                var action1 = "<a href='" + http_request + "/editTemplate?id=" + data.id
                    + "' class='btn btn-outline btn-circle btn-sm blue'><i class='fa fa-edit'></i>编辑</a>";
                var action2 = "<a  onclick='del(" + data.id
                    + ")' class='btn btn-outline btn-circle btn-sm red' ><i class='fa fa-trash'></i>删除</a>";
                var action3 = "<a  onclick='review(" + data.id
                    + ",1)' class='btn btn-outline btn-circle btn-sm green-jungle' ><i class='fa fa-qrcode'></i>提交审核</a>";

                return action1 + action2 + action3;
              } else if (data.state == 1) {
                var action1 = "<a href='" + http_request + "/editTemplate?id=" + data.id
                    + "' class='btn btn-outline btn-circle btn-sm blue'><i class='fa fa-edit'></i>查看</a>";
                var action2 = "<a onclick='review(" + data.id
                    + ",2)' class='btn btn-outline btn-circle btn-sm purple'><i class='fa fa-edit'></i>审核通过</a>";
                var action3 = "<a onclick='review(" + data.id
                    + ",0)' class='btn btn-outline btn-circle btn-sm purple'><i class='fa fa-edit'></i>审核驳回</a>";
                return action1 + action2 + action3;
              } else if (data.state == 2) {
                var action1 = "<a href='" + http_request + "/editTemplate?id=" + data.id
                    + "' class='btn btn-outline btn-circle btn-sm blue'><i class='fa fa-edit'></i>编辑</a>";
                var action2 = "<a  onclick='del(" + data.id
                    + ")' class='btn btn-outline btn-circle btn-sm red' ><i class='fa fa-trash'></i>删除</a>";
                var action3 = "<a  onclick='permitTemplate(" + data.id
                    + ")' class='btn btn-outline btn-circle btn-sm green-jungle' ><i class='fa fa-edit'></i>权限设置</a>";
                var action4 = "<a  onclick='copyTemplate(" + data.id
                    + ")' class='btn btn-outline btn-circle btn-sm green' ><i class='fa fa-copy'></i>模板拷贝</a>";
                return action1 + action2 + action3 + action4;
              }

            }
          }
        ],
        language: {
          "url": http_request + "/global/plugins/datatables/Chinese.json"
        },
        "order": [
          [0, "asc"]
        ]

      });

    },

    init: function () {
      if (!jQuery().dataTable) {
        return;
      }
      this.loadTemplateList();

    }
  };

}();

function del(id) {
  swal({
    title:'确定删除该模板以及对应的权限信息？',
    showCancelButton: true,
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(function (result) {

    if (result.value) {
      $.ajax({
        url: http_request + "/delTemplate",
        data: {
          "id": id
        }, success: function (data) {
          notify('', '操作成功', '', '');
          table.ajax.reload();
        }
      });
    }
  });

}

function review(id, state) {
  $.ajax({
    url: http_request + "/reviewTemplate",
    data: {
      "id": id,
      "state": state
    }, success: function (data) {
      notify('', '审核成功', '', '');
      table.ajax.reload();
    }
  });
}

function permitTemplate(id) {
  $.ajax({
    url: http_request + "/permitTemplate",
    data: {
      "id": id
    }, success: function (data) {
      //console.info(data);

      $("#permit_multi_select").empty();
      $("#templateID").val(id);
      var option = "";
      for (var i = 0; i < data.user.length; i++) {

        option = option + "<option value='" + data.user[i].id + "'>" + data.user[i].name
            + "</option>";

      }
      for (var i = 0; i < data.permit.length; i++) {

        option = option + "<option value='" + data.permit[i].id + "' selected>"
            + data.permit[i].name
            + "</option>";

      }

      $('#permit_multi_select').append(option);
      $('#permit_multi_select').multiSelect({
        selectableHeader: "<div style='background-color: #67809F;color: #FFFFFF;padding: 5px;text-align: center;'>未授权用户</div>",
        selectionHeader: "<div style='background-color: #67809F;color: #FFFFFF;padding: 5px;text-align: center;'>已授权用户</div>",

      });
      $('#permit_multi_select').multiSelect("refresh");
      $('#permit_dialog').modal();
    }
  });
}

function copyTemplate(id) {
  $.ajax({
    url: http_request + "/copyTemplate",
    data: {
      "id": id
    }, success: function (data) {
      //console.info(data);
      notify('', '拷贝成功', '', '');
      table.ajax.reload();

    }
  });
}

function permit() {
  var ids = $('#permit_multi_select').val().join(',');
  $.ajax({
    url: http_request + "/permit",
    data: {
      "id": $("#templateID").val(),
      "permit": ids
    }, success: function (data) {
      notify('', '模板授权成功', '', '');
    }
  });

}

jQuery(document).ready(function () {

  Template.init();
});
