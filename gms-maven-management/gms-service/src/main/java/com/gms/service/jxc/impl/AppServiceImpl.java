package com.gms.service.jxc.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gms.dao.repository.AppRepository;
import com.gms.entity.jxc.App;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.AppService;
import com.gms.util.DateUtil;
import com.gms.util.StringUtil;
@Service
public class AppServiceImpl implements AppService{
	/**
	 * @author zhoutianqi
	 * @className AppServiceImpl.java
	 * @date 2018年1月17日 下午3:58:02
	 * @description 
	 */
	@Autowired
	private AppRepository appRepository;
	
	@Override
	public void save(App app) {
		appRepository.save(app);
	}

	@Override
	public List<App> list(App app, Integer page, Integer pageSize,
			Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<App> pageShop=appRepository.findAll(new Specification<App>() {
			
			@Override
			public Predicate toPredicate(Root<App> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(app!=null){
					if(StringUtil.isNotEmpty(app.getAppName())){
						predicate.getExpressions().add(cb.like(root.get("appName"), "%"+app.getAppName().trim()+"%"));
					}	
					if (app.getBtime()!=null) {
						predicate.getExpressions()
								.add(cb.greaterThanOrEqualTo(root.get("createTime"), app.getBtime()));
					}
					if (app.getEtime()!=null) {
						predicate.getExpressions()
								.add(cb.lessThanOrEqualTo(root.get("createTime"), app.getEtime()));
					}
				}
				
				return predicate;
			}
		}, pageable);
		return pageShop.getContent();
	}
	
	@Override
	public Long getCount(App app) {
		Long count = appRepository.count(new Specification<App>() {

			@Override
			public Predicate toPredicate(Root<App> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if (app != null) {
					if (StringUtil.isNotEmpty(app.getAppName())) {
						predicate.getExpressions()
								.add(cb.like(root.get("appName"), "%" + app.getAppName().trim() + "%"));
					}
					if (app.getBtime()!=null) {
						predicate.getExpressions()
								.add(cb.greaterThanOrEqualTo(root.get("createTime"), app.getBtime()));
					}
					if (app.getEtime()!=null) {
						predicate.getExpressions()
								.add(cb.lessThanOrEqualTo(root.get("createTime"), app.getEtime()));
					}
				}
				return predicate;
			}
		});
		return count;
	}
	
	@Override
	public App findById(Integer id) {
		return appRepository.findOne(id);
	}

	@Override
	public void delete(Integer id) {
		appRepository.delete(id);
	}


}
