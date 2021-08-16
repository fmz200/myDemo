package com.soft.mydemo.mapper;

import com.soft.mydemo.bean.CategoryInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by fmz200 on 2021/08/07.
 */
@Mapper
public interface CategoryMapper {
    List<CategoryInfoBean> getAllCategories();

    int deleteCategoryByIds(@Param("ids") String[] ids);

    int updateCategoryById(CategoryInfoBean category);

    int addCategory(CategoryInfoBean category);
}
