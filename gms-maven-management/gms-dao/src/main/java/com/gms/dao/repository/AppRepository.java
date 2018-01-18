package com.gms.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gms.entity.jxc.App;

public interface AppRepository extends JpaRepository<App, Integer>,JpaSpecificationExecutor<App>{
	/**
	 * @author zhoutianqi
	 * @className AppRepository.java
	 * @date 2018年1月17日 下午3:53:14
	 * @description 
	 */
	

}
