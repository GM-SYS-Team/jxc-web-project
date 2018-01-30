package com.gms.service.jxc.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gms.entity.jxc.GoodsType;
import com.gms.dao.repository.GoodsTypeRepository;
import com.gms.service.jxc.GoodsTypeService;

/**
 * 商品类别Service实现类
 * @author jxc 
 *
 */
@Service("goodsTypeService")
public class GoodsTypeServiceImpl implements GoodsTypeService{

	@Resource
	private GoodsTypeRepository goodsTypeRepository;
	
	@Override
	public void save(GoodsType goodsType) {
		goodsTypeRepository.save(goodsType);
	}

	@Override
	public void delete(Integer id) {
		goodsTypeRepository.delete(id);
	}
	@Override
	public List<GoodsType> getAllByParentId(int parentId) {
		return goodsTypeRepository.findByParentId(parentId);
	}

	@Override
	public List<GoodsType> getAllByParentIdAndShopId(int parentId,Integer shopId) {
		return goodsTypeRepository.findByParentIdAndShopId(parentId,shopId);
	}

	@Override
	public GoodsType findById(Integer id) {
		return goodsTypeRepository.findOne(id);
	}

	@Override
	public List<GoodsType> getAllByShopId(Integer shopId) {
		return goodsTypeRepository.findAllByShopId(shopId);
	}

}
