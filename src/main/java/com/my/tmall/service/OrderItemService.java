package com.my.tmall.service;

import com.my.tmall.pojo.Order;
import com.my.tmall.pojo.OrderItem;

import java.util.List;

public interface OrderItemService {

    void add(OrderItem c);

    void delete(int id);

    void update(OrderItem c);

    OrderItem get(int id);

    void fill(List<Order> os);

    void fill(Order o);

    int getSaleCount(int pid);

    List<OrderItem> listByUser(int uid);
}
