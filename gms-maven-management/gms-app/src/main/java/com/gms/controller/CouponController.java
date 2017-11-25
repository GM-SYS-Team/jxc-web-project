package com.gms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gms.annoation.NeedAuth;

@Controller
@RequestMapping("/app/coupon")
@NeedAuth
public class CouponController extends BaseAppController {
	
}
