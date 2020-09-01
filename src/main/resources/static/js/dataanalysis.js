$(function () {
    $.ajax({
        type: 'get',
        url: baseUrl + '/size/analysis/index',
        dataType: 'json',
        success: function (data) {
            if (data.success) {
                // var column = data.data.column;
                var cake = data.data.cake;
                // initColumn(column.title, column.leData, column.xData, column.sData);
                initCake(cake.title, cake.leData, cake.sData);
            } else {
                // initColumn();
                initCake();
            }
        }
    });
});

//柱状图
// function initColumn(title, leData, xDate, sData) {
//     // 基于准备好的dom，初始化echarts实例
//     var column = echarts.init(document.getElementById('column'));
//     // 指定图表的配置项和数据
//     var cop = {
//         title: {
//             text: title
//         },
//         tooltip: {},
//         legend: {
//             data: leData
//         },
//         xAxis: {
//             data: xDate
//         },
//         yAxis: {},
//         series: [{
//             name: leData[0],
//             type: 'bar',
//             data: sData[0]
//         }, {
//             name: leData[1],
//             type: 'bar',
//             data: sData[1]
//         }, {
//             name: leData[2],
//             type: 'bar',
//             data: sData[2]
//         }
//         ]
//     };
//     // 使用刚指定的配置项和数据显示图表。
//     column.setOption(cop);
// }

//饼状图
function initCake(title, leData, sData) {
    if (sData.length === 0) {
        alert('数组返回错误，请联系管理员');
    }
    var cake = echarts.init(document.getElementById("cake"));
    var sData1 = [];
    for (var i = 0; i < leData.length; i++) {
        var sd = {value: sData[i], name: leData[i]};
        sData1.push(sd);
    }
    var cap = {
        title: {
            text: title,
            subtext: '娱乐测试',
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: leData
        },
        series: [
            {
                name: '罩杯',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: sData1,
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