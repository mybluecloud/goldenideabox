var PatentCpquery = function () {
  var ReviewData = {};
  var turnobj = null;
  var zoom = 1;

  function initImagePlugin(count) {

    turnobj = $('.flipbook').turn({
      width: 1024,
      height: 768,
      autoCenter: false
    });

    // $('#fb7-footer').css('opacity',1);
    // var all_width = $('.flipbook-viewport').width();
    // var center_w = $('#fb7-center').width();
    // var posX = all_width / 2 - center_w / 2;
    // $('#fb7-center').css('left', posX + 'px');
    //
    // initToolsEvent();
  }

  function initToolsEvent() {
    // Zoom Auto
    $('.fb7-zoom-auto').on('click', function () {
      zoom = 1;
      $(".flipbook").turn("zoom", zoom);
    });

    // Zoom In
    $('.fb7-zoom-in').on('click', function () {

      zoom = zoom * 2;
      $(".flipbook").turn("zoom", zoom);

    });

    // Zoom Out
    $('.fb7-zoom-out').on('click', function () {

      zoom = zoom / 2;

      $(".flipbook").turn("zoom", zoom);

    });
  }

  function clearImage() {
    //$('.flipbook').empty();
    if (turnobj != null) {
      $(".flipbook").turn("destroy");
    }
  }

  function loadImageData(num, imgUrl, desc) {
    console.info(num, imgUrl, desc);

    var page = '<div style="background-image:url(' + imgUrl + ')"></div>';
    if (imgUrl == '') {
      page = '<div>' + desc + '</div>';
    }
    $('.flipbook').append(page);

  }

  var handleApplicationData = function (data) {
    var projectChange = JSON.parse(data.projectChange);
    var records = '';
    if (projectChange != null) {
      projectChange.forEach(function (v) {
        var record = '<tr>'
          + '<td>' + v.biangengrq + '</td>'
          + '<td>' + v.biangengsx + '</td>'
          + '<td>' + v.biangengqnr + '</td>'
          + '<td>' + v.biangenghnr + '</td>'
          + '</tr>';
        records = records + record;
      });
    }

    var tpl = '<div class="portlet light">'
      + '  <div class="portlet-title">'
      + '    <div class="caption">'
      + '      <span class="caption-subject bold font-grey-gallery uppercase"> 著录项目信息 </span>'
      + '    </div>'
      + '    <div class="tools">'
      + '      <a href="" class="collapse"> </a>'
      + '    </div>'
      + '  </div>'
      + '  <div class="portlet-body">'
      + '    <table class="table table-bordered table-striped" style="background-color:#e5e5e5">'
      + '      <tbody></tbody>'
      + '      <tr>'
      + '        <td width="15%">申请号/专利号</td>'
      + '        <td width="35%">' + data.applicationNumber + '</td>'
      + '        <td width="15%">发明名称</td>'
      + '        <td width="35%"></td>'
      + '      </tr>'
      + '      <tr>'
      + '        <td width="15%">申请日</td>'
      + '        <td width="35%">' + data.applicationDate + '</td>'
      + '        <td width="15%">主分类号</td>'
      + '        <td width="35%">' + data.mainCategoryNumber + '</td>'
      + '      </tr>'
      + '      <tr>'
      + '        <td width="15%">案件状态</td>'
      + '        <td width="35%">' + data.caseStatus + '</td>'
      + '        <td width="15%">分案提交日</td>'
      + '        <td width="35%">' + data.submissionDate + '</td>'
      + '      </tr>'
      + '      <tr>'
      + '        <td width="15%">申请人</td>'
      + '        <td width="35%">' + data.applicant + '</td>'
      + '        <td width="15%">发明人/设计人</td>'
      + '        <td width="35%">' + data.inventor + '</td>'
      + '      </tr>'
      + '      <tr>'
      + '        <td width="15%">代理机构名称</td>'
      + '        <td width="35%">' + data.agency + '</td>'
      + '        <td width="15%">第一代理人</td>'
      + '        <td width="35%">' + data.firstAgent + '</td>'
      + '      </tr>'
      + '    </table>'
      + '  </div>'
      + '</div>'
      + '<div class="portlet light">'
      + '  <div class="portlet-title">'
      + '    <div class="caption">'
      + '      <span class="caption-subject bold font-grey-gallery uppercase"> 著录项目变更 </span>'
      + '    </div>'
      + '    <div class="tools">'
      + '      <a href="" class="collapse"> </a>'
      + '    </div>'
      + '  </div>'
      + '  <div class="portlet-body">'
      + '    <table class="table table-bordered table-striped table-condensed flip-content">'
      + '      <thead class="flip-content">'
      + '      <tr>'
      + '        <th> 变更生效日 </th>'
      + '        <th> 变更事项 </th>'
      + '        <th> 变更前 </th>'
      + '        <th> 变更后 </th>'
      + '      </tr>'
      + '      </thead>'
      + '      <tbody>' + records + '</tbody>'
      + '    </table>'
      + '  </div>'
      + '</div>';
    $('#portlet_tab_application').empty();
    $('#portlet_tab_application').append(tpl);
  }

  var handleReviewData = function (data) {
    ReviewData = {};

    data.forEach(function (item) {
      var code = '';

      ReviewData[item.id] = item;

      if (item.fileUrl == null) {
        code = '<li data-jstree=\'{ "type":"file","disabled":true,"icon" : "fa fa-warning icon-state-danger" }\' id="' + item.id + '"> '
          + item.fileName + ' </li>';
      } else if (item.fileUrl.length == 2) {
        code = '<li data-jstree=\'{"type":"file","disabled":true}\' id="' + item.id + '"> ' + item.fileName + ' </li>';
      } else {
        code = '<li data-jstree=\'{"type":"file"}\' id="' + item.id + '"> ' + item.fileName + '</li>';
      }

      switch (item.type.substr(0, 2)) {
        case '01':
          $('#01').append(code);
          break;
        case '02':
          $('#02').append(code);
          break;
        case '03':
          $('#03').append(code);
          break;
        case '04':
          $('#04').append(code);
          break;
        default:

      }
    });

    $('#fileTree').jstree({
      "core": {
        "themes": {
          "responsive": false
        }
      },
      "types": {
        "default": {
          "icon": "fa fa-folder icon-state-warning icon-lg"
        },
        "file": {
          "icon": "fa fa-file icon-state-warning icon-lg"
        }
      },
      "plugins": ["types"]
    });

    $('#fileTree').on('select_node.jstree', function (e, data) {
      var id = $('#' + data.selected).attr('id');

      if (ReviewData[id] == undefined || ReviewData[id] == null) {
        return;
      }
      clearImage();
      var filelst = JSON.parse(ReviewData[id].fileUrl);

      var count = 0;
      loadImageData(0, '', ReviewData[id].fileName);
      $.each(filelst, function (f) {
        for (var num in filelst[f]) {
          var url = http_request + '/document/' + ReviewData[id].applicationNumber + '/' + filelst[f][num];
          loadImageData(parseInt(num) + 1, url, '');
          count++;
        }

      });
      if (count % 2 == 1) {
        loadImageData(count + 1, '', '');
      }

      initImagePlugin(count);

    });

  }

  var handleCostData = function (data) {

    var payable = JSON.parse(data.payable);
    if (payable != null) {
      var payables = '';
      payable.forEach(function (v) {
        var record = '<tr>'
          + '<td>' + v.yingjiaofydm + '</td>'
          + '<td>' + v.shijiyjje + '</td>'
          + '<td>' + v.jiaofeijzr + '</td>'
          + '<td>' + v.feiyongzt + '</td>'
          + '</tr>';
        payables = payables + record;
      });

      $('#payable').append(payables);
    }

    var paid = JSON.parse(data.paid);
    if (paid != null) {
      var paids = '';
      paid.forEach(function (v) {
        var record = '<tr>'
          + '<td>' + v.feiyongzldm + '</td>'
          + '<td>' + v.jiaofeije + '</td>'
          + '<td>' + v.jiaofeisj + '</td>'
          + '<td>' + v.jiaofeirxm + '</td>'
          + '<td>' + v.shoujuh + '</td>'
          + '</tr>';
        paids = paids + record;
      });

      $('#paid').append(paids);
    }

  }

  var handlePostData = function (data) {

    var notice = JSON.parse(data.notice);
    if (notice != null) {
      var notices = '';
      notice.forEach(function (v) {
        var record = '<tr>'
          + '<td>' + v.tongzhislx + '</td>'
          + '<td>' + v.fawenrq + '</td>'
          + '<td>' + v.shoujianrxm + '</td>'
          + '<td>' + v.shoujianyzbm + '</td>'
          + '<td>' + v.xiazaisj + '</td>'
          + '<td>' + v.xiazaiip + '</td>'
          + '<td>' + v.fawenlx + '</td>'
          + '</tr>';
        notices = notices + record;
      });
      $('#notice').append(notices);
    }

    var patentCertificate = JSON.parse(data.patentCertificate);
    if (patentCertificate != null) {
      var patentCertificates = '';
      patentCertificate.forEach(function (v) {
        var record = '<tr>'
          + '<td>' + v.fawenrq + '</td>'
          + '<td>' + v.shoujianrxm + '</td>'
          + '<td>' + v.shoujianyzbm + '</td>'
          + '</tr>';
        patentCertificates = patentCertificates + record;
      });

      $('#patentCertificate').append(patentCertificates);
    }

  }

  var handleAnnounceData = function (data) {

    var publish = JSON.parse(data.publish);
    if (publish != null) {
      var publishs = '';
      publish.forEach(function (v) {
        var record = '<tr>'
          + '<td>' + v.gonggaoh + '</td>'
          + '<td>' + v.gongkaigglx + '</td>'
          + '<td>' + v.juanqih + '</td>'
          + '<td>' + v.gonggaor + '</td>'
          + '</tr>';
        publishs = publishs + record;
      });

      $('#publish').append(publishs);
    }

    var transactionPublish = JSON.parse(data.transactionPublish);
    if (transactionPublish != null) {
      var transactionPublishs = '';
      transactionPublish.forEach(function (v) {
        var record = '<tr>'
          + '<td>' + v.shiwugglxdm + '</td>'
          + '<td>' + v.juanqih + '</td>'
          + '<td>' + v.gonggaor + '</td>'
          + '</tr>';
        transactionPublishs = transactionPublishs + record;
      });

      $('#transactionPublish').append(transactionPublishs);
    }

  }

  function initTabsEvent() {
    $('#tabs').on('click', 'li', function () {
      var type = $(this).children("a").attr('id');
      var applicationNumber = $('#applicationNumber').val();
      if (type == undefined || applicationNumber == undefined) {
        return;
      }
      $.ajax({
        url: http_request + "/patent/cpquery",
        data: {
          "applicationNumber": applicationNumber,
          'type': type
        },
        dataType: 'json',
        success: function (data) {

          switch (type) {
            case 'application':
              handleApplicationData(data.data);
              break;
            case 'review':

              handleReviewData(data.data);

              break;
            case 'cost':
              handleCostData(data.data);
              break;
            case 'post':
              handlePostData(data.data);
              break;
            case 'announce':
              handleAnnounceData(data.data);
              break;
            default:

          }

        }
      });
    });
  }

  return {
    init: function () {
      initTabsEvent();

    }

  };

}();

jQuery(document).ready(function () {
  PatentCpquery.init();
});
