package com.my.tmall.controller;

import com.my.tmall.pojo.Product;
import com.my.tmall.pojo.PropertyValue;
import com.my.tmall.service.ProductService;
import com.my.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyValueController {
    @Autowired
    ProductService productService;
    @Autowired
    PropertyValueService propertyValueService;

    @RequestMapping("admin_propertyValue_edit")
    public String edit(Model model,int pid){
        Product product=productService.get(pid);
        propertyValueService.init(product);
        List<PropertyValue> pvs=propertyValueService.list(product.getId());

        model.addAttribute("p",product);
        model.addAttribute("pvs",pvs);
        return "admin/editPropertyValue";
    }

    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue pv){
        propertyValueService.update(pv);
        return "success";
    }
}
