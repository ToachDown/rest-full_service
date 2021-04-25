package com.Derect.join.controllers;

import com.Derect.join.entity.User;
import com.Derect.join.entity.Product;
import com.Derect.join.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public String list(@RequestParam(required = false, defaultValue = "") String filter,
                       @AuthenticationPrincipal User user,
                       Model model
    ){
        Iterable<Product> prod = productService.checkFilter(filter);
        model.addAttribute("isAuthorized", user);
        model.addAttribute("prod", prod);
        return "homes";
    }

    @PostMapping()
    public String add(
            @AuthenticationPrincipal User seller,
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("file") MultipartFile file
    ) throws IOException{
        Product product = new Product(name, price, seller);
        productService.saveFile(product, file);
        productService.saveProduct(product);
        return "redirect:/product";
    }

    @GetMapping("/update")
    public String up(@RequestParam("id") Long id,
                     @AuthenticationPrincipal User user,
                     Model model
    ){
        Product prod = productService.findProductById(id);
        model.addAttribute("isAuthorized", user);
        model.addAttribute("prod", prod);
        return "updateMessage";
    }

    @PostMapping("/update")
    public String update(@Valid @RequestParam("name") String name,
                         @RequestParam("id") Long id,
                         @RequestParam(value = "price", required = false) int price,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         Model model
    ) throws  IOException{
        Product prod = productService.findProductById(id);
        Product updateProd = productService.checkUpdate(prod, name, price, file);
        productService.saveProduct(updateProd);
        return "redirect:/product";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id,
                         @AuthenticationPrincipal User user,
                         Model model){
        productService.deleteProduct(id);
        model.addAttribute("isAuthorized", user);
        return "redirect:/product/page/" + user.getId();
    }

    @GetMapping("/page/{id}")
    public String mainPage(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal User user){
        Iterable<Product> prod = productService.findProductBySellerById(id);
        User pageUser = productService.findSellerById(id);
        boolean roots = id == user.getId();
        model.addAttribute("pageUser", pageUser);
        model.addAttribute("roots",roots);
        model.addAttribute("isAuthorized", user);
        model.addAttribute("prod", prod);
        return "mainPage";
    }
}
