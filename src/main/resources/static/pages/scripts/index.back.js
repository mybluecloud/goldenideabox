var IndexBack = function () {
  var sortable;
  function showModel(id,modelId){

    $.ajax({
      url: http_request + "/chart/showChart",
      data: {
        "id":modelId
      },
      success: function (data) {

        var chartDiv = '<div id="model-'+id+'" data-id="'+id+'" class="col-md-6 col-xs-12 col-sm-12">'
            + '<div class="portlet light">'
            + ' <div class="portlet-title">'
            + '   <div class="caption">'
            + '     <i class=" icon-layers font-green"></i>'
            + '     <span class="caption-subject font-green bold uppercase">'+data.chart.name+'</span>'
            + '   </div>'
            + '   <div class="actions">'
            + '     <a class="btn btn-circle btn-icon-only btn-default" href="javascript:;" onclick="deleteModel(' + id + ')">'
            + '       <i class="icon-trash"></i>'
            + '     </a>'
            + '   </div>'
            + ' </div>'
            + ' <div class="portlet-body">'
            + '   <div id="chart-'+data.chart.id+'" class="CSSAnimationChart"></div>'
            + ' </div>'
            + '</div>'
            + '</div>';

        $('.page-content-inner').append(chartDiv);
        switch (data.chart.type) {
          case 1:
            AmCharts.makeChart( 'chart-'+data.chart.id, {
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
            AmCharts.makeChart( 'chart-'+data.chart.id, {
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

            var chart = AmCharts.makeChart('chart-'+data.chart.id, {
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

  function initDropdownMenu() {
    $('.dropdown-toggle').click(function () {
      var flag = $(".theme-panel").is(":hidden");
      if (flag) {
        $('.theme-settings').empty();
        $.ajax({
          url: http_request + "/chart/getCharts",
          success: function (data) {


            var ops = '';
            for (var i = 0;i < data.length; ++i) {

              ops += '<option value="'+ data[i].id +'" >'+data[i].name+'</option>';
            }

            var option = '<li> 模块'
                + '  <select class="form-control input-sm input-small input-inline tooltips"  data-placement="left" id="model">'
                + '    <option value="1" selected="selected">图表</option>'
                + '  </select>'
                + '</li>'
                + '<li> 数据'
                + '  <select class="form-control input-sm input-small input-inline tooltips"  data-placement="left" id="source">'
                + ops
                + '  </select>'
                + '</li>';
            $('.theme-settings').append(option);

          }
        });
      }
    });

    $('#btnAddModel').click(function () {
      $.ajax({
        url: http_request + "/addIndexSetting",
        data: {
          "type":$('#model').val(),
          "modelId":$('#source').val()
        },
        success: function (data) {


          showModel(data.id,$('#source').val());
          $(".btn-theme-panel").removeClass("open");

        }
      });

    })
  }

  function initModel() {
    $.ajax({
      url: http_request + "/getIndexSetting",

      success: function (data) {

        for (var i = 0; i < data.length; ++i) {
          showModel(data[i].id,data[i].modelId);
        }
        var list = document.getElementsByClassName('page-content-inner').item(0);
        sortable = Sortable.create(list,{
          // Element dragging ended
          onEnd: function (/**Event*/evt) {

            var order = sortable.toArray();

            $.ajax({
              url: http_request + "/sortModel",
              data: {
                "sort":order.join(',')
              },
              success: function (data) {

                //window.location.reload(true);
              }
            });
          }
          
        });
        //$('.page-content-inner')
      }
    });


  }

  return {

    init: function () {
      initModel();
      initDropdownMenu();

    }
  };

}();

function deleteModel(id) {
  $.ajax({
    url: http_request + "/removeModel",
    data: {
      "id":id
    },
    success: function (data) {
      $('#model-' + id).remove();
      //window.location.reload(true);
    }
  });
}

jQuery(document).ready(function () {
  IndexBack.init();
});
