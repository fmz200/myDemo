package com.soft.mydemo.bean.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SalesInfoBean implements Serializable {
    /**
     * 人员id
     */
    private String sales_id;

    /**
     * 曾用名(续期)
     */
    private String sales_name_once;

    /**
     * 既往服务公司(下拉框)
     */
    private String server_com_once;

    /**
     * 人员姓名
     */
    private String sales_name;

    /**
     * 证件类型
     */
    private String id_type;

    /**
     * 证件号码
     */
    private String id_no;

    /**
     * 性别
     */
    private String sex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 民族
     */
    private String nation;

    /**
     * 教育程度
     */
    private String education;

    /**
     * 政治面貌
     */
    private String political_stat;

    /**
     * 婚姻状况
     */
    private String marital_stat;

    /**
     * 省级籍贯
     */
    private String sales_native_2lvl;

    /**
     * 市级籍贯
     */
    private String sales_native_3lvl;

    /**
     * 户口所在地
     */
    private String domicile;

    /**
     * 住址
     */
    private String home_address;

    /**
     * 邮编
     */
    private String home_zipcode;

    /**
     * 地域属性
     */
    private String area_type;

    /**
     * 业务职级
     */
    private String rank;

    /**
     * 任职时间
     */
    private Date duty_date;

    /**
     * 签约时间
     */
    private Date probation_date;

    /**
     * 开始考核日期
     */
    private Date assess_start_date;

    /**
     * 单证抵押金
     */
    private Double deposit;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 固定电话
     */
    private String fixed_line;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 小灵通
     */
    private String phs;

    /**
     * 是否有犯罪记录
     */
    private String is_criminal_record;

    /**
     * 状态
     */
    private String stat;

    /**
     * 工作经验
     */
    private String work_experience;

    /**
     * 健康状况
     */
    private String health_stat;

    /**
     * 人员状况
     */
    private String sales_stat;

    /**
     * 是否全职
     */
    private String is_full_time;

    /**
     * 毕业院校
     */
    private String graduate_school;

    /**
     * 学位
     */
    private String degree;

    /**
     * 主修专业
     */
    private String major;

    /**
     * 技术职称
     */
    private String technical_title;

    /**
     * 专长
     */
    private String speciality;

    /**
     * 外语
     */
    private String foreign_language;

    /**
     * 外语级别
     */
    private String foreign_language_lvl;

    /**
     * 计算机级别
     */
    private String computer_lvl;

    /**
     * 原工作
     */
    private String old_job;

    /**
     * 原服务公司
     */
    private String old_company;

    /**
     * 工作时间
     */
    private Date work_date;

    /**
     * 职称
     */
    private String vocation_title;

    /**
     * 备注1
     */
    private String remark_1;

    /**
     * 备注2
     */
    private String remark_2;

    /**
     * 备注3
     */
    private String remark_3;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作时间
     */
    private Date insert_time;

    /**
     * 操作类型:1插入2修改3删除
     */
    private String operate_type;

    /**
     * 操作用户id
     */
    private String user_id;

    /**
     * 入司职级
     */
    private String enter_rank;

    /**
     * 既往服务公司名称(文本框)
     */
    private String server_com_once2;

    /**
     * 上岗日期
     */
    private Date mountguard_date;

    /**
     * 离司次数
     */
    private Integer dismiss_times;

    /**
     * 首次入司时间
     */
    private Date first_probation_date;

    /**
     * 是否城市项目
     */
    private String is_city_project;

    /**
     * 城市项目批次
     */
    private String city_project_no;

    /**
     * 四级机构代码
     */
    private String org_sales_code;

    /**
     * 手机号插入时间
     */
    private Date mobiledate;

    /**
     * 座机号插入时间
     */
    private Date fixedlinedate;

}
