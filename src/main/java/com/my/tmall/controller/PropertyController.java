package com.my.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.my.tmall.pojo.Category;
import com.my.tmall.pojo.Property;
import com.my.tmall.service.CategoryService;
import com.my.tmall.service.PropertyService;
import com.my.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_property_add")
    public String add(Model model, Property property){
        propertyService.add(property);
        return "redirect:admin_property_list?cid="+property.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id){
        Property property=propertyService.get(id);
        propertyService.delete(id);
        return "redirect:admin_property_list?cid="+property.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String edit(Model model,int id){
        Property property=propertyService.get(id);
        Category category=categoryService.get(property.getCid());
        property.setCategory(category);
        model.addAttribute("p",property);
        return "admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property property){
        propertyService.update(property);
        return "redirect:admin_property_list?cid="+property.getCid();
    }

    @RequestMapping("admin_property_list")
    public String list(Model model, int cid, Page page){
        Category category=categoryService.get(cid);

        PageHelper.offsetPage(page.getStart(),page.getCount());//通过PageHelper设置分页参数
        List<Property> ps=propertyService.list(cid);//基于cid，获取当前分类下的属性集合

        int total= (int) new PageInfo<>(ps).getTotal();//通过PageInfo获取属性总数
        page.setTotal(total);//把总数设置给分页page对象
        page.setParam("&cid="+category.getId());//拼接字符串"&cid="+c.getId()，设置给page对象的Param值。 因为属性分页都是基于当前分类下的分页，所以分页的时候需要传递这个cid。

        model.addAttribute("ps",ps);
        model.addAttribute("c",category);
        model.addAttribute("page",page);

        return  "admin/listProperty";
    }
}
