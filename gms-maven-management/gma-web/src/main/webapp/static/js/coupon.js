$(function () {  
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
})

function addCoupon() {
	$("#list_coupon").hide();
	$("#add_coupon").show();
}

function btnQuit() {
	$("#list_coupon").show();
	$("#add_coupon").hide();
}

function keyup() {
	$("#coupon-title").html($("#title").val());
}

function saveCoupon() {
	var coupon_title = $("#coupon_title").val();
	if(isEmpty(coupon_title)){
		alert("优惠券标题不能为空！");
		return false;
	}
	var batchNum = $("#batchNum").val();
	if(isEmpty(coupon_title)){
		alert("优惠券发放总量不能为空！");
		return false;
	}
	var discount_money = $("#discount_money").val();
	if(isEmpty(coupon_title)){
		alert("优惠券金额不能为空！");
		return false;
	}
	var order_money = $("#order_money").val();
	if(isEmpty(coupon_title)){
		alert("优惠券门槛金额不能为空！");
		return false;
	}
	var couponCount = $("#couponCount").val();
	if(isEmpty(coupon_title)){
		alert("优惠券每人限领数量不能为空！");
		return false;
	}
	var expiryDateStart = $("#expiryDateStart").val();
	if(isEmpty(coupon_title)){
		alert("优惠券使用起始日期不能为空！");
		return false;
	}
	var expiryDateStop = $("#expiryDateStop").val();
	if(isEmpty(coupon_title)){
		alert("优惠券使用结束日期不能为空！");
		return false;
	}
	var coupon_intro = $("#coupon_intro").val();
	if(isEmpty(coupon_title)){
		alert("优惠券说明不能为空！");
		return false;
	}
	$.ajax( {
		url : "/admin/coupon/save",
		type : "POST",
		data : {
			coupon_title : coupon_title,
			batchNum : batchNum,
			discount_money : discount_money,
			couponCount : couponCount,
			order_money : order_money,
			expiryDateStart : expiryDateStart,
			expiryDateStop : expiryDateStop,
			coupon_intro : coupon_intro
		},
		beforeSend : function() {
			$("#save_btn").attr("disabled", true);
			$("#quit_btn").attr("disabled", true);
		},
		success : function(data) {
			alert("22222222222");
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
	$(".pull-left li").removeClass("active")
	$(obj).parent().addClass("active");
	$.ajax( {
		url : "/admin/coupon/list",
		type : "POST",
		data : {
			num : num
		},
		success : function(data) {
		}
	});
}
