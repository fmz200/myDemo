package com.soft.mydemo.mapper;

import com.soft.mydemo.bean.SalesInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Value;

@Mapper
public interface SaleInfoMapper {

    SalesInfoBean selectSales(@Value("saleId") String saleId);
}
