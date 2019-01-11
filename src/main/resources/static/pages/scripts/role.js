var table;
var Role = function () {

  return {

    loadRoleList: function () {
      table = $('#rolelist').DataTable({
        "ajax": http_request + "/roles",
        columns: [
          {
            "data": "name",
            "title": "名称"
          },
          {
            "data": "remark",
            "title": "简介"
          },
          {
            "data": null,
            "title": "操作",
            render: function (data, type, full, meta) {
              //console.info(data);
              var action1 = "<button  onclick='edit(" + data.id
                  + ")' class='btn btn-outline btn-circle btn-sm blue'><i class='fa fa-edit'></i>编辑</button>";
              var action2 = "<button  onclick='del(" + data.id
                  + ")' class='btn btn-outline btn-circle btn-sm red' ><i class='fa fa-edit'></i>删除</button>";

              return action1 + action2;
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
          [0, "asc"]
        ]
        //"ordering": false,

        //"autoWidth": false,

      });

    },

    init: function () {
      if (!jQuery().dataTable) {
        return;
      }
      this.loadRoleList();

    }
  };

}();

function del(id) {
  swal({
    title:'确定删除该用户组以及对应的权限信息？',
    showCancelButton: true,
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(function (result) {

    if (result.value) {
      $.ajax({
        url: http_request + "/delrole",
        data: {
          "id": id
        }, success: function (data) {
          notify('', '删除用户组成功', '', '');
          table.ajax.reload();
        }
      });
    }
  });

};

function edit(id) {
  $.ajax({
    url: http_request + "/queryrole",
    data: {
      "id": id
    }, success: function (data) {

      initModal(id, data.role.name, data.role.remark, data.user);

    }
  });
};

function save() {
  var id = $('#role_id').val();
  var name = $('#role_name').val();
  var remark = $('#role_remark').val();
  var users = '';
  if ($('#select_user').val() != null) {
    users = $('#select_user').val().join(',');
  }

  var url;
  if (id == 0) {
    url = http_request + "/addrole";
  } else {
    url = http_request + "/updaterole";
  }

  $.ajax({
    url: url,
    data: {
      "id": id,
      "name": name,
      "remark": remark,
      "users": users
    }, success: function (data) {
      if (id == 0) {
        notify('', '新建用户组 ' + name + ' 成功', '', '');
      } else {
        notify('', '更新用户组 ' + name + ' 成功', '', '');
      }

      table.ajax.reload();
    }
  });

};

function newRole() {
  initModal(0, '', '', '');
};

function initModal(id, name, remark, result) {

  $('#role_id').val(id);
  $('#role_name').val(name);
  $('#role_remark').val(remark);
  if (id == 0) {
    document.getElementById('title').innerText = "新建用户组";

  } else {
    document.getElementById('title').innerText = "修改用户组";
  }
  $.ajax({
    url: http_request + "/users",
    data: {
      "state": 0
    },
    success: function (data) {
      var map = {};
      for (var i = 0; i < result.length; i++) {
        map[result[i]['id']] = result[i];
      }
      $("#select_user").empty();
      var option = "";
      for (var i = 0; i < data.data.length; i++) {
        if (map[data.data[i].id] != undefined) {
          option = option + "<option value='" + data.data[i].id + "'selected>" + data.data[i].name
              + "</option>";
        } else {
          option = option + "<option value='" + data.data[i].id + "'>" + data.data[i].name
              + "</option>";
        }

      }

      $('#select_user').append(option);
      $('#select_user').multiSelect({
        selectableHeader: "<div  style='background-color: #67809F;color: #FFFFFF;padding: 5px;text-align: center;'>未添加用户</div>",
        selectionHeader: "<div  style='background-color: #67809F;color: #FFFFFF;padding: 5px;text-align: center;'>已添加用户</div>",

      });
      $('#select_user').multiSelect("refresh");

    }
  });

  $('#role_dialog').modal();
};

jQuery(document).ready(function () {
  Role.init();
});
