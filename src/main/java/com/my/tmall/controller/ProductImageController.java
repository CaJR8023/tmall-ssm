package com.my.tmall.controller;

import com.my.tmall.pojo.Product;
import com.my.tmall.pojo.ProductImage;
import com.my.tmall.service.ProductImageService;
import com.my.tmall.service.ProductService;
import com.my.tmall.util.ImageUtil;
import com.my.tmall.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("")
public class ProductImageController {
    @Autowired
    ProductImageService productImageService;

    @Autowired
    ProductService productService;

    @RequestMapping("admin_productImage_add")
    public String add(ProductImage productImage, HttpSession session, UploadedImageFile uploadedImageFile){//1. 通过pi对象接受type和pid的注入
        productImageService.add(productImage);//2. 借助productImageService，向数据库中插入数据。
        String fileName=productImage.getId()+".jpg";
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;
        if(ProductImageService.type_single.equals(productImage.getType())){
            imageFolder=session.getServletContext().getRealPath("img/productSingle");//3. 根据session().getServletContext().getRealPath( "img/productSingle")，定位到存放单个产品图片的目录
            imageFolder_small=session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle=session.getServletContext().getRealPath("img/productSingle_middle");
        }else{
            imageFolder=session.getServletContext().getRealPath("img/productDetail");
        }

        File file=new File(imageFolder,fileName);
        file.getParentFile().mkdirs();
        try {
            uploadedImageFile.getImage().transferTo(file);//5. 通过uploadedImageFile保存文件
            BufferedImage img= ImageUtil.change2jpg(file);//6. 借助ImageUtil.change2jpg()方法把格式真正转化为jpg，而不仅仅是后缀名为.jpg
            ImageIO.write(img,"jpg",file);

            if(ProductImageService.type_single.equals(productImage.getType())){
                File file_small=new File(imageFolder_small,fileName);
                File file_middle=new File(imageFolder_middle,fileName);

                ImageUtil.resizeImage(file,56,56,file_small);//7. 再借助ImageUtil.resizeImage把正常大小的图片，改变大小之后，分别复制到productSingle_middle和productSingle_small目录下。
                ImageUtil.resizeImage(file,217,190,file_middle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:admin_productImage_list?pid="+productImage.getPid();//8. 处理完毕之后，客户端条跳转到admin_productImage_list?pid=，并带上pid。
    }

    @RequestMapping("admin_productImage_delete")
    public String delete(int id,HttpSession session){
        ProductImage productImage=productImageService.get(id);

        String fileName=productImage.getId()+"jpg";
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;
        if (ProductImageService.type_single.equals(productImage.getType())){
            imageFolder=session.getServletContext().getRealPath("img/productSingle");
            imageFolder_small=session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle=session.getServletContext().getRealPath("img/productSingle_middle");
            File imageFile=new File(imageFolder,fileName);
            File file_small=new File(imageFolder_small,fileName);
            File file_middle=new File(imageFolder_middle,fileName);
            imageFile.delete();
            file_small.delete();
            file_middle.delete();
        }else{
            imageFolder=session.getServletContext().getRealPath("img/productDetail");
            File imageFile=new File(imageFolder,fileName);
            imageFile.delete();
        }
        productImageService.delete(id);
        return "redirect:admin_productImage_list?pid="+productImage.getPid();
    }

    @RequestMapping("admin_productImage_list")
    public String list(int pid, Model model){
        Product product=productService.get(pid);//1.根据pid获取Product对象
        List<ProductImage> pisSingle=productImageService.list(pid,ProductImageService.type_single);//2.根据pid对象获取单个图片的集合pisSingle
        List<ProductImage> pisDetail=productImageService.list(pid,ProductImageService.type_detail);//3.根据pid对象获取详情图片的集合pisDetail

        //4.把product 对象，pisSingle ，pisDetail放在model上
        model.addAttribute("p",product);
        model.addAttribute("pisSingle",pisSingle);
        model.addAttribute("pisDetail",pisDetail);

        return "admin/listProductImage";
    }
}
