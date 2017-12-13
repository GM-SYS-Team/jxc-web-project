package com.gms.controller;

import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.PushJob;
import com.gms.service.jxc.LogService;
import com.gms.service.jxc.PushJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuchuantong
 * @version V1.0
 * @Title: todo
 * @Package com.gms.controller
 * @Description: todo
 * @date 2017/11/19 21:55
 */
@RestController
@RequestMapping("/admin/push")
public class PushJobController {

    @Resource
    private PushJobService pushJobService;

    @Resource
    private LogService logService;

    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions(value = {"推送管理"})
    public Map<String, Object> list(PushJob pushJob, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) throws Exception {
        List<PushJob> pushJobList = pushJobService.list(pushJob, page, rows, Sort.Direction.ASC, "id");
        Long total = pushJobService.getCount(pushJob);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", pushJobList);
        resultMap.put("total", total);
        logService.save(new Log(Log.SEARCH_ACTION, "查询推送信息")); // 写入日志
        return resultMap;
    }

    /**
     * 添加或者修改推送信息
     *
     * @param pushJob 推送信息
     * @return 提示信息
     * @throws Exception 异常信息
     */
    @RequestMapping("/save")
    @RequiresPermissions(value = {"推送管理"})
    public Map<String, Object> save(PushJob pushJob) throws Exception {
        if (pushJob.getId() != null) { // 写入日志
            logService.save(new Log(Log.UPDATE_ACTION, "更新推送信息" + pushJob));
        } else {
            logService.save(new Log(Log.ADD_ACTION, "添加推送信息" + pushJob));
        }
        Map<String, Object> resultMap = new HashMap<>();
        pushJobService.save(pushJob);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 删除角色信息
     *
     * @param id 推送ID
     * @return 提示信息
     * @throws Exception 异常信息
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"推送管理"})
    public Map<String, Object> delete(Long id) throws Exception {
        logService.save(new Log(Log.DELETE_ACTION, "删除推送信息" + pushJobService.findById(id)));  // 写入日志
        Map<String, Object> resultMap = new HashMap<>();
        pushJobService.delete(id);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 删除角色信息
     *
     * @param id 推送ID
     * @return 提示信息
     * @throws Exception 异常信息
     */
    @RequestMapping("/submit")
    @RequiresPermissions(value = {"推送管理"})
    public Map<String, Object> submit(Long id) throws Exception {
        logService.save(new Log(Log.DELETE_ACTION, "提交推送信息" + pushJobService.findById(id)));  // 写入日志
        Map<String, Object> resultMap = new HashMap<>();
        pushJobService.submitPushJob(id);
        resultMap.put("success", true);
        return resultMap;
    }


    /**
     * 删除角色信息
     *
     * @param id 推送ID
     * @return 提示信息
     * @throws Exception 异常信息
     */
    @RequestMapping("/withdraw")
    @RequiresPermissions(value = {"推送管理"})
    public Map<String, Object> withdraw(Long id) throws Exception {
        logService.save(new Log(Log.DELETE_ACTION, "撤回推送信息" + pushJobService.findById(id)));  // 写入日志
        Map<String, Object> resultMap = new HashMap<>();
        pushJobService.withdrawPushJob(id);
        resultMap.put("success", true);
        return resultMap;
    }
}
