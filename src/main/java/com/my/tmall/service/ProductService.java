package com.my.tmall.service;

import com.my.tmall.pojo.Category;
import com.my.tmall.pojo.Product;

import java.util.List;

public interface ProductService {

    void add(Product product);

    void  delete(int id);

    void update(Product product);

    Product get(int id);

    List list(int cid);

    void setFirstProductImage(Product p);

    void fill(List<Category> categories);

    void fill(Category category);

    void fillByRow(List<Category> cs);

    void setSaleAndReviewNumber(Product product);

    void setSaleAndReviewNumber(List<Product> ps);
}
