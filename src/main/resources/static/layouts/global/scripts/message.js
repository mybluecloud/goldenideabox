
var Message = function () {
  var active_id = '';
  var active_name = '';
  var active_type = 0;
  var active_image = '';
  var wrapper = $('.page-quick-sidebar-wrapper');
  var wrapperChat = wrapper.find('.page-quick-sidebar-chat');
  var chatContainer = wrapperChat.find(".page-quick-sidebar-chat-user-messages");
  var input = wrapperChat.find('.page-quick-sidebar-chat-user-form .form-control');
  var wrapperClick = function () {
    wrapper.find('.page-quick-sidebar-chat-users .media-list > .media').click(function () {


      active_name = $(this).find('.media-heading').text();
      active_image = $(this).find('.media-object').attr("src");

      if ($(this).attr('id').indexOf('group') > 0) {
        active_type = 1;
        active_id = $(this).attr('id').slice(11);
      } else {
        active_type = 0;
        active_id = $(this).attr('id').slice(10);
      }
      $(this).children('.media-status').remove();
      chatContainer.html("");
      $.ajax({
        url: http_request + "/loadMessage",
        data: {
          "id": active_id,
          "type": active_type
        },
        success: function (data) {

          for (var i = 0;i < data.length;i++) {
            var dt = new Date(data[i].time);
            if (active_type == 0) {
              if (data[i].sender == active_id) {
                receiveMessage(active_name,active_image,dt.format("MM-dd hh:mm"),data[i].content,'in');
              } else {
                receiveMessage($("#user-name").text(),$("#user-image").attr("src"),dt.format("MM-dd hh:mm"),data[i].content,'out')
              }
            } else {
              if (data[i].sender == $("#user-id").val()) {
                receiveMessage(data[i].name,http_request+data[i].image,dt.format("MM-dd hh:mm"),data[i].content,'out');
              } else {
                receiveMessage(data[i].name,http_request+data[i].image,dt.format("MM-dd hh:mm"),data[i].content,'in');
              }
              var time = new Date().format("yyyy-MM-dd hh:mm:ss");
              Cookies.set('updateMsgTime', time);
            }


          }
          wrapperChat.addClass("page-quick-sidebar-content-item-shown");
        }
      });

    });

    wrapper.find('.page-quick-sidebar-chat-user .page-quick-sidebar-back-to-list').click(function () {
      wrapperChat.removeClass("page-quick-sidebar-content-item-shown");
    });
  };

  // Handles quick sidebar toggler
  function handleQuickSidebarToggler() {
    // quick sidebar toggler
    $('.dropdown-quick-sidebar-toggler a, .page-quick-sidebar-toggler, .quick-sidebar-toggler').click(function (e) {
      $('body').toggleClass('page-quick-sidebar-open');
    });
  };

  // Handles quick sidebar chats
  function handleQuickSidebarChat() {
    var wrapper = $('.page-quick-sidebar-wrapper');
    var wrapperChat = wrapper.find('.page-quick-sidebar-chat');

    var initChatSlimScroll = function () {
      var chatUsers = wrapper.find('.page-quick-sidebar-chat-users');
      var chatUsersHeight;

      chatUsersHeight = wrapper.height() - wrapper.find('.nav-tabs').outerHeight(true);

      // chat user list
      App.destroySlimScroll(chatUsers);
      chatUsers.attr("data-height", chatUsersHeight);
      App.initSlimScroll(chatUsers);

      var chatMessages = wrapperChat.find('.page-quick-sidebar-chat-user-messages');
      var chatMessagesHeight = chatUsersHeight - wrapperChat.find('.page-quick-sidebar-chat-user-form').outerHeight(true);
      chatMessagesHeight = chatMessagesHeight - wrapperChat.find('.page-quick-sidebar-nav').outerHeight(true);

      // user chat messages
      App.destroySlimScroll(chatMessages);
      chatMessages.attr("data-height", chatMessagesHeight);
      App.initSlimScroll(chatMessages);
    };

    initChatSlimScroll();
    App.addResizeHandler(initChatSlimScroll); // reinitialize on window resize




  };

  // Handles quick sidebar tasks
  function handleQuickSidebarAlerts() {
    var wrapper = $('.page-quick-sidebar-wrapper');

    var initAlertsSlimScroll = function () {
      var alertList = wrapper.find('.page-quick-sidebar-alerts-list');
      var alertListHeight;

      alertListHeight = wrapper.height() - 80 - wrapper.find('.nav-justified > .nav-tabs').outerHeight();

      // alerts list
      App.destroySlimScroll(alertList);
      alertList.attr("data-height", alertListHeight);
      App.initSlimScroll(alertList);
    };

    initAlertsSlimScroll();
    App.addResizeHandler(initAlertsSlimScroll); // reinitialize on window resize
  };

  function chatUser(id,name,image,desc,num) {
    if (image == null) {
      image = '/layouts/layout/backstage/img/avatar1.jpg';
    }
    if (desc == null) {
      desc = '';
    }
    var status = '';
    if (num > 0) {
      status =  '<div class="media-status">'
          + '<span class="badge badge-danger">'+ num +'</span>'
          + '</div>';
    }
    var tpl = '<li class="media" id="chat_user_'+ id +'">'
        + status
        + '<img class="media-object" src="'+http_request+image+'" alt="...">'
        + '<div class="media-body">'
        + '<h4 class="media-heading">'+name+'</h4>'
        + '<div class="media-heading-sub">'+desc+'</div>'
        + '</div>'
        + '</li>';
    return tpl;
  };

  function chatGroup(id,name,remark,num) {
    if (remark == null) {
      remark = '';
    }
    var status = '';
    if (num > 0) {
      status =  '<div class="media-status">'
          + '<span class="badge badge-danger">'+ num +'</span>'
          + '</div>';
    }
    var tpl = '<li class="media" id="chat_group_'+ id +'">'
        + status
        + '<img class="media-object" src="'+http_request+'/layouts/layout/backstage/img/group.jpg" alt="...">'
        + '<div class="media-body">'
        + '<h4 class="media-heading">'+name+'</h4>'
        + '<div class="media-heading-sub">'+remark+'</div>'
        + '</div>'
        + '</li>';
    return tpl;
  };

  function preparePost(dir, time, name, image, message) {
    var tpl = '';
    tpl += '<div class="post ' + dir + '">';
    tpl += '<img class="avatar" alt="" src="' + image + '"/>';
    tpl += '<div class="message">';
    tpl += '<span class="arrow"></span>';
    tpl += '<span class="name">'+name+'</span>&nbsp;';
    tpl += '<span class="datetime">' + time + '</span>';
    tpl += '<span class="body">';
    tpl += message;
    tpl += '</span>';
    tpl += '</div>';
    tpl += '</div>';

    return tpl;
  };

  function sendMessage(name,image,time,text) {
    // handle post
    var time = new Date().format("MM-dd hh:mm");
    var message = preparePost('out',time, name, image, text);
    message = $(message);
    chatContainer.append(message);

    chatContainer.slimScroll({
      scrollTo: '1000000px'
    });

    input.val("");
    $.ajax({
      url: http_request + "/sendMessage",
      data: {
        "id": active_id,
        "text": text,
        "type": active_type
      },
      success: function (data) {
        //console.info(data);
      }
    });
  };

  function receiveMessage(name,image,time,text,post) {

      var message = preparePost(post, time, name, image, text);
      message = $(message);
      chatContainer.append(message);

      chatContainer.slimScroll({
        scrollTo: '1000000px'
      });

  };
  function initChooseUser() {
    var user = new Bloodhound({
      datumTokenizer: function(d) { return d.id; },
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: http_request + '/chooseUser?query=%QUERY',
        wildcard: '%QUERY'
      }
    });

    user.initialize();

    $('#choose_user').typeahead(null, {
      name: 'datypeahead',
      displayKey: 'name',
      source: user.ttAdapter(),
      hint:  true,
      templates: {
        suggestion: Handlebars.compile([
          '<div class="media">',
          '<div class="pull-left">',
          '<div class="media-object">',
          '<img src="'+http_request+'{{image}}" width="50" height="50"/>',
          '</div>',
          '</div>',
          '<div class="media-body">',
          '<h6 class="media-heading">{{name}}</h6>',
          '<p>{{introduction}}</p>',
          '</div>',
          '</div>',
        ].join(''))
      }
    });

    $('#choose_user').bind('typeahead:select', function(ev, suggestion) {


      if ($('#chat_user_' + suggestion["id"]).length>0){

      } else {
        $('#staff').append(chatUser(suggestion["id"],suggestion["name"],suggestion["image"],suggestion["introduction"],0));
      }
      wrapperClick();
    });
  };
  function initSidebarContent() {

    var time = new Date().format("yyyy-MM-dd hh:mm:ss");
    if (Cookies.get('updateMsgTime') != undefined) {
      time = Cookies.get('updateMsgTime');
    }
    $.ajax({
      url: http_request + "/chatUser",
      data: {
        "time":time
      },
      success: function (data) {

        var group = data.role;
        var user = data.user;
        //var notice = data.notice;
        var num = 0;

        for (var i=0;i < user.length;i++) {

          if ($('#chat_user_' + user[i]["id"]).length>0){

          } else {
            $('#staff').append(chatUser(user[i]["id"],user[i]["name"],user[i]["image"],user[i]["introduction"],user[i]["num"]));
            num += user[i]["num"];
          }
        }

        for (var i=0;i < group.length;i++) {

          if ($('#chat_group_' + group[i]["id"]).length>0){

          } else {
            $('#group').append(chatGroup(group[i]["id"],group[i]["name"],group[i]["remark"],group[i]["num"]));
            num += group[i]["num"];
          }
        }

        // for (var i=0;i < notice.length;i++) {
        //
        //   if ($('#chat_group_' + notice[i]["id"]).length>0){
        //
        //   } else {
        //     $('.feeds .list-items').append(noticeContent(notice[i]["text"],notice[i]["time"]));
        //   }
        // }
        wrapperClick();
        if (num > 0) {
          $('#msgnum').text( num );
        }

        Cookies.set('updateMsgTime', new Date().format("yyyy-MM-dd hh:mm:ss"));
      }
    });
  };
  function initSendButton() {
    var handleChatMessagePost = function (e) {
      e.preventDefault();

      var text = input.val();
      if (text.length === 0) {
        return;
      }
      var image = $("#user-image").attr("src");

      sendMessage($("#user-name").text(),image,new Date().format("MM-dd hh:mm"),text);
    };

    wrapperChat.find('.page-quick-sidebar-chat-user-form .btn').click(handleChatMessagePost);
    wrapperChat.find('.page-quick-sidebar-chat-user-form .form-control').keypress(function (e) {
      if (e.which == 13) {
        handleChatMessagePost(e);
        return false;
      }
    });
  };

  function noticeContent(text,time) {

    var info = '<li>'
        + '<div class="col1">'
        + '<div class="cont">'
        + '<div class="cont-col1">'
        + '<div class="label label-sm label-info">'
        + '<i class="fa fa-check"></i>'
        + '</div>'
        + '</div>'
        + '<div class="cont-col2">'
        + '<div class="desc">' + text
        + '</div>'
        + '</div>'
        + '</div>'
        + '</div>'
        + '<div class="col2">'
        + '<div class="date">' + time + '</div>'
        + '</div>'
        + '</li>';
    return info;
  }

  return {


    init: function () {
      //layout handlers
      handleQuickSidebarToggler(); // handles quick sidebar's toggler
      handleQuickSidebarChat(); // handles quick sidebar's chats
      handleQuickSidebarAlerts(); // handles quick sidebar's alerts
      //数据初始化
      initChooseUser();
      initSidebarContent();

      initSendButton();





    }
  };

}();

jQuery(document).ready(function () {
  Message.init();
});