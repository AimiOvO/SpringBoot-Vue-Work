package com.gyhr.web.paas.complaint.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.paas.complaint.entity.Complaint;
import com.gyhr.web.paas.complaint.entity.ComplaintParm;
import com.gyhr.web.paas.complaint.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

/**
 * 投诉表(paas_complaint)表控制层
 *
 * @author makejava
 * @since 2024-02-14 15:16:34
 */
@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {
    @Autowired
    private ComplaintService complaintService;

    /**
     * 待处理投诉总数
     * @return
     */
    @GetMapping("/complaintCount")
    public ResultVo<Object> complaintCount() {
        QueryWrapper<Complaint> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Complaint::getStatus, "0");
        int count = complaintService.count(queryWrapper);

        return ResultUtils.success("查询成功",count);
    }

    /**
     * 投诉列表
     * @param parm
     * @return
     */
    @GetMapping("/complaintList")
    public ResultVo<Object> complaintList(ComplaintParm parm) {
        // 构造查询条件
        IPage<Complaint> page = new Page<>();
        page.setCurrent(page.getCurrent());
        page.setSize(page.getSize());
        IPage<Complaint> list = complaintService.getList(page, parm.getTitle(), parm.getContent(),
                parm.getCname(), parm.getCphone(), parm.getCustomerId());

        return ResultUtils.success("查询成功", list);
    }

    /**
     * 新增投诉
     * @param complaint
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:mycomplaint:add')")
    public ResultVo<Object> addComplaint(@RequestBody Complaint complaint) {
        // 设置投诉的状态
        complaint.setStatus("0");
        // 设置投诉时间
        complaint.setCreateTime(new Date());
        // 投诉保存
        boolean save = complaintService.save(complaint);
        if (save) {
            return ResultUtils.success("投诉成功!");
        }

        return ResultUtils.error("投诉失败!");
    }

    /**
     * 编辑投诉
     * @param complaint
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:mycomplaint:edit')")
    public ResultVo<Object> editComplaint(@RequestBody Complaint complaint) {
        // 投诉保存
        boolean update = complaintService.updateById(complaint);
        if (update) {
            return ResultUtils.success("编辑成功!");
        }

        return ResultUtils.error("编辑失败!");
    }

    /**
     * 删除投诉
     * @param complaintId
     * @return
     */
    @DeleteMapping("/{complaintId}")
    @PreAuthorize("hasAuthority('wy:mycomplaint:delete')")
    public ResultVo<Object> deletComplaint(@PathVariable("complaintId") Long complaintId) {
        boolean remove = complaintService.removeById(complaintId);
        if (remove) {
            return ResultUtils.success("删除成功!");
        }

        return ResultUtils.error("删除失败!");
    }

    /**
     * 处理投诉
     * @param complaint
     * @return
     */
    @PostMapping("/doComplaint")
    @PreAuthorize("hasAuthority('wy:complaint:do')")
    public ResultVo<Object> doComplaint(@RequestBody Complaint complaint) {
        // 投诉保存
        complaint.setStatus("1");
        boolean update = complaintService.updateById(complaint);
        if (update) {
            return ResultUtils.success("处理成功!");
        }

        return ResultUtils.error("处理失败!");
    }
}

