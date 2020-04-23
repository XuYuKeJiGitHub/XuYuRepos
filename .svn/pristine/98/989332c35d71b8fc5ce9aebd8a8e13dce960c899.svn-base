/**
 * Created by Administrator on 2017/11/8.
 */
$("#calder").calendar({
        height:200

})
$("#rightTab").tabs({
       height:250,
        border:false
})
$(function(){
        var myChart = echarts.init($("#chart01")[0]);
//app.title = '堆叠柱状图';

        option = {

                tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {

                        left: 'left',
                        data: ['正常使用335','沉默期310','测试期234','其他135']
                },
                series : [
                        {
                                name: '访问来源',
                                type: 'pie',
                                radius : '55%',
                                center: ['50%', '60%'],
                                data:[
                                        {value:335, name:'正常使用335'},
                                        {value:310, name:'沉默期310'},
                                        {value:234, name:'测试期234'},
                                        {value:135, name:'其他135'}
                                ],
                                itemStyle: {
                                        emphasis: {
                                                shadowBlur: 10,
                                                shadowOffsetX: 0,
                                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                                        }
                                }
                        }
                ]
        };
        myChart.setOption(option);
});
$(function(){
        var myChart = echarts.init($("#chart02")[0]);
//app.title = '堆叠柱状图';

        option = {
                tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {

                        left: 'left',
                        data: ['已激活50','可测试120','测试期55','已停机200']
                },
                series : [
                        {
                                name: '访问来源',
                                type: 'pie',
                                radius : '55%',
                                center: ['50%', '60%'],
                                data:[
                                        {value:335, name:'已激活50'},
                                        {value:310, name:'可测试120'},
                                        {value:234, name:'测试期55'},
                                        {value:135, name:'已停机200'}
                                ],
                                itemStyle: {
                                        emphasis: {
                                                shadowBlur: 10,
                                                shadowOffsetX: 0,
                                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                                        }
                                }
                        }
                ]
        };
        myChart.setOption(option);
});
$(function(){
        var myChart = echarts.init($("#chart03")[0]);
//app.title = '堆叠柱状图';

        option = {
                tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {

                        left: 'left',
                        data: ['在用444','活卡待激活124','测试期346','用户报停278']
                },
                series : [
                        {
                                name: '访问来源',
                                type: 'pie',
                                radius : '55%',
                                center: ['50%', '60%'],
                                data:[
                                        {value:335, name:'在用444'},
                                        {value:310, name:'活卡待激活124'},
                                        {value:234, name:'测试期346'},
                                        {value:135, name:'用户报停278'}
                                ],
                                itemStyle: {
                                        emphasis: {
                                                shadowBlur: 10,
                                                shadowOffsetX: 0,
                                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                                        }
                                }
                        }
                ]
        };
        myChart.setOption(option);
});
function init() {

}
function notic() {
        $.ajax({
                url:'',
                type:'post',
                dataType:'json',
                success:function (data) {
                        if(data){
                                var res=data.data;
                                $.each(res,function (index) {
                                        var li='<li>'+res[index].value+'</li>';
                                        $("#notic").append(li);

                                })
                        }

                }
        })

}