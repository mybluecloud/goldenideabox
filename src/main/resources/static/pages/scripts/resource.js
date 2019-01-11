var table;
var Resource = function () {

  return {

    loadResourceList: function () {
      table = $('#resourcelist').DataTable({
        "ajax": http_request + "/resources",
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
            "data": "resUrl",
            "title": "资源路径"
          },
          {
            "data": "type",
            "title": "类型"
          },
          {
            "data": "parentId",
            "title": "父资源ID"
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

              return action1+action2;
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
      this.loadResourceList();


    }
  };

}();

function del(id) {
  swal("确定删除该资源以及对应的权限信息？",{
    buttons: {
      yes:"确定",
      cancel:"取消",
    },
  }).then(function(result) {

    if (result == "yes") {
      $.ajax({
        url: http_request + "/delresource",
        data: {
          "id": id
        }, success: function (data) {
          notify('','删除资源成功','','');
          table.ajax.reload();
        }
      });
    }
  });

};

function edit(id) {
  $.ajax({
    url: http_request + "/queryresource",
    data: {
      "id": id
    }, success: function (data) {
      //console.info(data);
      initModal(id,data.resource.name,data.resource.resUrl,data.resource.type,data.resource.parentId,data.role);

    }
  });
};

function save() {
  var id = $('#resources_id').val();
  var name = $('#resources_name').val();
  var rurl = $('#resources_url').val();
  var type = $('#resources_type').val();
  var prurl = $('#resources_parent_url').val();


  var roles = '';
  if ($('#select_role').val() != null) {
    roles = $('#select_role').val().join(',');
  }

  var url;
  if (id == 0) {
    url = http_request + "/addresource";
  } else {
    url = http_request + "/updateresource";
  }

  $.ajax({
    url: url,
    data: {
      "id": id,
      "name":name,
      "url":rurl,
      "type":type,
      "parent_id":prurl,
      "roles":roles
    }, success: function (data) {
      notify('','更新资源 '+name+' 成功','','');
      table.ajax.reload();
    }
  });

};
function newResource() {
  initModal(0,'','','','','');
};
function initModal(id,name,rurl,type,prurl,result){





  $('#resources_id').val(id);
  $('#resources_name').val(name);
  $('#resources_url').val(rurl);
  $('#resources_type').val(type);
  $('#resources_parent_url').val(prurl);
  if (id == 0){
    document.getElementById('title').innerText="新建资源";

  } else {
    document.getElementById('title').innerText="修改资源";
  }
  $.ajax({
    url: http_request + "/roles",
    success: function (data) {

      var map = {};
      for (var i = 0; i < result.length; i++) {
        map[result[i]['id']] = result[i];
      }
      $("#select_role").empty();
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
      $('#select_role').append(option);
      $('#select_role').multiSelect({
        selectableHeader: "<div  style='background-color: #67809F;color: #FFFFFF;padding: 5px;text-align: center;'>未添加角色</div>",
        selectionHeader: "<div  style='background-color: #67809F;color: #FFFFFF;padding: 5px;text-align: center;'>已添加角色</div>",

      });
      $('#select_role').multiSelect("refresh");

    }
  });

  $('#resource_dialog').modal();
};
jQuery(document).ready(function () {
  Resource.init();
});
