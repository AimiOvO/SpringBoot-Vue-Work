package com.gyhr.web.paas.notice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.paas.notice.entity.Notice;
import com.gyhr.web.paas.notice.entity.NoticeParm;
import com.gyhr.web.paas.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 公告表(paas_notice)表控制层
 *
 * @author makejava
 * @since 2024-02-15 15:22:14
 */
@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    /**
     * 公告列表
     * @param parm
     * @return
     */
    @GetMapping("/noticelist")
    public ResultVo<Object> noticelist(NoticeParm parm) {
        //构造分页对象
        IPage<Notice> page = new Page<>();
        page.setCurrent(parm.getCurrentPage());
        page.setSize(parm.getPageSize());
        IPage<Notice> list = noticeService.getList(page, parm.getTitle(), parm.getUname());

        return ResultUtils.success("查询成功",list);
    }

    /**
     * 新增
     * @param notice
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('wy:notice:add')")
    public ResultVo<Object> addNotice(@RequestBody Notice notice) {
        notice.setCreateTime(new Date());
        boolean save = noticeService.save(notice);
        if(save){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    /**
     * 编辑
     * @param notice
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('wy:notice:edit')")
    public ResultVo<Object> editNotice(@RequestBody Notice notice) {
        boolean update = noticeService.updateById(notice);
        if(update){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    /**
     * 删除
     * @param noticeId
     * @return
     */
    @DeleteMapping("/{noticeId}")
    @PreAuthorize("hasAuthority('wy:notice:delete')")
    public ResultVo<Object> deleteNotice(@PathVariable("noticeId") Long noticeId) {
        boolean remove = noticeService.removeById(noticeId);
        if(remove){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

}

