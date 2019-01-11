var table;
var selectIDs = "";

function showUser() {
  var elements = document.getElementsByClassName("checkboxes");
  var ids = "";
  selectIDs = "";
  for (var i = 0; i < elements.length; i++) {
    if (elements[i].checked) {
      selectIDs = selectIDs + elements[i].value + ',';
    }
    ids = ids + elements[i].value + ',';
  }
  if (selectIDs.length == 0) {
    if (confirm("未选择记录则默认全部分配，是否继续？")) {
      selectIDs = ids;
    } else {
      return;
    }
  }
  $.ajax({
    url: http_request + "/patent/users",
    data: {
      "resUrl": "/patent/claim"
    },
    success: function (data) {
      $('.icheck-inline').empty();

      for (var i = 0; i < data.data.length; i++) {
        var option = '<label style="line-height: 15px;padding-top: 10px;">'
            + '<input type="radio" name="iCheck" value="' + data.data[i].id + '">'
            + data.data[i].name + '<span  class="badge badge-info">' + data.data[i].patentCount + '</span>'
            + '</label>';
        // option = option
        //     + "<label><input type='radio' value='" + data.data[i].id + "' name='iCheck' class='icheck'  data-radio='iradio_square-grey'>"
        //     + data.data[i].name + "<span  class='badge badge-info'>"
        //     + data.data[i].patentCount + "</span></label>";
        $('.icheck-inline').append(option);
      }
      $('.icheck-inline input').iCheck({
        radioClass: 'iradio_square-blue',
        increaseArea: '20%' // optional
      });



    }
  });

  $("#user_dialog").modal();
};

function appoint() {

  var id = $("input[name='iCheck']:checked").val();
  $.ajax({
    url: http_request + "/patent/appoint",
    data: {
      "userID": id,
      "orderIDs": selectIDs
    },
    success: function (data) {
      notify('', '指派案件成功', '', '');
      table.ajax.reload();
    }
  });
};
var AppointPatent = function () {

  return {
    loadOrderList: function () {

      table = $('#orderlist').DataTable({
        "ajax": http_request + "/patent/appointorder",

        columns: [
          {
            "data": null,
            "title": '<input type="checkbox" class="group-checkable" onchange="checkable()" >',
            orderable: false,
            width: "1%",
            render: function (data, type, full, meta) {

              return '<input type="checkbox" class="checkboxes" value="'
                  + data.id + '" >';
            }
          },

          {
            "data": "orderNum",
            "title": "订单号"
          },
          {
            "data": "time",
            "title": "创建时间"
          },
          {
            "data": "techName",
            "title": "技术名称"
          }
        ],
        language: {
          "url": http_request + "/global/plugins/datatables/Chinese.json"
        },
        "order": [
          [1, "asc"]
        ]
      });

      $('#orderlist').on( 'page.dt', function () {
        if ($('.group-checkable').prop("checked")) {
          $('.group-checkable').prop("checked",false);
        }
      } );
    },
    init: function () {
      if (!jQuery().dataTable) {
        return;
      }
      this.loadOrderList();
    }
  };

}();

jQuery(document).ready(function () {
  AppointPatent.init();
});
