package com.my.tmall.service.impl;

import com.my.tmall.mapper.OrderItemMapper;
import com.my.tmall.pojo.Order;
import com.my.tmall.pojo.OrderItem;
import com.my.tmall.pojo.OrderItemExample;
import com.my.tmall.pojo.Product;
import com.my.tmall.service.OrderItemService;
import com.my.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    ProductService productService;
    @Autowired
    OrderItemMapper orderItemMapper;


    @Override
    public void add(OrderItem c) {
        orderItemMapper.insert(c);
    }

    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem c) {
        orderItemMapper.updateByPrimaryKeySelective(c);
    }

    @Override
    public OrderItem get(int id) {
        OrderItem result=orderItemMapper.selectByPrimaryKey(id);
        setProduct(result);
        return result;
    }

    @Override
    public void fill(List<Order> os) {
        for (Order o:os){
            fill(o);
        }
    }

    @Override
    public void fill(Order o) {
        OrderItemExample example=new OrderItemExample();
        example.createCriteria().andOidEqualTo(o.getId());
        example.setOrderByClause("id desc");
        List<OrderItem> ois=orderItemMapper.selectByExample(example);
        setProduct(ois);

        float total=0;
        int totalNumber=0;
        for (OrderItem oi:ois){
            total+=oi.getNumber()*oi.getProduct().getPromotePrice();
            totalNumber+=oi.getNumber();
        }
        o.setTotal(total);
        o.setTotalNumber(totalNumber);
        o.setOrderItems(ois);
    }

    @Override
    public int getSaleCount(int pid) {
        OrderItemExample example=new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid);
        List<OrderItem> ois=orderItemMapper.selectByExample(example);
        int result=0;
        for (OrderItem oi:ois){
            result+=oi.getNumber();
        }
        return result;
    }

    @Override
    public List<OrderItem> listByUser(int uid) {
        OrderItemExample example=new OrderItemExample();
        example.createCriteria().andUidEqualTo(uid).andOidIsNull();
        List<OrderItem> result=orderItemMapper.selectByExample(example);
        setProduct(result);
        return result;
    }

    public void setProduct(List<OrderItem> ois){
        for (OrderItem oi:ois){
            setProduct(oi);
        }
    }

    public void setProduct(OrderItem oi){
        Product p=productService.get(oi.getPid());
        productService.setFirstProductImage(p);
        oi.setProduct(p);
    }
}
