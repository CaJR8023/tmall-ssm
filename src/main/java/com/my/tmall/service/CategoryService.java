package com.my.tmall.service;

import com.my.tmall.pojo.Category;
import com.my.tmall.util.Page;

import java.util.List;

public interface CategoryService {

    List<Category> list();

    void add(Category category);

    void delete(int id);

    Category get(int id);

    void update(Category category);
}
