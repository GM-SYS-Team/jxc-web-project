package com.gms.service.jxc;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.gms.entity.jxc.App;
import com.gms.entity.jxc.User;

public interface AppService {
	/**
	 * @author zhoutianqi
	 * @className AppService.java
	 * @date 2018年1月17日 下午3:56:07
	 * @description 
	 */
	public void save(App app);
	
	public List<App> list(App app,Integer page,Integer pageSize,Direction direction,String... properties);
	
	public Long getCount(App app);
	
	public App findById(Integer id);
	
	public void delete(Integer id);

}
