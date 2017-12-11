package com.gms.service.jxc;

import com.gms.entity.jxc.PushJob;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author wuchuantong
 * @version V1.0
 * @Title: 推送服务
 * @Package com.gms.service.jxc
 * @Description: 推送服务
 * @date 2017/11/18 23:03
 */
public interface PushJobService {

    List<PushJob> list(PushJob pushJob, Sort.Direction direction, String... properties);

    List<PushJob> list(PushJob pushJob, Integer page, Integer pageSize, Sort.Direction direction, String... properties);


    PushJob findById(Long pushJobId);

    PushJob save(PushJob pushJob);

    void delete(Long pushJobId);


    /**
     * 提交推送任务至第三方推送平台上
     *
     * @param pushJobId 确认的推送任务
     * @return 已推送的PushJob
     */
    PushJob submitPushJob(Long pushJobId);


    /**
     * 根据第三方推送平台的任务ID撤回任务
     *
     * @param pushJobId 已提交的推送任务
     * @return 已撤回的PushJob
     */
    PushJob withdrawPushJob(Long pushJobId);

    public Long getCount(PushJob pushJob);


}
