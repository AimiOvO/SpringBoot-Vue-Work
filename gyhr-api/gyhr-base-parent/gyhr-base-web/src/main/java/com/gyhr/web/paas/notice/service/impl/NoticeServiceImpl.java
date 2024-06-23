package com.gyhr.web.paas.notice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.paas.notice.entity.Notice;
import com.gyhr.web.paas.notice.mapper.NoticeMapper;
import com.gyhr.web.paas.notice.service.NoticeService;
import org.springframework.stereotype.Service;

/**
 * 公告表(paas_notice)表服务实现类
 *
 * @author makejava
 * @since 2024-02-15 15:22:13
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public IPage<Notice> getList(IPage<Notice> page, String title, String uname) {
        return this.baseMapper.getList(page, title, uname);
    }
}

