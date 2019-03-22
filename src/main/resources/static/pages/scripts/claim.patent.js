var table;

var ClaimPatent = function () {

  return {
    loadOrderList: function () {
      table = $('#orderlist').DataTable({
        "ajax": http_request + "/patent/claimorder",
        columns: [
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
          },
          {
            "data": null,
            "title": "操作",
            render: function (data, type, full, meta) {

              var action1 = "<a  href='" + http_request + "/patent/newpatent?orderid=" + data.id
                  + "' class='btn btn-outline btn-circle btn-sm blue'><i class='fa fa-edit'></i>新建案件</a>";
              // var action2 = "<button  onclick='del(" + data.id
              //     + ")' class='btn btn-outline btn-circle btn-sm red' ><i class='fa fa-edit'></i>删除</button>";

              return action1 ;
            }
          }
        ],
        language: {
          "url": http_request + "/global/plugins/datatables/Chinese.json"
        },
        "order": [
          [1, "asc"]
        ]
      });

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
  ClaimPatent.init();
});
