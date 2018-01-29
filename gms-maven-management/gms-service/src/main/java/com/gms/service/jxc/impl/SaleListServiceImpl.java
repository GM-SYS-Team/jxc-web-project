package com.gms.service.jxc.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gms.dao.repository.GoodsRepository;
import com.gms.dao.repository.GoodsTypeRepository;
import com.gms.dao.repository.SaleListGoodsRepository;
import com.gms.dao.repository.SaleListRepository;
import com.gms.entity.jxc.Customer;
import com.gms.entity.jxc.Goods;
import com.gms.entity.jxc.SaleList;
import com.gms.entity.jxc.SaleListGoods;
import com.gms.entity.jxc.Shop;
import com.gms.service.jxc.SaleListService;
import com.gms.util.StringUtil;

/**
 * 销售单Service实现类
 * @author jxc_
 *
 */
@Service("saleListService")
@Transactional
public class SaleListServiceImpl implements SaleListService{

	@Resource
	private SaleListRepository saleListRepository;
	
	@Resource
	private SaleListGoodsRepository saleListGoodsRepository;
	
	@Resource
	private GoodsRepository goodsRepository;
	
	@Resource
	private GoodsTypeRepository goodsTypeRepository;
	
	@Override
	public String getTodayMaxSaleNumber() {
		return saleListRepository.getTodayMaxSaleNumber();
	}

	@Transactional
	public void save(SaleList saleList, List<SaleListGoods> saleListGoodsList) {
		// 保存每个销售单商品
		for(SaleListGoods saleListGoods:saleListGoodsList){
			saleListGoods.setType(goodsTypeRepository.findOne(saleListGoods.getTypeId())); // 设置类别
			saleListGoods.setSaleList(saleList); // 设置采购单
			saleListGoodsRepository.save(saleListGoods);
			// 修改商品库存
			Goods goods=goodsRepository.findOne(saleListGoods.getGoodsId());
			goods.setInventoryQuantity(goods.getInventoryQuantity()-saleListGoods.getNum());
			goods.setState(2);
			goodsRepository.save(goods);
		}
		saleListRepository.save(saleList); // 保存销售单
	}

	@Override
	public List<SaleList> list(SaleList saleList,Integer page,Integer pageSize, Direction direction,
			String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<SaleList> pageSaleList = saleListRepository.findAll(new Specification<SaleList>(){

			@Override
			public Predicate toPredicate(Root<SaleList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(saleList!=null){
					if(saleList.getCustomer()!=null && saleList.getCustomer().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("customer").get("id"), saleList.getCustomer().getId()));
					}
					if(StringUtil.isNotEmpty(saleList.getSaleNumber())){
						predicate.getExpressions().add(cb.like(root.get("saleNumber"), "%"+saleList.getSaleNumber().trim()+"%"));
					}
					if(saleList.getState()!=null){
						predicate.getExpressions().add(cb.equal(root.get("state"), saleList.getState()));
					}
					if(saleList.getbSaleDate()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("saleDate"), saleList.getbSaleDate()));
					}
					if(saleList.geteSaleDate()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("saleDate"), saleList.geteSaleDate()));
					}
					if(saleList.getShopId()!=null && saleList.getShopId().intValue()>0){//默认等于零
						predicate.getExpressions().add(cb.equal(root.get("shopId"), saleList.getShopId()));
					}
				}
				return predicate;
			}
		  },pageable);
		return pageSaleList.getContent();
	}
	
	@Override
	public Long getCount(SaleList saleList){
		Long count=saleListRepository.count(new Specification<SaleList>() {

			@Override
			public Predicate toPredicate(Root<SaleList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(saleList!=null){
					if(saleList.getCustomer()!=null && saleList.getCustomer().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("customer").get("id"), saleList.getCustomer().getId()));
					}
					if(StringUtil.isNotEmpty(saleList.getSaleNumber())){
						predicate.getExpressions().add(cb.like(root.get("saleNumber"), "%"+saleList.getSaleNumber().trim()+"%"));
					}
					if(saleList.getState()!=null){
						predicate.getExpressions().add(cb.equal(root.get("state"), saleList.getState()));
					}
					if(saleList.getbSaleDate()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("saleDate"), saleList.getbSaleDate()));
					}
					if(saleList.geteSaleDate()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("saleDate"), saleList.geteSaleDate()));
					}
					if(saleList.getShopId()!=null && saleList.getShopId().intValue()>0){//默认等于零
						predicate.getExpressions().add(cb.equal(root.get("shopId"), saleList.getShopId()));
					}
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		saleListGoodsRepository.deleteBySaleListId(id);
		saleListRepository.delete(id);
	}

	@Override
	public SaleList findById(Integer id) {
		return saleListRepository.findOne(id);
	}

	@Override
	public void update(SaleList saleList) {
		saleListRepository.save(saleList);
	}

	@Override
	public List<Object> countSaleByDay(String begin, String end,Integer shopId) {
		return saleListRepository.countSaleByDay(begin, end, shopId);
	}

	@Override
	public List<Object> countSaleByMonth(String begin, String end,Integer shopId) {
		return saleListRepository.countSaleByMonth(begin, end, shopId);
	}

	@Override
	public int getSaleOrderPayedListCount(Shop shop, Integer state) {
		if(shop!= null){
			return saleListRepository.getSaleOrderPayedListCount(shop.getId(), state);
		}else{
			return saleListRepository.getSaleOrderPayedListCountAdmin(state);
		}
		
	}
}
