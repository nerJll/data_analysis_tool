$(function () {
    $.ajax({
        type: 'get',
        url: baseUrl + '/size/analysis/index',
        dataType: 'json',
        success: function (data) {
            if (data.success) {
                var column = data.data.column;
                var cake = data.data.cake;
                initColumn(column.title,column.leData,column.xData,column.sData);
                initCake(cake.title,cake.leData,cake.sData);
            } else {
                initColumn();
                initCake();
            }
        }
    });
});

//柱状图
function initColumn(title, leData, xDate, sData) {
    // 基于准备好的dom，初始化echarts实例
    var column = echarts.init(document.getElementById('column'));
    // 指定图表的配置项和数据
    var cop = {
        title: {
            text: 'ECharts 入门示例'
        },
        tooltip: {},
        legend: {
            data: ['销量1', '销量2', '销量3']
        },
        xAxis: {
            data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
        },
        yAxis: {},
        series: [{
            name: '销量1',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20]
        }, {
            name: '销量2',
            type: 'bar',
            data: [3, 21, 32, 6, 11, 18]
        }, {
            name: '销量3',
            type: 'bar',
            data: [4, 18, 38, 12, 7, 21]
        }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    column.setOption(cop);
}

//饼状图
function initCake(title, leData, sData) {
    var cake = echarts.init(document.getElementById("cake"));
    var cap = {
        title: {
            text: '某站点用户访问来源',
            subtext: '纯属虚构',
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
        },
        series: [
            {
                name: '罩杯',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: [
                    {value: 335, name: '直接访问'},
                    {value: 310, name: '邮件营销'},
                    {value: 234, name: '联盟广告'},
                    {value: 135, name: '视频广告'},
                    {value: 1548, name: '搜索引擎'}
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    cake.setOption(cap);
}