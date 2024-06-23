package com.gyhr.web.paas.notice.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.paas.notice.entity.Notice;

/**
 * 公告表(Notice)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-15 15:22:13
 */
public interface NoticeMapper extends BaseMapper<Notice>{
    IPage<Notice> getList(IPage<Notice> page, @Param("title") String title, @Param("uname") String uname);
}

