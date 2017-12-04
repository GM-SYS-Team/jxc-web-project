package com.gms.service.jxc.impl;

import com.gms.entity.jxc.PushJob;
import com.gms.service.jxc.PushJobService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author wuchuantong
 * @version V1.0
 * @Title: 推送服务实现类
 * @Package com.gms.service.jxc.impl
 * @Description: 推送服务实现类
 * @date 2017/11/19 1:03
 */
@Service("pushJobService")
@Transactional
public class PushJobServiceImpl implements PushJobService {


    @Override
    public List<PushJob> list(PushJob pushJob, Sort.Direction direction, String... properties) {
        return null;
    }

    @Override
    public PushJob findById(Long pushJobId) {
        return null;
    }

    @Override
    public PushJob save(PushJob pushJob) {
        return null;
    }

    @Override
    public PushJob submitPushJob(PushJob pushJob) {
        return null;
    }

    @Override
    public PushJob withdrawPushJob(PushJob pushJob) {
        return null;
    }
}
