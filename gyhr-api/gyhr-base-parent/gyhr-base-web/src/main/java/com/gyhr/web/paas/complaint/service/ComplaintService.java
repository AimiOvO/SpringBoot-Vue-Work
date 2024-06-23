package com.gyhr.web.paas.complaint.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.paas.complaint.entity.Complaint;

/**
 * 投诉表(paas_complaint)表服务接口
 *
 * @author makejava
 * @since 2024-02-14 15:16:33
 */
public interface ComplaintService extends IService<Complaint>{
    IPage<Complaint> getList(IPage<Complaint> page,
                             String title, String content,
                             String cname, String cphone, Long customerId);
}
