package com.my.tmall.service;

import com.my.tmall.pojo.Review;

import java.util.List;

public interface ReviewService {

    void add(Review c);

    void delete(int id);

    void update(Review review);

    Review get(int id);

    List list(int pid);

    int getCount(int pid);
}
