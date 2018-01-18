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
			if(data.shop == null){
				$("#position1").html(data.total_user);
				$("#position2").html(data.total_shop);
				$("#position3").html(data.totalCount);
				$("#position4").html(data.before_date_count);
				
				$("#div1 h3").html("用户总量");
				$("#div2 h3").html("店铺总量");
				$("#div3 h3").html("优惠券总量");
				$("#div4 h3").html("优惠券未开始");
				
				$("#shop_info").hide();
			}else{
				$("#position1").html(data.totalCount);
				$("#position2").html(data.before_date_count);
				$("#position3").html(data.between_date_count);
				$("#position4").html(data.out_date_count);
				
				$("#shop_info").show();
				$(".shop_name").html(data.shop.shopName);
				$(".shop_business").html(data.shop.business);
				$(".shop_contactName").html(data.shop.contactName);
				$(".shop_contactCode").html(data.shop.contactCode);
				$(".shop_phone").html(data.shop.phoneNum);
				$(".shop_address").html(data.shop.shopAddress);
				$(".shop_createTime").html(data.shop.createTime);
				
				if(data.shop.pictureAddress != "" && data.shop.pictureAddress!=null){
					$('#shopPic').attr('src',data.shop.pictureAddress);
				}else{
					$('#shopPic').attr('src',"../static/images/nopic.jpg");
				}
			}
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