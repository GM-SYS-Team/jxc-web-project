package com.gms.controller;

import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.PushJob;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.PushJobService;
import com.gms.service.jxc.LogService;
import com.gms.service.jxc.ShopService;
import com.gms.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
public class PushJobController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PushJobController.class);

    @Resource
    private PushJobService pushJobService;

    @Autowired
    private ShopService shopService;

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

    @ResponseBody
    @RequestMapping("/listShop")
    @RequiresPermissions(value = {"推送管理"})
    public Map<String, Object> listShop(Shop shop, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows, HttpServletRequest request, @RequestParam(value = "userId", required = false) Integer userId)
            throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            shop.setUserId(userId);//优先考虑入参userid
            //获取当前用户，判断是管理员操作还是商铺操作
            User currentUser = getCurrentUser(request);
            if (currentUser.getUserType().equals(Constant.SHOPTYPE)) {//如果是商铺账户，只取当前账户的商铺列表
                shop.setUserId(currentUser.getId());
            }
            List<Shop> shopList = shopService.list(shop, page, rows, Sort.Direction.ASC, "id");
            Long total = shopService.getCount(shop);
            resultMap.put("rows", shopList);
            resultMap.put("total", total);
            logService.save(new Log(Log.SEARCH_ACTION, "查询商铺信息")); // 写入日志
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultMap.put("success", false);
            resultMap.put("errorInfo", e.getMessage());
        }
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
    public Map<String, Object> save(PushJob pushJob, String cg) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            if (StringUtils.isBlank(cg)) {
                throw new RuntimeException("请选择要推送的商铺");
            }
            pushJob.setObjectId(Integer.valueOf(cg));
            User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("currentUser");
            pushJob.setModifyUser(currentUser.getTrueName());
            if (pushJob.getId() != null) { // 写入日志
                pushJob.setCreateUser(currentUser.getTrueName());
                logService.save(new Log(Log.UPDATE_ACTION, "更新推送信息" + pushJob));
            } else {
                logService.save(new Log(Log.ADD_ACTION, "添加推送信息" + pushJob));
            }
            pushJobService.save(pushJob);
            resultMap.put("success", true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultMap.put("success", false);
            resultMap.put("errorInfo", e.getMessage());
        }

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
        Map<String, Object> resultMap = new HashMap<>();
        try {
            User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("currentUser");
            logService.save(new Log(Log.DELETE_ACTION, "删除推送信息" + pushJobService.findById(id)));  // 写入日志
            pushJobService.delete(id, currentUser.getTrueName());
            resultMap.put("success", true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultMap.put("success", false);
            resultMap.put("errorInfo", e.getMessage());
        }
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
        Map<String, Object> resultMap = new HashMap<>();
        try {
            User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("currentUser");
            logService.save(new Log(Log.DELETE_ACTION, "提交推送信息" + pushJobService.findById(id)));  // 写入日志
            pushJobService.submitPushJob(id, currentUser.getTrueName());
            resultMap.put("success", true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultMap.put("success", false);
            resultMap.put("errorInfo", e.getMessage());
        }
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
        Map<String, Object> resultMap = new HashMap<>();
        try {
            User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("currentUser");
            logService.save(new Log(Log.DELETE_ACTION, "撤回推送信息" + pushJobService.findById(id)));  // 写入日志
            pushJobService.withdrawPushJob(id, currentUser.getTrueName());
            resultMap.put("success", true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultMap.put("success", false);
            resultMap.put("errorInfo", e.getMessage());
        }
        return resultMap;
    }
}
