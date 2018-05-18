package com.ctl.test.mappers;

import com.ctl.test.model.SendCoupon;
import com.ctl.test.model.SendCouponExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SendCouponMapper {
    long countByExample(SendCouponExample example);

    int deleteByExample(SendCouponExample example);

    int insert(SendCoupon record);

    int insertSelective(SendCoupon record);

    List<SendCoupon> selectByExample(SendCouponExample example);

    int updateByExampleSelective(@Param("record") SendCoupon record, @Param("example") SendCouponExample example);

    int updateByExample(@Param("record") SendCoupon record, @Param("example") SendCouponExample example);
}