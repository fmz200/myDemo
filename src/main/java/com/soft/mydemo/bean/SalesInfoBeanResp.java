/*
 * Copyright (c) 2021. fmz200 自用学习项目.
 */

package com.soft.mydemo.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SalesInfoBeanResp extends BaseResponse {

    public SalesInfoBean salesInfoBean;
}
