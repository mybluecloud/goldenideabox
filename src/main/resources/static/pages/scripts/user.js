var table;
var User = function () {

  return {

    loadUserList: function (state) {
      if (table != null) {
        table.destroy();
        $('#userlist').empty();
      }
      table = $('#userlist').DataTable({
        "ajax": {
          "url": http_request + "/users",
          "data": {
            "state": state
          }
        },
        columns: [
          {
            "data": null,
            "title": '<input class="group-checkable" onchange="checkable()" type="checkbox"  >',
            orderable: false,
            width: "1%",
            render: function (data, type, full, meta) {

              return '<input type="checkbox"  class="checkboxes" value="'
                  + data.id + '" >';
            }
          },
          {
            "data": "account",
            "title": "账户"
          },
          {
            "data": "name",
            "title": "姓名"
          },
          {
            "data": "sex",
            "title": "性别",
            render: function (data, type, full, meta) {
              var str;
              switch (data) {
                case 1:
                  str = '<span class="label label-sm label-primary">男</span>';
                  break;
                case 2:
                  str = '<span class="label label-sm label-danger">女</span>';
                  break;
                default:
                  str = '<span class="label label-sm label-default">保密</span>';
              }

              return str;
            }
          },
          {
            "data": "phone",
            "title": "手机号"
          },
          {
            "data": "wechat",
            "title": "微信"
          },
          {
            "data": "email",
            "title": "电子邮件"
          },
          {
            "data": "introduction",
            "title": "介绍"
          },
          {
            "data": "lastedLoginTime",
            "title": "上次登录时间",
            render: function (data, type, full, meta) {
              var dt = new Date(data);
              return dt.format("yyyy-MM-dd hh:mm:ss");
            }
          },
          {
            "data": "state",
            "title": "状态",
            render: function (data, type, full, meta) {
              var str;
              switch (data) {
                case 0:
                  str = '<span class="label label-sm label-success">正常</span>';
                  break;
                case 1:
                  str = '<span class="label label-sm label-info">审核</span>';
                  break;
                case 2:
                  str = '<span class="label label-sm label-warning">锁定</span>';
                  break;
                default:
                  str = '<span class="label label-sm label-default">异常</span>';
              }

              return str;
            }
          },
          {
            "data": null,
            "title": "操作",
            render: function (data, type, full, meta) {
              //console.info(data);
              var action;
              switch (data.state) {
                case 0:
                  action = "<a href='#' onclick='changeState(" + data.id
                      + ",2)' class='btn btn-outline btn-circle btn-sm red'><i class='fa fa-edit'></i>锁定</a>";
                  break;
                case 1:
                  action = "<a href='#' onclick='changeState(" + data.id
                      + ",0)' class='btn btn-outline btn-circle btn-sm blue'><i class='fa fa-edit'></i>通过</a>";
                  break;
                case 2:
                  action = "<a href='#' onclick='changeState(" + data.id
                      + ",0)' class='btn btn-outline btn-circle btn-sm blue'><i class='fa fa-edit'></i>解锁</a>";
                  break;
                default:
                  action = "<a href='#' onclick='changeState(" + data.id
                      + ",0)' class='btn btn-outline btn-circle btn-sm blue'><i class='fa fa-edit'></i>恢复</a>";
              }

              return action;
            }
          }
        ],

        // "createdRow": function (row, data, index) {
        //   /* 设置表格中的内容居中 */
        //   $('td', row).attr("class", "text-center");
        // },
        language: {
          "url": http_request + "/global/plugins/datatables/Chinese.json"
        },
        "order": [
          [1, "asc"]
        ]
        //"ordering": false,

        //"autoWidth": false,

      });

      $('#userlist').on( 'page.dt', function () {
        if ($('.group-checkable').prop("checked")) {
          $('.group-checkable').prop("checked",false);
        }
      } );

    },
    init: function () {
      if (!jQuery().dataTable) {
        return;
      }
      this.loadUserList(0);

      $('#state').on('change', function () {

        User.loadUserList($(this).val());
      });

    }
  };

}();

function del(id) {
  $.ajax({
    url: http_request + "/deluser",
    data: {
      "id": id
    }, success: function (data) {
      table.ajax.reload();
      //console.log("删除成功" + data);
    }
  });
};

function changeState(id, state) {
  $.ajax({
    url: http_request + "/changestate",
    data: {
      "id": id,
      "state": state
    }, success: function (data) {
      table.ajax.reload();
      //console.log("删除成功" + data);
    }
  });
};



jQuery(document).ready(function () {
  User.init();

});
