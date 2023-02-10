package com.soft.mydemo.mapper;

import com.soft.mydemo.bean.user.SalesInfoBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SaleInfoMapper {

    SalesInfoBean querySalesInfoById(String saleId);

    List<SalesInfoBean> querySalesInfoList(SalesInfoBean salesInfoParams);
}
