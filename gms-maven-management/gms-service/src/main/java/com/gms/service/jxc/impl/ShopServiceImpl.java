package com.gms.service.jxc.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gms.entity.jxc.Shop;
import com.gms.dao.repository.ShopRepository;
import com.gms.service.jxc.ShopService;
import com.gms.util.StringUtil;

/**
 * 用户Service实现类
 * @author jxc 
 *
 */
@Service("ShopService")
public class ShopServiceImpl implements ShopService{

	@Resource
	private ShopRepository shopRepository;
	
	
	@Override
	public Shop findByShopName(String ShopName) {
		return shopRepository.findByShopName(ShopName);
	}

	@Override
	public Shop findById(Integer id) {
		return shopRepository.findOne(id);
	}

	@Override
	public void save(Shop Shop) {
		shopRepository.save(Shop);
	}

	@Override
	public List<Shop> list(Shop shop, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Shop> pageShop=shopRepository.findAll(new Specification<Shop>() {
			
			@Override
			public Predicate toPredicate(Root<Shop> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(shop!=null){
					if(StringUtil.isNotEmpty(shop.getShopName())){
						predicate.getExpressions().add(cb.like(root.get("shopName"), "%"+shop.getShopName().trim()+"%"));
					}	
					if(shop.getUserId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("userId"), shop.getUserId()));	
					}
				}
				
				return predicate;
			}
		}, pageable);
		return pageShop.getContent();
	}

	@Override
	public Long getCount(Shop Shop) {
		Long count=shopRepository.count(new Specification<Shop>() {

			@Override
			public Predicate toPredicate(Root<Shop> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(Shop!=null){
					if(StringUtil.isNotEmpty(Shop.getShopName())){
						predicate.getExpressions().add(cb.like(root.get("shopName"), "%"+Shop.getShopName().trim()+"%"));
					}	
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		shopRepository.delete(id);
	}
	
	@Override
	public List<Shop> findPhoneNum(String telephone) {
		return shopRepository.findPhoneNum(telephone);
	}

	@Override
	public Shop queryShopByShopIdAndUserId(Integer shopId, Integer userId) {
		return shopRepository.queryShopByShopIdAndUserId(shopId, userId);
	}

	@Override
	public List<Shop> findByUserId(Integer userId) {
		return shopRepository.findByUserId(userId);
	}
}