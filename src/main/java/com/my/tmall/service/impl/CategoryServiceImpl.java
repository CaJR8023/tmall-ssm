package com.my.tmall.service.impl;

import com.my.tmall.mapper.CategoryMapper;
import com.my.tmall.pojo.Category;
import com.my.tmall.pojo.CategoryExample;
import com.my.tmall.service.CategoryService;
import com.my.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;



    public List<Category> list() {
        //按照这种写法，传递一个example对象，这个对象指定按照id倒排序来查询
        CategoryExample example=new CategoryExample();
        example.setOrderByClause("id desc");
        return categoryMapper.selectByExample(example);
    }

    public void add(Category category) { categoryMapper.insert(category);}

    @Override
    public void delete(int id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Category get(int id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }
}
