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