package com.my.tmall.service;

import com.my.tmall.pojo.Product;
import com.my.tmall.pojo.PropertyValue;

import java.util.List;

public interface PropertyValueService {

    void init(Product product);//初始化PropertyValue

    void update(PropertyValue propertyValue);

    PropertyValue get(int ptid,int pid);

    List<PropertyValue> list(int pid);
}
