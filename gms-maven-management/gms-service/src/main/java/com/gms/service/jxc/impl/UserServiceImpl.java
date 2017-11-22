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

import com.gms.entity.jxc.User;
import com.gms.dao.repository.UserRepository;
import com.gms.service.jxc.UserService;
import com.gms.util.Constant;
import com.gms.util.StringUtil;

/**
 * 用户Service实现类
 * @author jxc 
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource
	private UserRepository userRepository;
	
	
	@Override
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Override
	public User findById(Integer id) {
		return userRepository.findOne(id);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public List<User> list(User user, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<User> pageUser=userRepository.findAll(new Specification<User>() {
			
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(user!=null){
					if(StringUtil.isNotEmpty(user.getUserName())){
						predicate.getExpressions().add(cb.like(root.get("userName"), "%"+user.getUserName().trim()+"%"));
					}
					if(user.getShopId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("shopId"), user.getShopId()));
					}
					predicate.getExpressions().add(cb.notEqual(root.get("id"), 1)); // 管理员除外
				}
				return predicate;
			}
		}, pageable);
		return pageUser.getContent();
	}

	@Override
	public Long getCount(User user) {
		Long count=userRepository.count(new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(user!=null){
					if(StringUtil.isNotEmpty(user.getUserName())){
						predicate.getExpressions().add(cb.like(root.get("userName"), "%"+user.getUserName().trim()+"%"));
					}	
					predicate.getExpressions().add(cb.notEqual(root.get("id"), 1)); // 管理员除外
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		userRepository.delete(id);
	}

	@Override
	public User findByUuid(String uuid) {
		return userRepository.findByUuid(uuid);
	}

	@Override
	public User findUserByTelephone(String telephone) {
		return userRepository.findUserByTelephone(telephone);
	}


}
