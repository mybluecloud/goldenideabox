var Login = function () {

  var handleLogin = function () {


    $('#regeditForm').validate({
      errorPlacement: function(error, element) {
        $(error).css('color','red');
        $( element )
        .parent()
        .find( "label[for='" + element.attr( "id" ) + "']" )
        .append( error );

      },

      errorElement: "span",
      rules: {
        account: {
          required: true,
          minlength: 5
        },
        name: {
          required: true,
          minlength: 2
        },
        password: {
          required: true,
          minlength: 5
        },
        confirm_password: {
          required: true,
          minlength: 5,
          equalTo: "#password"
        }
      }
    });

    $('.login-form input').keypress(function (e) {
      if (e.which == 13) {
        if ($('.login-form').validate().form()) {
          $('.login-form').submit(); //form validation success, call ajax form submit
        }
        return false;
      }
    });

    $('.regedit-form input').keypress(function (e) {
      if (e.which == 13) {
        if ($('.regedit-form').validate().form()) {
          $('.regedit-form').submit();
        }
        return false;
      }
    });

    $('#regedit').click(function () {
      $('.login-form').hide();
      $('.regedit-form').show();
    });

    $('#back-btn').click(function () {
      $('.login-form').show();
      $('.regedit-form').hide();
    });
  }

  return {
    //main function to initiate the module
    init: function () {

      handleLogin();

      // init background slide images
      $('.login-bg').backstretch([
            http_request + "/pages/img/bg1.jpg",
            http_request + "/pages/img/bg2.jpg",
            http_request + "/pages/img/bg3.jpg"
          ], {
            fade: 1000,
            duration: 8000
          }
      );

      $('.regedit-form').hide();

    }

  };

}();

jQuery(document).ready(function () {
  Login.init();
});