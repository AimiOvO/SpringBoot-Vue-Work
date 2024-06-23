package com.gyhr.web.paas.complaint.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gyhr.web.paas.complaint.entity.Complaint;


/**
 * 投诉表(Complaint)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-14 15:16:33
 */
public interface ComplaintMapper extends BaseMapper<Complaint>{
    IPage<Complaint> getList(IPage<Complaint> page,
        @Param("title") String title, @Param("content") String content,
        @Param("cname") String cname, @Param("cphone") String cphone, @Param("customerId") Long customerId);
}

