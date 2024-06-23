package com.gyhr.web.paas.notice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.paas.notice.entity.Notice;

/**
 * 公告表(paas_notice)表服务接口
 *
 * @author makejava
 * @since 2024-02-15 15:22:13
 */
public interface NoticeService extends IService<Notice>{
    IPage<Notice> getList(IPage<Notice> page, String title, String uname);
}
