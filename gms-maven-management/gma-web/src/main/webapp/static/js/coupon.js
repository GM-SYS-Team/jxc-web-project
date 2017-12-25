$(function () { 
	var page_size = $("#page_size").val();
	couponAjax(0,1,page_size);
	$('#expiryDate').daterangepicker({ 
	    format: 'yyyy-MM-dd',
	    startDate: new Date(),
	    endDate: new Date(),
	    minDate:new Date(),
	    locale:{
	        applyLabel: '确认',
	        cancelLabel: '取消',
	        fromLabel: '从',
	        toLabel: '到',
	        weekLabel: 'W',
	        customRangeLabel: 'Custom Range',
	        daysOfWeek:["日","一","二","三","四","五","六"],
	        monthNames: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
	    }
	}, function (start, end, label) {
		$('#expiryDate').html(start.format('yyyy-MM-dd HH') + ' 至  ' + end.format('yyyy-MM-dd HH'));
	})
	
	$('.form_datetime').datetimepicker({
        language:  'fr',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0,
        showMeridian: 1,
        format: 'yyyy-mm-dd hh:ii',
        language:"zh-CN"
    });
	 today("startExpiryDate");
	 today("stopExpiryDate");
	 
	 keyupDate();
})

function today(input_id){
	var myDate = new Date();
	//获取当前年
	var year=myDate.getFullYear();
	//获取当前月
	var month=myDate.getMonth()+1;
	//获取当前日
	var date=myDate.getDate(); 
	var h=myDate.getHours();       //获取当前小时数(0-23)
	var m=myDate.getMinutes();     //获取当前分钟数(0-59)
	var now=year+'-'+p(month)+"-"+p(date)+" "+p(h)+':'+p(m);
	document.getElementById(input_id).value = now;
	function p(s) {
	    return s < 10 ? '0' + s: s;
	}
	$('.form_datetime').attr("data-date",now);
}

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
        (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
            RegExp.$1.length == 1 ? o[k] :
                ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

function addCoupon() {
	emptyText();
	$("#list_coupon").hide(1000);
	$("#add_coupon").show(1000);
}

function btnQuit() {
	$("#list_coupon").show(1000);
	$("#add_coupon").hide(1000);
}

function keyup() {
	$("#coupon_title_eg").html($("#couponName").val());
}

function keyupAmount() {
	var minAmount = $("#min_amount").val();
	$("#coupon-amount").html(minAmount);
	$("#max_amount").attr("min",minAmount);
}

function keyRadomAmount(){
	var minAmount = $("#min_amount").val();
	var maxAmount = $("#max_amount").val();
	$("#coupon-amount").html(minAmount+"~"+maxAmount);
}
function keyupDate() {
	var startExpiryDate = $("#startExpiryDate").val();
	var stopExpiryDate = $("#stopExpiryDate").val();
	$("#coupon-date-section").html(startExpiryDate+"~"+stopExpiryDate);
}
function keyupIntro() {
	$(".js-desc-detail").html($("#couponIntro").val());
}

function emptyText(){
	$("#couponName").val("");
	$("#totalCount").val("");
	$("#min_amount").val("");
	$("#max_amount").val("");
	/*$("#couponCount").val("");*/
	$("#goodsIds").html("");
	$("#couponIntro").val("");
}

function saveCoupon() {
	var couponName = $("#couponName").val();
	if(isEmpty(couponName)){
		alert("优惠券标题不能为空！");
		return false;
	}
	var totalCount = $("#totalCount").val();
	if(isEmpty(totalCount)){
		alert("优惠券发放总量不能为空！");
		return false;
	}
	var min_amount = $("#min_amount").val();
	var max_amount = $("#max_amount").val();
	if($("#set_amount input[type='checkbox']").is(':checked')){
		if(isEmpty(min_amount)){
			alert("优惠券随机面值最小值不能为空！");
			return false;
		}
		if(isEmpty(max_amount)){
			alert("优惠券随机面值最大值不能为空！");
			return false;
		}
		if(min_amount>max_amount){
			alert("优惠券随机面值设置有误！");
			return false;
		}
	}else{
		if(isEmpty(min_amount)){
			alert("优惠券面值不能为空！");
			return false;
		}
		max_amount = min_amount;
	}
	/*var couponCount = $("#couponCount").val();
	if(isEmpty(couponCount)){
		alert("优惠券每人限领数量不能为空！");
		return false;
	}*/
	var startExpiryDate = $("#startExpiryDate").val();
	if(isEmpty(startExpiryDate)){
		alert("优惠券使用起始日期不能为空！");
		return false;
	}
	var stopExpiryDate = $("#stopExpiryDate").val();
	if(isEmpty(stopExpiryDate)){
		alert("优惠券使用结束日期不能为空！");
		return false;
	}
	var goodsIds = $("#goodsIds").html();
	if(isEmpty(goodsIds)){
		alert("适用商品不能为空！");
		return false;
	}
	var couponIntro = $("#couponIntro").val();
	$.ajax( {
		url : "/admin/coupon/save",
		type : "POST",
		data : {
			couponName : couponName,
			totalCount : totalCount,
			minAmount : min_amount,
			maxAmount : max_amount,
			/*couponCount : couponCount,*/
			startExpiryDate : startExpiryDate,
			stopExpiryDate : stopExpiryDate,
			couponIntro : couponIntro,
			goodsIds:goodsIds
		},
		beforeSend : function() {
			$("#save_btn").attr("disabled", true);
			$("#quit_btn").attr("disabled", true);
		},
		success : function(data) {
			if(data.success){
				alert("插入成功！");
				$("#add_coupon").hide();
				$("#list_coupon").show();
				$(".pull-left li").removeClass("active");
				$(".pull-left li:first").addClass("active");
				var page_size = $("#page_size").val();
				couponAjax(0,1,page_size);
			}
		}
	});
}

function isEmpty(value){
	if(value == "" || value == null){
		return true;
	}
	return false;
}

function getCouponList(obj) {
	var num = $(obj).parent().attr("data_num");
	$(".pull-left li").removeClass("active");
	$(obj).parent().addClass("active");
	var page_size = $("#page_size").val();
	couponAjax(num,1,page_size);
}
function couponAjax(num,currentPage,page_size){
	btnQuit();
	$.ajax( {
		url : "/admin/coupon/list",
		type : "POST",
		data : {
			num : num,
			page:currentPage,
			rows:page_size
		},
		success : function(data) {
			var html = "";
			if(data.size>0){
				if(num == 1){
					$(".oper_th").show();
				}else{
					$(".oper_th").hide();
				}
				$("#list_coupon").show(1000);
				$("#add_coupon").hide(1000);
				$(".js-list-empty-region").hide();
				$("#table_list").show();
				$.each(data.couponList, function (index, item) {
					var value = "";
					if(item.minAmount == item.maxAmount){
						value = item.minAmount;
					}else{
						value = item.minAmount+"~"+item.maxAmount;
					}
					html += "<tr>"
							     +"<td>"+ item.couponName +"</td>"
							     +"<td>"+ value +"</td>"
							     +"<td>"+ item.totalCount +"</td>"
							     +"<td>"+ item.remainCount +"</td>"
							   /*  +"<td>"+ item.couponCount +"</td>"*/
							     +"<td>"+ new Date(item.expiryDateStart).format("yyyy-MM-dd hh:mm") +"</td>"
							     +"<td>"+ new Date(item.expiryDateStop).format("yyyy-MM-dd hh:mm") +"</td>";
					if(num == 1){
						html += "<td>"
								     +"<a href='javascript:ovid()' onclick='coupon_del(this,"+ item.id +")' class='btn btn-danger operation_btn'>删除</a>";
						if(item.status !=1){
							html += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:ovid()' onclick='coupon_share(this,"+ item.id +")' class='btn bg-deep-blue operation_btn' id='share_btn'>共享</a>"
						}
						html +="</td>";
					}
					html += "</tr>";
				})
				$("#coupon_tbody").html(html);
				if(data.size>page_size){
					$("#fenye_coupon").show();
					$("#total_page").val(data.size);
					$("#search_page").val(currentPage);
					$("#current_page").html(currentPage);
					$(".start_num").html((currentPage-1)*page_size+1);
					$(".end_num").html((currentPage-1)*page_size + data.couponList.length);
					$(".total_num").html(data.size);
				}else{
					$("#fenye_coupon").hide();
				}
			}else{
				$(".js-list-empty-region").show();
				$("#table_list").hide();
			}
		}
	});
}

function showPageCoupon(param) {
	var num = $(".pull-left li").attr("data_num");
    var currentPage = parseInt($("#search_page").val());
    var page_size = $("#page_size").val();
    var total_page = $("#total_page").val();
	var flag = 0;
	if('prePage'==param) {
		if(currentPage==1){
			currentPage =1;
			flag = 1; cursor: ;
		}else{
			currentPage = currentPage-1;
		}
	}else if('nextPage'==param) {
		if(currentPage*page_size>=total_page){
			currentPage = total_page;
			flag = 1;
		}else{
			currentPage = currentPage+1;
		}
	}
	if(flag==0){
		couponAjax(num,currentPage,page_size);
	 }
}

function coupon_del(obj,id){
	layer.confirm('确认要删除吗？',{icon:0,},function(index){
		$(obj).parents("tr").remove();
		$.ajax( {
			url : "/admin/coupon/delete",
			type : "post",
			data : {
				id:id
			},
			success : function(data) {
				if(data.success){
					layer.msg('已删除!',{icon:1,time:1000});
					if($("#coupon_tbody").html()==""){
						$(".js-list-empty-region").show();
						$("#table_list").hide();
					}
				}
			}
		});
	});
}

function coupon_share(obj,id){
	layer.confirm('确认要共享吗？',{icon:0,},function(index){
		$(obj).remove();
		$.ajax( {
			url : "/admin/coupon/shareCoupon",
			type : "post",
			data : {
				id:id
			},
			success : function(data) {
				if(data.success){
					layer.msg('共享成功!',{icon:1,time:1000});
				}
			}
		});
	});
}

function chooseGoods(){
	var currentPage = parseInt($("#goods_search_page").val());
    var page_size = $("#goods_page_size").val();
	goodsAjax(currentPage,page_size);
	layer.open({
        type: 1,
        offset: '50px',
        title: '选择适用商品',
		maxmin: true, 
		shadeClose: false, //点击遮罩关闭层
        area : ['800px' , ''],
        content:$('#ad_goods'),
		btn:['确定','取消'],
		yes:function(index,layero){	
			var str = "";
			$("input[name='goodsId']:checked").each(function(){ 
				str += $(this).val()+","; 
			}) 
			if(str ==""){
			   layer.alert("请选择使用商品",{
				   title: '提示框',				
				   icon:0,								
			    }); 
	            return false;            
	        }else{
	        	$("#goodsIds").html(str.substring(0, str.length-1));
	        	layer.close(index);	     
	        } 
	   }
    })
}

function goodsAjax(currentPage,page_size){
	$.ajax( {
		url : "/admin/goods/listHasInventoryQuantity",
		type : "GET",
		data : {
			page:currentPage,
			rows:page_size
		},
		success : function(data) {
			var html = "";
			if(data.total>0){
				$.each(data.rows, function (index, item) {
					html += "<tr>"
							     +"<td><label><input type='checkbox' name='goodsId' class='ace' value='"+ item.id +"'/><span class='lbl'></span></label></td>"
							     +"<td>"+ item.name +"</td>"
							     +"<td>"+ item.model +"</td>"
							     +"<td>"+ item.unit +"</td>"
							     +"<td>"+ item.sellingPrice +"</td>"
							    /* +"<td>"+ item.inventoryQuantity +"</td>"*/
							     +"</tr>";
				})
				$("#goods_tbody").html(html);
				if(data.total>page_size){
					$("#fenye_goods").show();
					$("#goods_total_page").val(data.total);
					$("#goods_search_page").val(currentPage);
					$("#current_page_goods").html(currentPage);
					$(".start_num_goods").html((currentPage-1)*page_size+1);
					$(".end_num_goods").html((currentPage-1)*page_size + data.rows.length);
					$(".total_num_goods").html(data.total);
				}
			}else{
				alert("暂无商品");
			}
		}
	});
}


function showPage(param) {
    var currentPage = parseInt($("#goods_search_page").val());
    var page_size = $("#goods_page_size").val();
    var total_page = $("#goods_total_page").val();
	var flag = 0;
	if('prePage'==param) {
		if(currentPage==1){
			currentPage =1;
			flag = 1;
		}else{
			currentPage = currentPage-1;
		}
	}else if('nextPage'==param) {
		if(currentPage*page_size>=total_page){
			currentPage = total_page;
			flag = 1;
		}else{
			currentPage = currentPage+1;
		}
	}
	if(flag==0){
		goodsAjax(currentPage,page_size);
	 }
}

function is_check(){
	if($("#set_amount input[type='checkbox']").is(':checked')){
    	$(".js-random").show();
    }else{
    	$(".js-random").hide();
    }
}
    


