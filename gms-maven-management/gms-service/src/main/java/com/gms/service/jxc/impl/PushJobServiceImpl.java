package com.gms.service.jxc.impl;

import com.gms.dao.repository.PushJobRepository;
import com.gms.entity.jxc.PushJob;
import com.gms.service.jxc.PushJobService;
import com.gms.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.Timestamp;
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

    @Resource
    private PushJobRepository pushJobRepository;

    @Override
    public List<PushJob> list(PushJob pushJob, Sort.Direction direction, String... properties) {
        return null;
    }

    @Override
    public List<PushJob> list(PushJob pushJob, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = new PageRequest(page - 1, pageSize, direction, properties);
        Page<PushJob> pagePushJob = pushJobRepository.findAll(new Specification<PushJob>() {

            @Override
            public Predicate toPredicate(Root<PushJob> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (pushJob != null) {
                    if (StringUtil.isNotEmpty(pushJob.getTitle())) {
                        predicate.getExpressions().add(cb.like(root.get("title"), "%" + pushJob.getTitle().trim() + "%"));
                    }
                }
                return predicate;
            }
        }, pageable);
        return pagePushJob.getContent();
    }

    @Override
    public PushJob findById(Long pushJobId) {
        return null;
    }

    @Override
    public PushJob save(PushJob pushJob) {
        if (pushJob.getId() == null) {
            pushJob.setCreateTime(new Timestamp(System.currentTimeMillis()));
            pushJob.setPushPlatform("XIAOMI");
            pushJob.setPushStatus("1");
            pushJob.setOpenType("webview");
            pushJob.setPushType("ALL");
            pushJob.setValid("Y");
        } else {
            PushJob storedPushJob = pushJobRepository.getOne(pushJob.getId());
            storedPushJob.setTitle(pushJob.getTitle());
            storedPushJob.setContent(pushJob.getContent());
            storedPushJob.setUrl(pushJob.getUrl());
            storedPushJob.setPushTime(pushJob.getPushTime());
            pushJob = storedPushJob;
        }
        pushJob.setModifyTime(new Timestamp(System.currentTimeMillis()));
        return pushJobRepository.save(pushJob);
    }

    @Override
    public void delete(Long pushJobId) {
        PushJob storedPushJob = pushJobRepository.getOne(pushJobId);
        if (storedPushJob != null) {
            storedPushJob.setPushStatus("0");
            pushJobRepository.save(storedPushJob);
        }
    }

    @Override
    public PushJob submitPushJob(Long pushJobId) {
        PushJob storedPushJob = pushJobRepository.getOne(pushJobId);
        if (storedPushJob != null) {
            storedPushJob.setPushStatus("2");
            pushJobRepository.save(storedPushJob);
        }
        return storedPushJob;
    }

    @Override
    public PushJob withdrawPushJob(Long pushJobId) {
        PushJob storedPushJob = pushJobRepository.getOne(pushJobId);
        if (storedPushJob != null) {
            storedPushJob.setPushStatus("3");
            pushJobRepository.save(storedPushJob);
        }
        return storedPushJob;
    }

    @Override
    public Long getCount(PushJob pushJob) {
        Long count = pushJobRepository.count(new Specification<PushJob>() {

            @Override
            public Predicate toPredicate(Root<PushJob> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (pushJob != null) {
                    if (StringUtil.isNotEmpty(pushJob.getTitle())) {
                        predicate.getExpressions().add(cb.like(root.get("title"), "%" + pushJob.getTitle().trim() + "%"));
                    }
                }
                return predicate;
            }
        });
        return count;
    }
}
