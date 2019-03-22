var Navigation_v7 = {

  tooltip: function() {


    $('.fb7-menu li').filter(':not(.fb7-goto,.fb7-menu li:first-child,.fb7-menu li:last-child )').each(function() {
      $(this).css('cursor','pointer');
      var description = $('a', this).attr('title');
      var tooltip = '<span class="fb7-tooltip">'+description+'<b></b></span>';
      $('a', this).removeAttr("title");
      $(this).append(tooltip);
    });

    $('.fb7-menu li').mousemove(function(e) {

      var tooltip=$('.fb7-tooltip', this);
      var offset = $(this).offset();
      var relY = e.pageY - offset.top;
      var x2= e.pageX-$('#fb7').offset().left+tooltip.width()
      var width_area=$('#fb7').width()

      if( (x2+20)>width_area){
        var orient="right";
      }else{
        var orient="left";
      }

      if(orient=="left"){
        var relX = e.pageX - offset.left;
        $('#fb7 .fb7-tooltip b').css('left','6px')
      }else{
        var relX = e.pageX - offset.left-tooltip.width()-5;
        $('#fb7 .fb7-tooltip b').css('left',(tooltip.width()+6)+'px')
      }

      //$('.fb7-tooltip', this).html( x2+" > "+width_area  );
      $('.fb7-tooltip', this).css({ left: relX, top: relY-45 });
    })

    $('.fb7-menu li').hover(function() {
      $('.fb7-tooltip').stop();
      $('.fb7-tooltip', this).fadeIn();
    }, function() {
      $('.fb7-tooltip').hide();
    });

    Book_v7.resize_page()

  },


  ///event mouse down in book
  book_mouse_down: function(){
    $('#fb7-about').css('z-index',5);
    //Book_v7.resize_page();
  },

  book_mouse_up: function(e){
    var offset = $(this).offset();
    var relativeX = (e.pageX - offset.left);
    if( relativeX > ( WIDTH_BOOK / 2 )  ){
      //$('#fb7-about').css('z-index',11);
    }

  },

  init: function() {

    // Double Click
    if(ZOOM_DOUBLE_CLICK_ENABLED=="true"){
      $('#fb7-book').dblclick(function() {

        if(Book_v7.checkScrollBook()==false){ //zoom
          Book_v7.zoomTo(ZOOM_DOUBLE_CLICK)
        }else{
          Book_v7.zoomAuto();
          $('#fb7-container-book').css('cursor', 'default');
        }
      });
    }

    //focus for page manager

    var page_manager=$('#fb7-page-number');
    page_manager.focus(function(e) {
      var target=$(e.currentTarget);
      target.data('current',target.val());
      target.val('')
      //target.addClass('focus_input');


    });
    page_manager.focusout(function(e) {
      var target=$(e.currentTarget);
      var old=target.data('current');
      //target.removeClass('focus_input');
      if( target.val() ==''){
        target.val(old);
      }
    });



    //full screen
    $('.fb7-fullscreen').on('click', function() {

      $('.fb7-tooltip').hide();

      $('#fb7').fullScreen({

        'callback': function(isFullScreen){

          Book_v7.book_area();
          Book_v7.zoomAuto();
          Book_v7.center_icon();

          if(isFullScreen){

          }else{

          }

        }
      });
      e.preventDefault();

    });


    //download
    $('.fb7-download').on('click', function(event) {

      //$.address.update();
      // event.preventDefault();

    });

    // Home
    $('.fb7-home').on('click', function() {
      setPage(1);
      //setAddress('book5-1');
    });

    // Zoom Original
    $('.fb7-zoom-original').click(function() {

      Book_v7.zoomOriginal();


    });

    // Zoom Auto
    $('.fb7-zoom-auto').on('click', function() {
      Book_v7.zoomAuto();
    });

    // Zoom In
    $('.fb7-zoom-in').on('click', function() {

      Book_v7.zoomIn();


    });

    // Zoom Out
    $('.fb7-zoom-out').on('click', function() {

      Book_v7.zoomOut();

    });

    // All Pages
    $('.fb7-show-all').on('click', function() {
      $('#fb7-all-pages').
      addClass('active').
      css('opacity', 0).
      animate({ opacity: 1 }, 1000);
      Book_v7.all_pages();
      return false;
    })

    // Goto Page
    $('#fb7-page-number').keydown(function(e) {
      if (e.keyCode == 13) {
        setPage( $('#fb7-page-number').val() );
      }
    });

    $('.fb7-goto button').click(function(e) {
      setPage( $('#fb7-page-number').val() );
    });


    // Contact
    $('.contact').click(function() {
      $('#fb7-contact').addClass('active').animate({ opacity: 1 }, 1000);
      contact_form();
      clear_on_focus();
      return false;
    })

    //change z-index in about
    $('#fb7-book').bind('mousedown',this.book_mouse_down);
    $('#fb7-book').bind('mouseup',this.book_mouse_up);
    if (Book_v7.isiPhone()) {//for IPhone
      $('#fb7-book').bind('touchstart',this.book_mouse_down);
      $('#fb7-book').bind('touchend',this.book_mouse_up);
    }

    //show tooltip for icon
    if ( !Book_v7.isiPhone() && TOOL_TIP_VISIBLE=="true" ) {
      this.tooltip();
    }
  }
}
FlipBook = function() {
  return {
    init: function () {
      // Double Click

        $('.flipbook').dblclick(function() {

          if(Book_v7.checkScrollBook()==false){ //zoom
            Book_v7.zoomTo(ZOOM_DOUBLE_CLICK)
          }else{
            Book_v7.zoomAuto();
            $('#fb7-container-book').css('cursor', 'default');
          }
        });


    }
  };
}

jQuery(document).ready(function () {
  FlipBook.init();
});