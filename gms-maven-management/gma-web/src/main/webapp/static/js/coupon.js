$(function () { 
	couponAjax(0);
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
	$("#coupon-amount").html($("#couponAmount").val());
}
function keyupDate() {
	$("#coupon-date-section").html($("#expiryDate").val());
}
function keyupIntro() {
	$(".js-desc-detail").html($("#couponIntro").val());
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
	var couponAmount = $("#couponAmount").val();
	if(isEmpty(couponAmount)){
		alert("优惠券面值不能为空！");
		return false;
	}
	var couponCount = $("#couponCount").val();
	if(isEmpty(couponCount)){
		alert("优惠券每人限领数量不能为空！");
		return false;
	}
	var expiryDate = $("#expiryDate").val();
	if(isEmpty(expiryDate)){
		alert("优惠券使用起始日期不能为空！");
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
			couponAmount : couponAmount,
			couponCount : couponCount,
			expiryDate : expiryDate,
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
				couponAjax(0);
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
	couponAjax(num);
}
function couponAjax(num){
	$.ajax( {
		url : "/admin/coupon/list",
		type : "POST",
		data : {
			num : num
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
					html += "<tr>"
							     +"<td>"+ item.couponName +"</td>"
							     +"<td>"+ item.couponAmount +"</td>"
							     +"<td>"+ item.totalCount +"</td>"
							     +"<td>"+ item.remainCount +"</td>"
							     +"<td>"+ item.couponCount +"</td>"
							     +"<td>"+ new Date(item.expiryDateStart).format("yyyy-MM-dd") +"</td>"
							     +"<td>"+ new Date(item.expiryDateStop).format("yyyy-MM-dd") +"</td>";
					if(num == 1){
						html += "<td>"
								     +"<a href='javascript:ovid()' onclick='coupon_del(this,"+ item.id +")' class='btn btn-danger operation_btn'>删除</a>"
							     +"</td>";
					}
					html += "</tr>";
				})
				$("#coupon_tbody").html(html);
			}else{
				$(".js-list-empty-region").show();
				$("#table_list").hide();
			}
		}
	});
}
function showPage(param) {
	var num = $(".pull-left .active").attr("data_num");
    var currentPage = parseInt($("#search_page").val());
    var page_size = $("#page_size").val();
    var total_page = $("#total_page").val();
	var flag = 0;
	if('prePage'==param) {
		if(currentPage==1){
			currentPage ==1;
			flag = 1;
		}else{
			currentPage = currentPage-1;
		}
	}else if('nextPage'==param) {
		if(currentPage==total_page){
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
				}
			}
		});
	});
}

function chooseGoods(){
	goodsAjax();
	layer.open({
        type: 1,
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
				alert(str);
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

function goodsAjax(){
	$.ajax( {
		url : "/admin/goods/listHasInventoryQuantity",
		type : "GET",
		data : {
			page:1,
			rows:5
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
							     +"<td>"+ item.inventoryQuantity +"</td>"
							     +"</tr>";
				})
				$("#goods_tbody").html(html);
			}else{
				alert("暂无商品");
			}
		}
	});
}




