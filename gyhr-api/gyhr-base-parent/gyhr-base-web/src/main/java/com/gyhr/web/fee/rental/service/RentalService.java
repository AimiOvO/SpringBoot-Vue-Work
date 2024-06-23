package com.gyhr.web.fee.rental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyhr.web.fee.rental.entity.Rental;

/**
 * 租费表(fee_rental)表服务接口
 *
 * @author makejava
 * @since 2024-02-12 22:10:04
 */
public interface RentalService extends IService<Rental>{
    boolean saveFeeRental(Rental rental);
    boolean updateFeeRental(Rental rental);

    IPage<Rental> getList(IPage<Rental> page, String cname,
                         String homeNum, String payStatus, Long customerId);
}
