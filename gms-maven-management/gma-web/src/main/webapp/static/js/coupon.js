function addCoupon(){
	$("#list_coupon").hide();
	$("#add_coupon").show();
}

function btnQuit(){
	$("#list_coupon").show();
	$("#add_coupon").hide();
}

function keyup(){
	$("#coupon-title").html($("#title").val());
}
$(".pull-left li a").click(function(){
	$(".pull-left li").removeClass("active")
	$(this).parent().addClass("active");
})
