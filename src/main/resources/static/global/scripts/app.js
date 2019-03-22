/**
 Core script to handle the entire theme and core functions
 **/


var App = function () {

  var resizeHandlers = [];

  var staticPath = '../static/';

  // runs callback functions set by App.addResponsiveHandler().
  var _runResizeHandlers = function() {
    // reinitialize other subscribed elements
    for (var i = 0; i < resizeHandlers.length; i++) {
      var each = resizeHandlers[i];
      each.call();
    }
  };

  // handle group element heights
  var handleHeight = function() {
    $('[data-auto-height]').each(function() {
      var parent = $(this);
      var items = $('[data-height]', parent);
      var height = 0;
      var mode = parent.attr('data-mode');
      var offset = parseInt(parent.attr('data-offset') ? parent.attr('data-offset') : 0);

      items.each(function() {
        if ($(this).attr('data-height') == "height") {
          $(this).css('height', '');
        } else {
          $(this).css('min-height', '');
        }

        var height_ = (mode == 'base-height' ? $(this).outerHeight() : $(this).outerHeight(true));
        if (height_ > height) {
          height = height_;
        }
      });

      height = height + offset;

      items.each(function() {
        if ($(this).attr('data-height') == "height") {
          $(this).css('height', height);
        } else {
          $(this).css('min-height', height);
        }
      });

      if(parent.attr('data-related')) {
        $(parent.attr('data-related')).css('height', parent.height());
      }
    });
  }


  // Handles portlet tools & actions
  var handlePortletTools = function() {
    // handle portlet remove
    $('body').on('click', '.portlet > .portlet-title > .tools > a.remove', function(e) {
      e.preventDefault();
      var portlet = $(this).closest(".portlet");

      if ($('body').hasClass('page-portlet-fullscreen')) {
        $('body').removeClass('page-portlet-fullscreen');
      }

      portlet.find('.portlet-title .fullscreen').tooltip('destroy');
      portlet.find('.portlet-title > .tools > .reload').tooltip('destroy');
      portlet.find('.portlet-title > .tools > .remove').tooltip('destroy');
      portlet.find('.portlet-title > .tools > .config').tooltip('destroy');
      portlet.find('.portlet-title > .tools > .collapse, .portlet > .portlet-title > .tools > .expand').tooltip('destroy');

      portlet.remove();
    });

    // handle portlet fullscreen
    $('body').on('click', '.portlet > .portlet-title .fullscreen', function(e) {
      e.preventDefault();
      var portlet = $(this).closest(".portlet");
      if (portlet.hasClass('portlet-fullscreen')) {
        $(this).removeClass('on');
        portlet.removeClass('portlet-fullscreen');
        $('body').removeClass('page-portlet-fullscreen');
        portlet.children('.portlet-body').css('height', 'auto');
      } else {
        var height = App.getViewPort().height -
          portlet.children('.portlet-title').outerHeight() -
          parseInt(portlet.children('.portlet-body').css('padding-top')) -
          parseInt(portlet.children('.portlet-body').css('padding-bottom'));

        $(this).addClass('on');
        portlet.addClass('portlet-fullscreen');
        $('body').addClass('page-portlet-fullscreen');
        portlet.children('.portlet-body').css('height', height);
      }
    });

    $('body').on('click', '.portlet > .portlet-title > .tools > a.reload', function(e) {
      e.preventDefault();
      var el = $(this).closest(".portlet").children(".portlet-body");
      var url = $(this).attr("data-url");
      var error = $(this).attr("data-error-display");
      if (url) {
        App.blockUI({
          target: el,
          animate: true,
          overlayColor: 'none'
        });
        $.ajax({
          type: "GET",
          cache: false,
          url: url,
          dataType: "html",
          success: function(res) {
            App.unblockUI(el);
            el.html(res);
            App.initAjax() // reinitialize elements & plugins for newly loaded content
          },
          error: function(xhr, ajaxOptions, thrownError) {
            App.unblockUI(el);
            var msg = 'Error on reloading the content. Please check your connection and try again.';
            if (error == "toastr" && toastr) {
              toastr.error(msg);
            } else if (error == "notific8" && $.notific8) {
              $.notific8('zindex', 11500);
              $.notific8(msg, {
                theme: 'ruby',
                life: 3000
              });
            } else {
              alert(msg);
            }
          }
        });
      } else {
        // for demo purpose
        App.blockUI({
          target: el,
          animate: true,
          overlayColor: 'none'
        });
        window.setTimeout(function() {
          App.unblockUI(el);
        }, 1000);
      }
    });

    // load ajax data on page init
    $('.portlet .portlet-title a.reload[data-load="true"]').click();

    $('body').on('click', '.portlet > .portlet-title > .tools > .collapse, .portlet .portlet-title > .tools > .expand', function(e) {
      e.preventDefault();
      var el = $(this).closest(".portlet").children(".portlet-body");
      if ($(this).hasClass("collapse")) {
        $(this).removeClass("collapse").addClass("expand");
        el.slideUp(200);
      } else {
        $(this).removeClass("expand").addClass("collapse");
        el.slideDown(200);
      }
    });
  };

  // Handlesmaterial design checkboxes
  var handleMaterialDesign = function() {

    // Material design ckeckbox and radio effects
    $('body').on('click', '.md-checkbox > label, .md-radio > label', function() {
      var the = $(this);
      // find the first span which is our circle/bubble
      var el = $(this).children('span:first-child');

      // add the bubble class (we do this so it doesnt show on page load)
      el.addClass('inc');

      // clone it
      var newone = el.clone(true);

      // add the cloned version before our original
      el.before(newone);

      // remove the original so that it is ready to run on next click
      $("." + el.attr("class") + ":last", the).remove();
    });

    if ($('body').hasClass('page-md')) {
      // Material design click effect
      // credit where credit's due; http://thecodeplayer.com/walkthrough/ripple-click-effect-google-material-design
      var element, circle, d, x, y;
      $('body').on('click', 'a.btn, button.btn, input.btn, label.btn', function(e) {
        element = $(this);

        if(element.find(".md-click-circle").length == 0) {
          element.prepend("<span class='md-click-circle'></span>");
        }

        circle = element.find(".md-click-circle");
        circle.removeClass("md-click-animate");

        if(!circle.height() && !circle.width()) {
          d = Math.max(element.outerWidth(), element.outerHeight());
          circle.css({height: d, width: d});
        }

        x = e.pageX - element.offset().left - circle.width()/2;
        y = e.pageY - element.offset().top - circle.height()/2;

        circle.css({top: y+'px', left: x+'px'}).addClass("md-click-animate");

        setTimeout(function() {
          circle.remove();
        }, 1000);
      });
    }

    // Floating labels
    var handleInput = function(el) {
      if (el.val() != "") {
        el.addClass('edited');
      } else {
        el.removeClass('edited');
      }
    }

    $('body').on('keydown', '.form-md-floating-label .form-control', function(e) {
      handleInput($(this));
    });
    $('body').on('blur', '.form-md-floating-label .form-control', function(e) {
      handleInput($(this));
    });

    $('.form-md-floating-label .form-control').each(function(){
      if ($(this).val().length > 0) {
        $(this).addClass('edited');
      }
    });
  }

  // Handles Bootstrap Dropdowns
  var handleDropdowns = function() {
    /*
      Hold dropdown on click
    */
    $('body').on('click', '.dropdown-menu.hold-on-click', function(e) {
      e.stopPropagation();
    });
  };

  return {
    init: function () {
      handlePortletTools();
      handleMaterialDesign(); // handle material design
      handleDropdowns(); // handle dropdowns
      //Handle group element heights
      this.addResizeHandler(handleHeight); // handle auto calculating height on window resize
    },

    getStaticPath: function() {
      return staticPath;
    },

    //public function to add callback a function which will be called on window resize
    addResizeHandler: function (func) {
      resizeHandlers.push(func);
    },

    //public functon to call _runresizeHandlers
    runResizeHandlers: function () {
      _runResizeHandlers();
    },

    // To get the correct viewport width based on  http://andylangton.co.uk/articles/javascript/get-viewport-size-javascript/
    getViewPort: function() {
      var e = window,
          a = 'inner';
      if (!('innerWidth' in window)) {
        a = 'client';
        e = document.documentElement || document.body;
      }

      return {
        width: e[a + 'Width'],
        height: e[a + 'Height']
      };
    },


    initSlimScroll: function(el) {
      if (!$().slimScroll) {
        return;
      }

      $(el).each(function() {
        if ($(this).attr("data-initialized")) {
          return; // exit
        }

        var height;

        if ($(this).attr("data-height")) {
          height = $(this).attr("data-height");
        } else {
          height = $(this).css('height');
        }

        $(this).slimScroll({
          allowPageScroll: true, // allow page scroll when the element scroll is ended
          size: '7px',
          color: ($(this).attr("data-handle-color") ? $(this).attr("data-handle-color") : '#bbb'),
          wrapperClass: ($(this).attr("data-wrapper-class") ? $(this).attr("data-wrapper-class") : 'slimScrollDiv'),
          railColor: ($(this).attr("data-rail-color") ? $(this).attr("data-rail-color") : '#eaeaea'),
          position:  'right',
          height: height,
          alwaysVisible: ($(this).attr("data-always-visible") == "1" ? true : false),
          railVisible: ($(this).attr("data-rail-visible") == "1" ? true : false),
          disableFadeOut: true
        });

        $(this).attr("data-initialized", "1");
      });
    },

    destroySlimScroll: function(el) {
      if (!$().slimScroll) {
        return;
      }

      $(el).each(function() {
        if ($(this).attr("data-initialized") === "1") { // destroy existing instance before updating the height
          $(this).removeAttr("data-initialized");
          $(this).removeAttr("style");

          var attrList = {};

          // store the custom attribures so later we will reassign.
          if ($(this).attr("data-handle-color")) {
            attrList["data-handle-color"] = $(this).attr("data-handle-color");
          }
          if ($(this).attr("data-wrapper-class")) {
            attrList["data-wrapper-class"] = $(this).attr("data-wrapper-class");
          }
          if ($(this).attr("data-rail-color")) {
            attrList["data-rail-color"] = $(this).attr("data-rail-color");
          }
          if ($(this).attr("data-always-visible")) {
            attrList["data-always-visible"] = $(this).attr("data-always-visible");
          }
          if ($(this).attr("data-rail-visible")) {
            attrList["data-rail-visible"] = $(this).attr("data-rail-visible");
          }

          $(this).slimScroll({
            wrapperClass: ($(this).attr("data-wrapper-class") ? $(this).attr("data-wrapper-class") : 'slimScrollDiv'),
            destroy: true
          });

          var the = $(this);

          // reassign custom attributes
          $.each(attrList, function(key, value) {
            the.attr(key, value);
          });

        }
      });
    },

    getResponsiveBreakpoint: function (size) {
      // bootstrap responsive breakpoints
      var sizes = {
        'xs': 480,     // extra small
        'sm': 768,     // small
        'md': 992,     // medium
        'lg': 1200     // large
      };

      return sizes[size] ? sizes[size] : 0;
    }
  };

}();

<!-- END THEME LAYOUT SCRIPTS -->

jQuery(document).ready(function () {
  App.init();
});