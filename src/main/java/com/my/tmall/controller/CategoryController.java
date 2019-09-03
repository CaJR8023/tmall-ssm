package com.my.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.my.tmall.pojo.Category;
import com.my.tmall.service.CategoryService;
import com.my.tmall.util.ImageUtil;
import com.my.tmall.util.Page;
import com.my.tmall.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("/admin_category_list")
    public String list(Model model, Page page){
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Category> cs=categoryService.list();
        int total= (int) new PageInfo<>(cs).getTotal();
        page.setTotal(total);
        model.addAttribute("cs",cs);
        model.addAttribute("page",page);
        return "admin/listCategory";
    }

    @RequestMapping("/admin_category_add")
    public String add(Category category, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
    categoryService.add(category);
        File imageFolder=new File(session.getServletContext().getRealPath("img/category"));
        File file=new File(imageFolder,category.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        uploadedImageFile.getImage().transferTo(file);
        BufferedImage img= ImageUtil.change2jpg(file);
        ImageIO.write(img,"jpg",file);
        return "redirect:/admin_category_list";
    }

    @RequestMapping("/admin_category_delete")
    public String delete(int id,HttpSession session) throws  IOException{
        categoryService.delete(id);

        File imageFolder=new File(session.getServletContext().getRealPath("img/category"));
        File file=new File(imageFolder,id+".jpg");
        file.delete();

        return "redirect:/admin_category_list";
    }

    @RequestMapping("/admin_category_edit")
    public String edit(int id,Model model)throws  IOException{
        Category category=categoryService.get(id);
        model.addAttribute("c",category);
        return "admin/editCategory";
    }

    @RequestMapping("admin_category_update")
    public String update(Category category,HttpSession session,UploadedImageFile uploadedImageFile)throws IOException{
        categoryService.update(category);
        MultipartFile image=uploadedImageFile.getImage();
        if(null!=image && !image.isEmpty()){
            File imageFolder=new File(session.getServletContext().getRealPath("img/category"));
            File file=new File(imageFolder, category.getId()+".jpg");
            image.transferTo(file);
            BufferedImage img=ImageUtil.change2jpg(file);
            ImageIO.write(img,"jpg",file);
        }
        return "redirect:/admin_category_list";
    }
}
