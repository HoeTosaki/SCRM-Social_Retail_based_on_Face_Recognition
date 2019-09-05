// 基于准备好的dom，初始化echarts实例
var saleChart = echarts.init(document.getElementById('sale-graph'));

// 指定图表的配置项和数据
var chartOption = {
    title: {
        text: '销售情况'
    },
    tooltip: {},
    legend: {
        // data:['销量']
    },
    xAxis: {
        axisLabel: {
            interval: 0
        }
        // ,data: ["2019-01","2019-02","2019-03","2019-04","2019-05","2019-06"]
        ,
        data: []
    },
    yAxis: [{
        type: 'value'
    }],
    series: [{
        // name: '销量',
        type: 'line'
            // ,data: [5, 20, 36, 10, 10, 20]
            ,
        data: []
    }]
};

saleChart.setOption(chartOption);

function presentChart(x, y) {
    // alert('present x = '+x);
    // alert('present y = '+y);

    y_new = [];
    for (ys in y) {
        y_new.push(Number(y[ys]));
    }
    // alert('present ynew = ' + y_new);
    var option = {
        xAxis: {
            data: x
        },
        series: [{
            data: y_new
        }]
    }
    saleChart.setOption(option);
}

// presentChart(['1','2','3','4'],[1,2,3,4]);

$(function () {
    var today = new Date();
    $('#graph-start-date').datetimepicker({
        format: 'yyyy-mm',
        autoclose: 'true',
        startView: 'year',
        minView: 'year',
        startDate: new Date(2017, 12, 1),
        endDate: today,
        language: 'zh-CN'
    }).on('changeDate', function (ev) {
        var end = ev.date;
        $('#graph-end-date').datetimepicker("setStartDate", end);
    });

    $('#graph-end-date').datetimepicker({
        format: 'yyyy-mm',
        autoclose: 'true',
        startView: 'year',
        minView: 'year',
        startDate: new Date(2017, 12, 1),
        endDate: today,
        language: 'zh-CN'
    }).on('changeDate', function (ev) {
        var start = ev.date;
        $('#graph-start-date').datetimepicker("setEndDate", start);
    });

    $('#table-start-date').datetimepicker({
        format: 'yyyy-mm-dd',
        autoclose: 'true',
        minView: 'month',
        startDate: new Date(2017, 12, 1),
        endDate: today,
        todayHighlight: true,
        language: 'zh-CN'
    }).on('changeDate', function (ev) {
        var end = ev.date;
        $('#table-end-date').datetimepicker("setStartDate", end);
    });


    $('#table-end-date').datetimepicker({
        format: 'yyyy-mm-dd',
        autoclose: 'true',
        minView: 'month',
        startDate: new Date(2017, 12, 1),
        endDate: today,
        todayBtn: 'linked',
        todayHighlight: true,
        language: 'zh-CN'
    }).on('changeDate', function (ev) {
        var start = ev.date;
        $('#table-start-date').datetimepicker("setEndDate", start);
    });
});

$('#buy-list-table').bootstrapTable({
    // data: vals,
    columns: [{
        field: 'buy_date',
        title: '日期',
        sortable: true
    }, {
        field: 'user_id',
        title: '用户'
    }, {
        field: 'mngr_id',
        title: '收银员'
    }, {
        field: 'goods_id',
        title: '商品id'
    }, {
        field: 'buy_cnt',
        title: '购买数量'
    }, {
        field: 'goods_type',
        title: '商品类别'
    }, {
        field: 'goods_name',
        title: '商品名称'
    }, {
        field: 'goods_price',
        title: '价格'
    }, {
        field: 'total_pay',
        title: '总付款'
    }],
    // toolbar: '#toolbar',
    cache: true,
    striped: true,
    pagination: true, //是否显示分页（*）
    sortable: true, //是否启用排序
    sortOrder: 'des',
    search: true,
    strictSearch: false,
    showRefresh: true,
    sidePagination: "client",
    pageSize: 5,
    onLoadSuccess: function () {},
    onLoadError: function () {
        showTips("数据加载失败！");
    }
});

var TabelInit = function (vals) {
    var oTableInit = new Object();
    oTableInit.init = function () {
        $('#buy-list-table').bootstrapTable('load', vals);
    };
    return oTableInit;
}

function presentLog(vals) {
    // alert(vals[0]);
    var oTable = TabelInit(vals);
    oTable.init();
}

// presentLog([{buy_date:"2019-08-01", user_id: "0", mngr_id: "0", goods_id: "0", buy_cnt: "1",
//     goods_type: "123", goods_name: "123", goods_price: "1.00", total_pay: "1.00"}, {buy_date:"2019-08-20", user_id: "0", mngr_id: "0", goods_id: "0", buy_cnt: "1",
//     goods_type: "123", goods_name: "123", goods_price: "1.00", total_pay: "1.00"}]);