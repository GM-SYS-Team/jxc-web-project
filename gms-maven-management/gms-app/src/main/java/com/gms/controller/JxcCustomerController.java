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
import com.gms.entity.jxc.Customer;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.exception.MyException;
import com.gms.service.jxc.CustomerService;
import com.gms.service.jxc.ShopService;
import com.google.common.base.Preconditions;

@Controller
@RequestMapping("/app/jxc/customer")
@NeedAuth
public class JxcCustomerController extends BaseAppController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ShopService shopService;

	/**
	 * 查询客户信息(page和rows不输入则为查询所有客户)
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
		Customer customer = new Customer();
		customer.setShopId(shopId);
		Long total = customerService.getCount(customer);
		if (page == null) {
			page = 1;
			rows = total.intValue();
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<Customer> customerList = customerService.list(customer, page, rows, Direction.ASC, "id");
		resultMap.put("rows", customerList);
		resultMap.put("total", total);
		return success(resultMap);
	}

	/**
	 * 新增客户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> save(Customer customer) throws Exception {
		Preconditions.checkArgument(StringUtils.isNotBlank(customer.getName()), "客户名称不能为空");
		Preconditions.checkArgument(customer.getName().length() < 50, "客户名称长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(customer.getContact()), "联系人不能为空");
		Preconditions.checkArgument(customer.getContact().length() < 50, "联系人长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(customer.getNumber()), "联系电话不能为空");
		Preconditions.checkArgument(customer.getNumber().length() < 50, "联系电话长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(customer.getAddress()), "联系地址不能为空");
		Preconditions.checkArgument(customer.getAddress().length() < 300, "联系地址长度不能超过300");
		Preconditions.checkNotNull(customer.getShopId(), "店铺ID不能为空");
		User user = getUser();
		validateUser(user, User.SHOPER);
		Shop shop = shopService.queryShopByShopIdAndUserId(customer.getShopId(), user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		customerService.save(customer);
		return success(customer);
	}

	/**
	 * 修改客户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/modify")
	@ResponseBody
	public Map<String, Object> modify(Customer customer) throws Exception {
		Preconditions.checkNotNull(customer.getId(), "id不能为空");
		Preconditions.checkArgument(StringUtils.isNotBlank(customer.getName()), "客户名称不能为空");
		Preconditions.checkArgument(customer.getName().length() < 50, "客户名称长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(customer.getContact()), "联系人不能为空");
		Preconditions.checkArgument(customer.getContact().length() < 50, "联系人长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(customer.getNumber()), "联系电话不能为空");
		Preconditions.checkArgument(customer.getNumber().length() < 50, "联系电话长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(customer.getAddress()), "联系地址不能为空");
		Preconditions.checkArgument(customer.getAddress().length() < 300, "联系地址长度不能超过300");
		Preconditions.checkNotNull(customer.getShopId(), "店铺ID不能为空");
		User user = getUser();
		validateUser(user, User.SHOPER);
		Shop shop = shopService.queryShopByShopIdAndUserId(customer.getShopId(), user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Customer tempCustomer = customerService.findById(customer.getId());
		if (tempCustomer == null) {
			throw new MyException("客户不存在");
		}
		if (tempCustomer.getShopId() != customer.getShopId()) {
			throw new MyException("客户不存在");
		}
		customerService.save(customer);
		return success(customer);
	}

	/**
	 * 客户删除
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(Customer customer) throws Exception {
		Preconditions.checkNotNull(customer.getId(), "id不能为空");
		User user = getUser();
		validateUser(user, User.SHOPER);
		Shop shop = shopService.queryShopByShopIdAndUserId(customer.getShopId(), user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Customer tempCustomer = customerService.findById(customer.getId());
		if (tempCustomer == null) {
			throw new MyException("客户不存在");
		}
		if (tempCustomer.getShopId() != customer.getShopId()) {
			throw new MyException("客户不存在");
		}
		customerService.delete(customer.getId());
		return success();

	}
}
