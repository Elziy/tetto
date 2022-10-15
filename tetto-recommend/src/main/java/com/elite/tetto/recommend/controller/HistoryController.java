package com.elite.tetto.recommend.controller;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.recommend.entity.HistoryEntity;
import com.elite.tetto.recommend.service.HistoryService;
import com.elite.tetto.recommend.util.SecurityUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-15 21:19:41
 */
@RestController
@RequestMapping("recommend/history")
public class HistoryController {
    @Resource
    private HistoryService historyService;
    
    // 添加浏览历史
    @PostMapping("/{aid}")
    public R addHistory(@PathVariable("aid") Long aid) {
        Long uid = SecurityUtil.getLoginUserId();
        boolean h = historyService.addHistory(uid, aid);
        System.out.println(h);
        return R.ok();
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("recommend:history:list")
    public R list(@RequestParam Map<String, Object> params) {
        
        PageUtils page = historyService.queryPage(params);
        
        return R.ok().put("page", page);
    }
    
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("recommend:history:info")
    public R info(@PathVariable("id") Long id) {
        HistoryEntity history = historyService.getById(id);
        
        return R.ok().put("history", history);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("recommend:history:save")
    public R save(@RequestBody HistoryEntity history) {
        historyService.save(history);
        
        return R.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("recommend:history:update")
    public R update(@RequestBody HistoryEntity history) {
        historyService.updateById(history);
        
        return R.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("recommend:history:delete")
    public R delete(@RequestBody Long[] ids) {
        historyService.removeByIds(Arrays.asList(ids));
        
        return R.ok();
    }
    
}
