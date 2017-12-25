package com.gms.dao.repository;

import com.gms.entity.jxc.PushJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wuchuantong
 * @version V1.0
 * @Title: pushJob持久类
 * @Package com.gms.dao.repository
 * @Description: pushJob持久类
 * @date 2017/11/18 23:05
 */
public interface PushJobRepository extends JpaRepository<PushJob, Long>, JpaSpecificationExecutor<PushJob> {
}
