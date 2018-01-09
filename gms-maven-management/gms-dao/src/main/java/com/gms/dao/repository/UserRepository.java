package com.gms.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.User;

/**
 * 用户Repository接口
 * @author jxc 
 *
 */
public interface UserRepository extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User>{

	/**
	 * 根据用户名查找用户实体
	 * @param userName
	 * @return
	 */
	@Query(value="select * from t_user where user_name=?1",nativeQuery=true)
	public User findByUserName(String userName);

	@Query(value="select * from t_user where uuid=?1", nativeQuery=true)
	public User findByUuid(String uuid);

	@Query(value="select * from t_user where phone_num=?1", nativeQuery=true)
	public User findUserByTelephone(String telephone);


	@Query(value="select * from t_user where phone_num=?1 and user_type=?2", nativeQuery=true)
	public User findUserByTelephone(String telephone, String userType);
}
