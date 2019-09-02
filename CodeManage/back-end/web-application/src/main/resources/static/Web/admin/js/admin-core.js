$(document).ready(function () {
    var trigger = $('.hamburger'),
        overlay = $('.overlay'),
        isClosed = false;

    trigger.hover(function () {
        hamburger_cross();
    });

    function hamburger_cross() {

        if (isClosed == true) {
            overlay.hide();
            trigger.removeClass('is-open');
            trigger.addClass('is-closed');
            isClosed = false;
        } else {
            overlay.show();
            trigger.removeClass('is-closed');
            trigger.addClass('is-open');
            isClosed = true;
        }
    }

    $('[data-toggle="offcanvas"]').click(function () {
        $('#wrapper').toggleClass('toggled');
    });
});

$('.statis-index').bind('onclick',function(){
    shows($(this).text());
});

function shows(a)
{
    $('#dropdownBtn').text(a);
}

function presentLog(vals) {
    // body...
}

// 基于准备好的dom，初始化echarts实例
var saleChart = echarts.init(document.getElementById('sale-graph'));

// 指定图表的配置项和数据
var option = {
    title: {
        text: '销售情况'
    },
    tooltip: {},
    legend: {
        // data:['销量']
    },
    xAxis: {
        axisLabel:{
            interval:0
        }
        // ,data: ["2019-01","2019-02","2019-03","2019-04","2019-05","2019-06"]
        ,data: []
    },
    yAxis: [{
        type : 'value'
    }],
    series: [{
        // name: '销量',
        type: 'line'
        // ,data: [5, 20, 36, 10, 10, 20]
        ,data:[]
    }]
};

saleChart.setOption(option);

function presentChart(x,y){
    // alert('present x = '+x);
    // alert('present y = '+y);

    y_new = [];
    for(ys in y)
    {
        y_new.push(Number(y[ys]));
    }
    // alert('present ynew = ' + y_new);
    var option = {
        xAxis:{data:x},
        series:[{data:y_new}]
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
        startDate: new Date(2017,12,1),
        endDate: today,
        language: 'zh-CN'
    }).on('changeDate', function(ev){
        var end = ev.date;
        $('#graph-end-date').datetimepicker("setStartDate", end);
        console.log(end);
    });

    $('#graph-end-date').datetimepicker({
        format: 'yyyy-mm',
        autoclose: 'true',
        startView: 'year',
        minView: 'year',
        startDate: new Date(2017,12,1),
        endDate: today,
        language: 'zh-CN'
    }).on('changeDate', function(ev){
        var start = ev.date;
        $('#graph-start-date').datetimepicker("setEndDate", start);
        console.log(end);
    });

    $('#table-start-date').datetimepicker({
        format: 'yyyy-mm-dd',
        autoclose: 'true',
        minView: 'month',
        startDate: new Date(2017,12,1),
        endDate: today,
        todayHighlight: true,
        language: 'zh-CN'
    }).on('changeDate', function(ev){
        var end = ev.date;
        $('#table-end-date').datetimepicker("setStartDate", end);
        console.log(end);
    });


    $('#table-end-date').datetimepicker({
        format: 'yyyy-mm-dd',
        autoclose: 'true',
        minView: 'month',
        startDate: new Date(2017,12,1),
        endDate: today,
        todayBtn: 'linked',
        todayHighlight: true,
        language: 'zh-CN'
    }).on('changeDate', function(ev){
        var start = ev.date;
        $('#table-start-date').datetimepicker("setEndDate", start);
        console.log(end);
    });
});


$('#buy-list-table').bootstrapTable({
    url: 'data1.json',
    columns: [{
        field: 'buy_date',
        title: '日期'
    }, {
        field: 'user_id',
        title: '用户'
    }, {
        field: 'mngr_id',
        title: '收银员'
    }, {
        field: 'goods_id',
        title: '商品id'
    },{
        field: 'buy_cnt',
        title: '购买数量'
    },{
        field: 'goods_type',
        title: '商品类别'
    },{
        field: 'goods_name',
        title: '商品名称'
    },{
        field: 'goods_price',
        title: '价格'
    },{
        field: 'total_pay',
        title: '总付款'
    }],
    striped: true,
    pagination: true,                   //是否显示分页（*）
    sortable: true,                     //是否启用排序
    showRefresh: true
});

// var $table;
//         //初始化bootstrap-table的内容
//         function InitMainTable () {
//             //记录页面bootstrap-table全局变量$table，方便应用
//             var queryUrl = '/TestUser/FindWithPager?rnd=' + Math.random()
//             $table = $('#grid').bootstrapTable({
//                 url: queryUrl,                      //请求后台的URL（*）
//                 method: 'GET',                      //请求方式（*）
//                 //toolbar: '#toolbar',              //工具按钮用哪个容器
//                 striped: true,                      //是否显示行间隔色
//                 cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
//                 pagination: true,                   //是否显示分页（*）
//                 sortable: true,                     //是否启用排序
//                 sortOrder: "asc",                   //排序方式
//                 sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
//                 pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
//                 pageSize: rows,                     //每页的记录行数（*）
//                 pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
//                 search: false,                      //是否显示表格搜索
//                 strictSearch: true,
//                 showColumns: true,                  //是否显示所有的列（选择显示的列）
//                 showRefresh: true,                  //是否显示刷新按钮
//                 minimumCountColumns: 2,             //最少允许的列数
//                 clickToSelect: true,                //是否启用点击选中行
//                 //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
//                 uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
//                 showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
//                 cardView: false,                    //是否显示详细视图
//                 detailView: false,                  //是否显示父子表
//                 //得到查询的参数
//                 queryParams : function (params) {
//                     //这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
//                     var temp = {
//                         rows: params.limit,                         //页面大小
//                         page: (params.offset / params.limit) + 1,   //页码
//                         sort: params.sort,      //排序列名
//                         sortOrder: params.order //排位命令（desc，asc）
//                     };
//                     return temp;
//                 },
//                 columns: [{
//                     checkbox: true,
//                     visible: true                  //是否显示复选框
//                 }, {
//                     field: 'Name',
//                     title: '姓名',
//                     sortable: true
//                 }, {
//                     field: 'Mobile',
//                     title: '手机',
//                     sortable: true
//                 }, {
//                     field: 'Email',
//                     title: '邮箱',
//                     sortable: true,
//                     formatter: emailFormatter
//                 }, {
//                     field: 'Homepage',
//                     title: '主页',
//                     formatter: linkFormatter
//                 }, {
//                     field: 'Hobby',
//                     title: '兴趣爱好'
//                 }, {
//                     field: 'Gender',
//                     title: '性别',
//                     sortable: true
//                 }, {
//                     field: 'Age',
//                     title: '年龄'
//                 }, {
//                     field: 'BirthDate',
//                     title: '出生日期',
//                     formatter: dateFormatter
//                 }, {
//                     field: 'Height',
//                     title: '身高'
//                 }, {
//                     field: 'Note',
//                     title: '备注'
//                 }, {
//                     field:'ID',
//                     title: '操作',
//                     width: 120,
//                     align: 'center',
//                     valign: 'middle',
//                     formatter: actionFormatter
//                 }, ],
//                 onLoadSuccess: function () {
//                 },
//                 onLoadError: function () {
//                     showTips("数据加载失败！");
//                 },
//                 onDblClickRow: function (row, $element) {
//                     var id = row.ID;
//                     EditViewById(id, 'view');
//                 },
//             });
//         };