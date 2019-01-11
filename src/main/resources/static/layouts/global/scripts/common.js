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

Date.prototype.format = function (format) {
  var o = {
    "M+": this.getUTCMonth() + 1, //month
    "d+": this.getUTCDate() ,    //day
    "h+": this.getUTCHours() + 8,   //hour
    "m+": this.getUTCMinutes(), //minute
    "s+": this.getUTCSeconds(), //second
    "q+": Math.floor((this.getUTCMonth() + 3) / 3),  //quarter
    "S": this.getUTCMilliseconds() //millisecond
  }
  if (/(y+)/.test(format)) {
    format = format.replace(RegExp.$1,
        (this.getUTCFullYear() + "").substr(4 - RegExp.$1.length));
  }
  for (var k in o) {
    if (new RegExp("(" + k + ")").test(format)) {
      format = format.replace(RegExp.$1,
          RegExp.$1.length == 1 ? o[k] :
              ("00" + o[k]).substr(("" + o[k]).length));
    }
  }
  return format;
}

var notify = function (title, message, url, target) {
  $.notify({
    // options
    icon: 'glyphicon glyphicon-info-sign',
    title: title,
    message: message,
    url: url,
    target: target
  }, {
    // settings
    element: 'body',
    position: null,
    type: "info",
    allow_dismiss: true,
    newest_on_top: false,
    showProgressbar: false,
    placement: {
      from: "bottom",
      align: "right"
    },
    offset: 20,
    spacing: 10,
    z_index: 1031,
    delay: 1000,
    timer: 500,
    url_target: '_blank',
    mouse_over: null,
    animate: {
      enter: 'animated fadeInDown',
      exit: 'animated fadeOutUp'
    },
    onShow: null,
    onShown: null,
    onClose: null,
    onClosed: null,
    icon_type: 'class',
    template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0}" role="alert">' +
    '<button type="button" aria-hidden="true" class="close" data-notify="dismiss">×</button>' +
    '<span data-notify="icon"></span> ' +
    '<span data-notify="title">{1}</span> ' +
    '<span data-notify="message">{2}</span>' +
    '<div class="progress" data-notify="progressbar">' +
    '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>'
    +
    '</div>' +
    '<a href="{3}" target="{4}" data-notify="url"></a>' +
    '</div>'
  });
}

function isNumber(val) {

  var regPos = /^\d+(\.\d+)?$/; //非负浮点数
  var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
  if (regPos.test(val) || regNeg.test(val)) {
    return true;
  } else {
    return false;
  }

}

var checkable = function () {

  //var checked = $('.group-checkable').prop("checked");
  var checked = $('.group-checkable').is(':checked');

  jQuery(".checkboxes").each(function () {

    if (checked) {

      $(this).prop("checked", true);
    } else {

      $(this).prop("checked", false);
    }
  });
};