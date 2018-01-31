package com.gms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gms.annoation.NeedAuth;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.Supplier;
import com.gms.entity.jxc.User;
import com.gms.exception.MyException;
import com.gms.service.jxc.ShopService;
import com.gms.service.jxc.SupplierService;
import com.google.common.base.Preconditions;

@Controller
@RequestMapping("/app/jxc/supplier")
@NeedAuth
public class JxcSupplierController extends BaseAppController {

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private ShopService shopService;

	/**
	 * 查询供应商信息(page和rows不输入则为查询所有供应商)
	 * 
	 * @param shopId
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows, Integer shopId) throws Exception {
		Preconditions.checkNotNull(shopId, "店铺ID不能为空");
		if (shopId <= 0) {
			throw new MyException("非法请求");
		}
		User user = getUser();
		validateUser(user, User.SHOPER);
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Supplier supplier = new Supplier();
		supplier.setShopId(shopId);
		Long total = supplierService.getCount(supplier);
		if (page == null) {
			page = 1;
			rows = total.intValue();
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<Supplier> supplierList = supplierService.list(supplier, page, rows, Direction.ASC, "id");
		resultMap.put("rows", supplierList);
		resultMap.put("total", total);
		return success(resultMap);
	}

	/**
	 * 新增供应商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> save(Supplier supplier) throws Exception {
		Preconditions.checkArgument(StringUtils.isNotBlank(supplier.getName()), "供应商名称不能为空");
		Preconditions.checkArgument(supplier.getName().length() < 50, "供应商名称长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(supplier.getContact()), "联系人不能为空");
		Preconditions.checkArgument(supplier.getContact().length() < 50, "联系人长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(supplier.getNumber()), "联系电话不能为空");
		Preconditions.checkArgument(supplier.getNumber().length() < 50, "联系电话长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(supplier.getAddress()), "联系地址不能为空");
		Preconditions.checkArgument(supplier.getAddress().length() < 300, "联系地址长度不能超过300");
		Preconditions.checkNotNull(supplier.getShopId(), "店铺ID不能为空");
		User user = getUser();
		validateUser(user, User.SHOPER);
		
		Shop shop = shopService.queryShopByShopIdAndUserId(supplier.getShopId(), user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		supplierService.save(supplier);
		return success(supplier);
	}

	/**
	 * 修改供应商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/modify")
	@ResponseBody
	public Map<String, Object> modify(Supplier supplier) throws Exception {
		Preconditions.checkNotNull(supplier.getId(), "id不能为空");
		Preconditions.checkArgument(StringUtils.isNotBlank(supplier.getName()), "供应商名称不能为空");
		Preconditions.checkArgument(supplier.getName().length() < 50, "供应商名称长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(supplier.getContact()), "联系人不能为空");
		Preconditions.checkArgument(supplier.getContact().length() < 50, "联系人长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(supplier.getNumber()), "联系电话不能为空");
		Preconditions.checkArgument(supplier.getNumber().length() < 50, "联系电话长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(supplier.getAddress()), "联系地址不能为空");
		Preconditions.checkArgument(supplier.getAddress().length() < 300, "联系地址长度不能超过300");
		Preconditions.checkNotNull(supplier.getShopId(), "店铺ID不能为空");
		User user = getUser();
		validateUser(user, User.SHOPER);
		Shop shop = shopService.queryShopByShopIdAndUserId(supplier.getShopId(), user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Supplier tempSupplier = supplierService.findById(supplier.getId());
		if (tempSupplier == null) {
			throw new MyException("供应商不存在");
		}
		if (tempSupplier.getShopId() != supplier.getShopId()) {
			throw new MyException("供应商不存在");
		}
		supplierService.save(supplier);
		return success(supplier);
	}

	/**
	 * 供应商删除
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(Supplier supplier) throws Exception {
		Preconditions.checkNotNull(supplier.getId(), "id不能为空");
		User user = getUser();
		validateUser(user, User.SHOPER);
		Shop shop = shopService.queryShopByShopIdAndUserId(supplier.getShopId(), user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Supplier tempSupplier = supplierService.findById(supplier.getId());
		if (tempSupplier == null) {
			throw new MyException("供应商不存在");
		}
		if (tempSupplier.getShopId() != supplier.getShopId()) {
			throw new MyException("供应商不存在");
		}
		supplierService.delete(supplier.getId());
		return success();

	}
}
