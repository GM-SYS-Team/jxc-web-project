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
			$(".shop_name").html(data.shop.contactName);
			$(".shop_business").html(data.shop.business);
			$(".shop_contactName").html(data.shop.contactName);
			$(".shop_contactCode").html(data.shop.contactCode);
			$(".shop_phone").html(data.shop.phoneNum);
			$(".shop_address").html(data.shop.shopAddress);
			$(".shop_createTime").html(data.shop.createTime);
		}
	});
}