package com.soft.mydemo.controller;

import com.soft.mydemo.bean.CategoryInfoBean;
import com.soft.mydemo.bean.RespBean;
import com.soft.mydemo.common.CommonConstants;
import com.soft.mydemo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 超级管理员专属Controller
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<CategoryInfoBean> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public RespBean deleteById(@PathVariable String ids) {
        boolean result = categoryService.deleteCategoryByIds(ids);
        if (result) {
            return new RespBean(CommonConstants.SUCCESS, "删除成功!");
        }
        return new RespBean(CommonConstants.ERROR, "删除失败!");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RespBean addNewCate(CategoryInfoBean category) {

        if ("".equals(category.getCateName()) || category.getCateName() == null) {
            return new RespBean(CommonConstants.ERROR, "请输入栏目名称!");
        }

        int result = categoryService.addCategory(category);

        if (result == 1) {
            return new RespBean(CommonConstants.SUCCESS, "添加成功!");
        }
        return new RespBean(CommonConstants.ERROR, "添加失败!");
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public RespBean updateCate(CategoryInfoBean category) {
        int i = categoryService.updateCategoryById(category);
        if (i == 1) {
            return new RespBean(CommonConstants.SUCCESS, "修改成功!");
        }
        return new RespBean(CommonConstants.ERROR, "修改失败!");
    }
}
