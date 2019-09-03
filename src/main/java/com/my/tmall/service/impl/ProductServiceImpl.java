package com.my.tmall.service.impl;

import com.my.tmall.mapper.ProductMapper;
import com.my.tmall.pojo.Category;
import com.my.tmall.pojo.Product;
import com.my.tmall.pojo.ProductExample;
import com.my.tmall.pojo.ProductImage;
import com.my.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;

    @Override
    public void add(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void delete(int id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product product) {
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public Product get(int id) {
        Product product=productMapper.selectByPrimaryKey(id);
        setCategory(product);
        setFirstProductImage(product);
        return product;
    }

    public void setCategory(List<Product> ps){
        for(Product product:ps)
            setCategory(product);
    }
    public void setCategory(Product product){
        int cid= product.getCid();
        Category category=categoryService.get(cid);
        product.setCategory(category);
    }

    @Override
    public List list(int cid) {
        ProductExample example=new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        List result=productMapper.selectByExample(example);
        setCategory(result);
        setFirstProductImage(result);
        return result;
    }

    @Override
    public void setFirstProductImage(Product p) {
        List<ProductImage> pis = productImageService.list(p.getId(), ProductImageService.type_single);
        if (!pis.isEmpty()) {
            ProductImage pi = pis.get(0);
            p.setFirstProductImage(pi);
        }
    }

    @Override
    public void fill(List<Category> categories) {
        for (Category c:categories){
            fill(c);
        }
    }

    @Override
    public void fill(Category category) {
        List<Product> ps=list(category.getId());
        category.setProducts(ps);
    }

    @Override
    public void fillByRow(List<Category> cs) {
        int productNumberEachRow=8;
        for (Category c:cs){
            List<Product> products=c.getProducts();
            List<List<Product>> productsByRow=new ArrayList<>();
            for (int i=0;i<products.size();i+=productNumberEachRow){
                int size=i+productNumberEachRow;
                size=size>products.size()?products.size():size;
                List<Product> productsOfEachRow=products.subList(i,size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
    }

    @Override
    public void setSaleAndReviewNumber(Product product) {
        int saleCount=orderItemService.getSaleCount(product.getId());
        product.setSaleCount(saleCount);

        int reviewCount=reviewService.getCount(product.getId());
        product.setReviewCount(reviewCount);
    }

    @Override
    public void setSaleAndReviewNumber(List<Product> ps) {
        for (Product p:ps){
            setSaleAndReviewNumber(p);
        }
    }

    public void setFirstProductImage(List<Product> ps) {
        for (Product p : ps) {
            setFirstProductImage(p);
        }
    }
}
