package com.gyhr.web.paas.repair.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.paas.repair.entity.Repair;
import com.gyhr.web.paas.repair.entity.RepairParm;
import com.gyhr.web.paas.repair.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

/**
 * (paas_repair)表控制层
 *
 * @author makejava
 * @since 2024-02-14 16:31:44
 */
@RestController
@RequestMapping("/api/repair")
public class RepairController {

    @Autowired
    private RepairService repairService;

    /**
     * 获取待处理维修总数
     * @param userId
     * @return
     */
    @GetMapping("/doRepairCount")
    public ResultVo<Object> doRepairCount(@RequestParam("userId") Long userId) {
        QueryWrapper<Repair> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Repair::getUserId, userId)
                .eq(Repair::getStatus, "1");
        int count = repairService.count(queryWrapper);

        return ResultUtils.success("查询成功",count);
    }

    /**
     * 获取待派修维修总数
     * @return
     */
    @GetMapping("/repairCount")
    public ResultVo<Object> repairCount() {
        QueryWrapper<Repair> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Repair::getStatus, "0");
        int count = repairService.count(queryWrapper);

        return ResultUtils.success("查询成功",count);
    }

    /**
     * 维修列表
     * @param parm
     * @return
     */
    @GetMapping("/repairlist")
    public ResultVo<Object> repairlist(RepairParm parm) {
        IPage<Repair> page = new Page<>();
        page.setSize(page.getSize());
        page.setCurrent(page.getCurrent());
        IPage<Repair> list = repairService.getList(page,
                parm.getRepairTitle(), parm.getRepairContent(),
                parm.getCname(), parm.getPhone(), parm.getCustomerId(), parm.getUserId());

        return ResultUtils.success("查询成功", list);
    }

    /**
     * 新增维修
     * @param repair
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:myrepair:add')")
    public ResultVo<Object> addRepair(@RequestBody Repair repair) {
        repair.setCreateTime(new Date());
        repair.setStatus("0");
        boolean save = repairService.save(repair);
        if (save) {
            return ResultUtils.success("报修成功!");
        }

        return ResultUtils.error("报修失败!");
    }

    /**
     * 编辑
     * @param repair
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:myrepair:edit')")
    public ResultVo<Object> editRepair(@RequestBody Repair repair) {
        if (repair.getImgurl() == "") repair.setImgurl(null);
        boolean update = repairService.updateById(repair);
        if (update) {
            return ResultUtils.success("编辑成功!");
        }

        return ResultUtils.error("编辑失败!");
    }

    /**
     * 删除
     * @param repairId
     * @return
     */
    @DeleteMapping("/{repairId}")
    @PreAuthorize("hasAuthority('wy:myrepair:delete')")
    public ResultVo<Object> deleteRepair(@PathVariable("repairId") Long repairId) {
        boolean remove = repairService.removeById(repairId);
        if (remove) {
            return ResultUtils.success("删除成功!");
        }

        return ResultUtils.error("删除失败!");
    }

    /**
     * 派修
     * @param repair
     * @return
     */
    @PostMapping("/assignRepair")
    @PreAuthorize("hasAuthority('wy:repair:assign')")
    public ResultVo<Object> assignRepair(@RequestBody Repair repair){
        repair.setStatus("1");
        boolean update = repairService.updateById(repair);
        if (update) {
            return ResultUtils.success("派修成功!");
        }

        return ResultUtils.error("派修失败!");
    }

    /**
     * 处理
     * @param repair
     * @return
     */
    @PostMapping("/doRepair")
    @PreAuthorize("hasAuthority('wy:dorepair:do')")
    public ResultVo<Object> doRepair(@RequestBody Repair repair) {
        repair.setStatus("2");
        boolean update = repairService.updateById(repair);
        if (update) {
            return ResultUtils.success("处理成功!");
        }

        return ResultUtils.error("处理失败!");
    }
}

