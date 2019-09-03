package com.my.tmall.comparator;

import com.my.tmall.pojo.Product;

import java.util.Comparator;

public class ProductPriceConparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return (int) (o1.getPromotePrice()-o2.getPromotePrice());
    }
}
