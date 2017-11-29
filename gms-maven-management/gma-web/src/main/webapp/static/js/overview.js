$(document).ready(function(){
	queryContent()
});

function queryContent(){
	$.ajax( {
		url : "/admin/overview/map",
		type : "POST",
		data : {
		},
		success : function(data) {
			$("#total_count").html(data.totalCount);
			$("#before_date_count").html(data.before_date_count);
			$("#between_date_count").html(data.between_date_count);
			$("#out_date_count").html(data.out_date_count);
			
			$(".shop_name").html(data.shop.contactName);
			$(".shop_business").html(data.shop.business);
			$(".shop_contactName").html(data.shop.contactName);
			$(".shop_contactCode").html(data.shop.contactCode);
			$(".shop_phone").html(data.shop.phoneNum);
			$(".shop_address").html(data.shop.shopAddress);
			$(".shop_createTime").html(data.shop.createTime);
			
			$("#jin_num1").html("("+data.jin_num1+")");
			$("#jin_num2").html("("+data.jin_num2+")");
			$("#sale_num1").html("("+data.sale_num1+")");
			$("#sale_num2").html("("+data.sale_num2+")");
			$("#kucun_num").html("("+data.kucun_num+")");
			
			 require.config({
				 paths: {
		                echarts: '../static/bgManager/js/dist'
		            }
		        });
		        require(
		            [
		                'echarts',
						'echarts/theme/macarons',
		                'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
		                'echarts/chart/bar'
		            ],
		            function (ec,theme) {
		                var myChart = ec.init(document.getElementById('main'),theme);
		               option = {
		    title : {
		       /* text: '当周交易记录',
		        subtext: '每周7天的交易记录'*/
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['优惠券领取情况']
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            boundaryGap : false,
		            data :data.dateArr
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            axisLabel : {
		                formatter: '{value}单'
		            }
		        }
		    ],
		    series : [
		        {
		            name:'领取数量',
		            type:'line',
		            data:data.amountArr,
		            markPoint : {
		                data : [
		                    {type : 'max', name: '最大值'},
		                    {type : 'min', name: '最小值'}
		                ]
		            },
		            markLine : {
		                data : [
		                    {type : 'average', name: '平均值'}
		                ]
		            }
		        }
				   
		    ]
		};
              
		myChart.setOption(option);
		}
		);

		}
	});
}