var options = null;

var Chart = function () {

  function initDropdownMenu() {
    $('.dropdown-toggle').click(function () {
      var flag = $(".theme-panel").is(":hidden");
      if (flag) {
        $('.theme-settings').empty();

        var bDate = false;
        if ($('#type').val() == '3') {
          bDate = true;
        }
        $.ajax({
          url: http_request + "/chart/getChartOption",
          data: {
            "bDate":bDate
          },
          success: function (data) {
            options = data;

            var ops = '<option selected="selected" >请选择</option>';
            for (var i = 0;i < options.template.length; ++i) {

              ops += '<option value="'+ options.template[i].id +'" >'+options.template[i].name+'</option>';
            }

            var properties = '';
            if ($('#type').val() == '3') {
              properties = '<li> 分类属性'
                  + '  <select class="form-control input-sm input-small input-inline tooltips"  data-placement="left" id="property1">'
                  + '    <option selected="selected">请选择</option>'
                  + '  </select>'
                  + '</li>'
                  + '<li> 日期属性'
                  + '  <select class="form-control input-sm input-small input-inline tooltips"  data-placement="left" id="property2">'
                  + '    <option selected="selected">请选择</option>'
                  + '  </select>'
                  + '</li>'
                  + '<li> 粒度'
                  + '  <select class="form-control input-sm input-small input-inline tooltips"  data-placement="left" id="scope">'
                  + '    <option value="0" selected="selected">默认</option>'
                  + '    <option value="1">月</option>'
                  + '    <option value="2">季度</option>'
                  + '    <option value="3">年</option>'
                  + '  </select>'
                  + '</li>';
            } else {
              properties = '<li> 属性'
                  + '  <select class="form-control input-sm input-small input-inline tooltips"  data-placement="left" id="property1">'
                  + '    <option selected="selected">请选择</option>'
                  + '  </select>'
                  + '</li>';
            }


            
            var option = '<li> 名称'
                + ' <input class="form-control input-sm input-small input-inline tooltips"  data-placement="left" id="name">'
                + '</li>'
                + '<li> 模板'
                + '  <select class="form-control input-sm input-small input-inline tooltips" onchange="changeTemplate(this)" data-placement="left" id="template">'
                + ops
                + '  </select>'
                + '</li>'
                + '<li> 数据源'
                + '  <select class="form-control input-sm input-small input-inline tooltips"  data-placement="left" id="source">'
                + '    <option selected="selected">请选择</option>'
                + '  </select>'
                + '</li>'
                + properties;
            $('.theme-settings').append(option);

          }
        });
      }
    });

    $('#btnAddChart').click(function () {
      if ($('#type').val() == '3') {
        addChart($('#name').val(),$('#template').val(),$('#source').val(),$('#property1').val(),$('#property2').val(),$('#scope').val(),$('#type').val());
      } else {
        addChart($('#name').val(),$('#template').val(),$('#source').val(),$('#property1').val(),0,0,$('#type').val());
      }

    })
  }

  function initChartSelect() {
    if ($('#charts').val() != undefined) {
      showChart($('#charts option:selected').val());


      $('#charts').change(function () {

        $('.portlet').remove();
        showChart($('#charts option:selected').val());

      });
    }


  }

  return {

    init: function () {
      initChartSelect();
      initDropdownMenu();
    }
  };

}();

function changeTemplate(template) {

  $('#source').empty();
  var source = '<option value="" filter="">默认</option>';
  for (var i = 0;i < options.source.length; ++i) {
    if (options.source[i].templateId == template.value) {
      source += '<option value="'+ options.source[i].id +'" >'+options.source[i].configname+'</option>';
    }
  }
  $('#source').append(source);


  $('#property1').empty();
  var id = 'template' + template.value;
  var propertyData =  options[id];
  var property1 = '';

  for (var i = 0;i < propertyData.length; ++i) {

    property1 += '<option value="'+ propertyData[i].sortId +'" >'+propertyData[i].name+'</option>';
  }

  $('#property1').append(property1);

  if ($('#type').val() == '3') {
    $('#property2').empty();
    var id = 'dateTemplate' + template.value;
    var propertyData =  options[id];
    var property2 = '';

    for (var i = 0;i < propertyData.length; ++i) {

      property2 += '<option value="'+ propertyData[i].sortId +'" >'+propertyData[i].name+'</option>';
    }

    $('#property2').append(property2);
  }
};

function addChart(name,templateId,configId,property1,property2,scope,type){

  if (configId == '') {
    configId = 0;
  }
  $.ajax({
    url: http_request + "/chart/addChart",
    data: {
      "name":name,
      "templateId": templateId,
      "configId": configId,
      "property1": property1,
      "property2": property2,
      "scope": scope,
      "type":type
    },
    success: function (data) {
      //console.info(data);
      if (data == '-1' ) {
        swal({
          title:'该属性分类超过50个，无法正常显示！',
          confirmButtonText: '确定'
        })
      } else {
        window.location.reload(true);
      }

    }
  });
};

function showChart(id){

  $.ajax({
    url: http_request + "/chart/showChart",
    data: {
      "id":id
    },
    success: function (data) {
      //console.info(data);
      var chartDiv = '<div class="portlet light portlet-fit ">'
          + ' <div class="portlet-title">'
          + '   <div class="caption">'
          + '     <i class=" icon-layers font-green"></i>'
          + '     <span class="caption-subject font-green bold uppercase">'+data.chart.name+'</span>'
          + '   </div>'
          + '   <div class="actions">'
          + '     <a class="btn btn-circle btn-icon-only btn-default" href="javascript:;" onclick="deleteChart(' + data.chart.id + ')">'
          + '       <i class="icon-trash"></i>'
          + '     </a>'
          + '   </div>'
          + ' </div>'
          + ' <div class="portlet-body">'
          + '   <div id="'+data.chart.id+'" class="CSSAnimationChart"></div>'
          + ' </div>'
          + '</div>';

      $('.page-content-inner').append(chartDiv);
      switch (data.chart.type) {
        case 1:
          AmCharts.makeChart( data.chart.id, {
            "type": "pie",
            "theme": "light",
            "dataProvider": data.data,
            "valueField": "value",
            "titleField": "name",
            "outlineAlpha": 0.4,
            "depth3D": 15,
            "balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
            "angle": 30,
            "export": {
              "enabled": true
            }
          } );
          break;
        case 2:
          AmCharts.makeChart( data.chart.id, {
            "type": "serial",
            "theme": "light",
            "dataProvider": data.data,
            "valueAxes": [ {
              "gridColor": "#FFFFFF",
              "gridAlpha": 0.2,
              "dashLength": 0
            } ],
            "gridAboveGraphs": true,
            "startDuration": 1,
            "graphs": [ {
              "balloonText": "[[category]]: <b>[[value]]</b>",
              "fillAlphas": 0.8,
              "lineAlpha": 0.2,
              "type": "column",
              "valueField": "value"
            } ],
            "chartCursor": {
              "categoryBalloonEnabled": false,
              "cursorAlpha": 0,
              "zoomable": false
            },
            "categoryField": "name",
            "categoryAxis": {
              "gridPosition": "start",
              "gridAlpha": 0,
              "tickPosition": "start",
              "tickLength": 20,
              "labelRotation":30
            },
            "export": {
              "enabled": true
            }

          } );
          break;
        case 3:

          AmCharts.addInitHandler(function(chart) {
            if (data.data.length != chart.dataProvider.length) {
              return;
            }

            // do nothing if serisField is not set
            if (chart.seriesField === undefined)
              return;

            // get graphs and dataProvider


            if (chart.graphs === undefined)
              chart.graphs = [];
            var graphs = chart.graphs;
            var dataSet = chart;


            // collect value fields for graphs that might already exist
            // in chart config
            var valueFields = {};

            if (graphs.length) {
              for (var i = 0; i < graphs.length; i++) {
                var g = graphs[i];
                if (g.id === undefined)
                  g.id = i;
                valueFields[g.id] = g.valueField;

              }
            }

            // process data
            var newData = [];
            var dpoints = {};


            for (var i = 0; i < dataSet.dataProvider.length; i++) {

              // get row data
              var row = dataSet.dataProvider[i];
              var category = row[dataSet.categoryField];
              var series = row[chart.seriesField];

              // create a data point
              if (dpoints[category] === undefined) {
                dpoints[category] = {};
                dpoints[category][dataSet.categoryField] = category;
                newData.push(dpoints[category]);
              }

              // check if we need to generate a graph
              if (valueFields[series] === undefined) {
                // apply template
                var g = {};
                if (chart.seriesGraphTemplate !== undefined) {
                  g = cloneObject(chart.seriesGraphTemplate);
                }
                g.id = series;
                g.valueField = series;
                g.title = series;


                // add to chart's graphs
                graphs.push(g);
                valueFields[series] = series;

              }

              // add series value field
              if (row[chart.seriesValueField] !== undefined)
                dpoints[category][series] = row[chart.seriesValueField];

              // add the rest of the value fields (if applicable)
              for (var field in valueFields) {
                if (valueFields.hasOwnProperty(field) && row[field] !== undefined)
                  dpoints[category][field] = row[field];
              }
            }

            // set data
            dataSet.dataProvider = newData;

            // function which clones object
            function cloneObject(obj) {

              if (null == obj || "object" != typeof obj) return obj;
              var copy = obj.constructor();

              for (var attr in obj) {

                if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
              }

              return copy;
            }

          }, ["serial"]);

          var categoryAxis = {};
          var dataDateFormat = "";

          switch (data.chart.scope) {
            case 0:
              dataDateFormat = "YYYY-MM-DD";
              categoryAxis = {
                "parseDates": true,
                "dashLength": 1,
                "minorGridEnabled": true
              };
              break;
            case 1:

              dataDateFormat = "YYYY-MM";
              categoryAxis = {
                "parseDates": true,
                "dashLength": 1,
                "minorGridEnabled": true
              };
              break;
            case 2:
              dataDateFormat = "YYYY";
              categoryAxis = {
                "parseDates": false,
                "dashLength": 1,
                "minorGridEnabled": true
              };
              break;
            case 3:
              dataDateFormat = "YYYY";
              categoryAxis = {
                "parseDates": false,
                "dashLength": 1,
                "minorGridEnabled": true
              };
              break;
          }

          var chart = AmCharts.makeChart(data.chart.id, {
            "type": "serial",
            "theme": "light",
            "dataDateFormat": dataDateFormat,
            "marginRight": 40,
            "marginLeft": 40,
            "autoMarginOffset": 20,
            "mouseWheelZoomEnabled":true,
            "seriesField": "name1",
            "seriesValueField": "value",
            "seriesGraphTemplate": {
              "lineThickness": 2,
              "bullet": "round"
            },
            "categoryField": "name2",
            "categoryAxis": categoryAxis,
            "valueAxes": [{
              "stackType": "none"
            }],
            "chartScrollbar": {

              "oppositeAxis":false,
              "offset":30,
              "scrollbarHeight": 80,
              "backgroundAlpha": 0,
              "selectedBackgroundAlpha": 0.1,
              "selectedBackgroundColor": "#888888",
              "graphFillAlpha": 0,
              "graphLineAlpha": 0.5,
              "selectedGraphFillAlpha": 0,
              "selectedGraphLineAlpha": 1,
              "autoGridCount":true,
              "color":"#AAAAAA"
            },
            "chartCursor": {
              "pan": true,
              "valueLineEnabled": true,
              "valueLineBalloonEnabled": true,
              "cursorAlpha":1,
              "cursorColor":"#258cbb",
              "limitToGraph":"g1",
              "valueLineAlpha":0.2,
              "valueZoomable":false
            },
            "legend": {
              "position": "right"
            },
            "dataProvider": data.data,
            "export": {
              "enabled": true
            }
          });

          chart.addListener("rendered", zoomChart);

          zoomChart();

        function zoomChart() {
          chart.zoomToIndexes(chart.dataProvider.length - 40, chart.dataProvider.length - 1);
        }
          break;
      }



    }
  });
};

function deleteChart(id) {
  $.ajax({
    url: http_request + "/chart/deleteChart",
    data: {
      "id":id
    },
    success: function (data) {
      window.location.reload(true);
    }
  });
}

jQuery(document).ready(function () {
  Chart.init();
});