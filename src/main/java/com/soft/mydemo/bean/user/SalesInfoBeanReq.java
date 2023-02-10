package com.soft.mydemo.bean.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesInfoBeanReq extends SalesInfoBean implements Serializable {

    public String sale_id;

    public String stu_name;

    private Integer pageNum;

    private Integer pageSize;

    public String birthdayStart;

    public String birthdayEnd;


}
