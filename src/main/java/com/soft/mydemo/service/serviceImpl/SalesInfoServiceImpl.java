package com.soft.mydemo.service.serviceImpl;

import com.soft.mydemo.bean.SalesInfoBean;
import com.soft.mydemo.bean.SalesInfoBeanResp;
import com.soft.mydemo.mapper.SaleInfoMapper;
import com.soft.mydemo.service.SalesInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesInfoServiceImpl implements SalesInfoService {

    @Autowired
    private SaleInfoMapper saleInfoMapper;

    @Override
    public SalesInfoBeanResp querySalesInfo(String saleId) {
        SalesInfoBeanResp salesInfoBeanResp = new SalesInfoBeanResp();
        try {
            SalesInfoBean salesInfoBean = saleInfoMapper.selectSales(saleId);
            salesInfoBeanResp.setSalesInfoBean(salesInfoBean);
        } catch (Exception e) {
            e.printStackTrace();
            salesInfoBeanResp.setCode("-1");
            salesInfoBeanResp.setMessage("查询失败！" + e);
        }
        return salesInfoBeanResp;
    }
}
