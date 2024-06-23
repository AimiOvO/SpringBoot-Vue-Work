package com.gyhr.web.paas.complaint.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyhr.web.paas.complaint.entity.Complaint;
import com.gyhr.web.paas.complaint.mapper.ComplaintMapper;
import com.gyhr.web.paas.complaint.service.ComplaintService;
import org.springframework.stereotype.Service;

/**
 * 投诉表(paas_complaint)表服务实现类
 *
 * @author makejava
 * @since 2024-02-14 15:16:34
 */
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements ComplaintService {

    @Override
    public IPage<Complaint> getList(IPage<Complaint> page, String title, String content, String cname, String cphone, Long customerId) {
        return this.baseMapper.getList(page, title, content, cname, cphone , customerId);
    }
}

