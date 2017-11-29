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

import com.gms.entity.jxc.Supplier;
import com.gms.dao.repository.SupplierRepository;
import com.gms.service.jxc.SupplierService;
import com.gms.util.StringUtil;

/**
 * 供应商Service实现类
 * @author jxc 
 *
 */
@Service("supplierService")
public class SupplierServiceImpl implements SupplierService{

	@Resource
	private SupplierRepository supplierRepository;
	

	@Override
	public void save(Supplier supplier) {
		supplierRepository.save(supplier);
	}

	@Override
	public List<Supplier> list(Supplier supplier, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Supplier> pageSupplier=supplierRepository.findAll(new Specification<Supplier>() {
			
			@Override
			public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(supplier!=null){
					if(StringUtil.isNotEmpty(supplier.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+supplier.getName().trim()+"%"));
					}	
					if(supplier.getShopId()!=null && supplier.getShopId().intValue()>0){//默认等于零
						predicate.getExpressions().add(cb.equal(root.get("shopId"), supplier.getShopId()));
					}
				}
				return predicate;
			}
		}, pageable);
		return pageSupplier.getContent();
	}

	@Override
	public Long getCount(Supplier supplier) {
		Long count=supplierRepository.count(new Specification<Supplier>() {

			@Override
			public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(supplier!=null){
					if(StringUtil.isNotEmpty(supplier.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+supplier.getName().trim()+"%"));
					}	
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		supplierRepository.delete(id);
	}

	@Override
	public Supplier findById(Integer id) {
		return supplierRepository.findOne(id);
	}

	@Override
	public List<Supplier> findByName(String name) {
		return supplierRepository.findByName(name);
	}
	
	@Override
	public List<Supplier> findByShopAndName(Integer shopId,String name) {
		return supplierRepository.findByShopAndName(shopId,name);
	}

}
