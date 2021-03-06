package com.Derect.join.controllers;

import com.Derect.join.entity.Basket;
import com.Derect.join.entity.Role;
import com.Derect.join.entity.User;
import com.Derect.join.entity.Product;
import com.Derect.join.repository.UserRepository;
import com.Derect.join.service.ProductService;
import com.Derect.join.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public String list(@RequestParam(required = false, defaultValue = "") String filter,
                       @AuthenticationPrincipal User user,
                       Model model
    ){
        Iterable<Product> prod = productService.checkFilter(filter);
        model.addAttribute("isAuthorized", user);
        model.addAttribute("ADMIN",Role.ADMIN);
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
        model.addAttribute("ADMIN",Role.ADMIN);
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
        model.addAttribute("ADMIN",Role.ADMIN);
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
        model.addAttribute("ADMIN",Role.ADMIN);
        model.addAttribute("isAuthorized", user);
        model.addAttribute("prod", prod);
        return "mainPage";
    }

    @PostMapping("/add")
    public String addToBasket(
            @AuthenticationPrincipal User user,
            @RequestParam("productId") Long id
    ){
        Product prod = productService.findProductById(id);
        if(user.getBasket() == null){
            Basket basket = new Basket();
            Set<Product> products = new HashSet<>();
            products.add(prod);
            basket.setBasketOfGoods(products);
            user.setBasket(basket);
        } else {
            Basket basket = user.getBasket();
            Set<Product> products = basket.getBasketOfGoods();
            products.add(prod);
            basket.setBasketOfGoods(products);
            user.setBasket(basket);
        }
        userRepository.save(user);
        return "redirect:/product";
    }

    @GetMapping("/basket")
    public String returnBasket(Model model,
                               @AuthenticationPrincipal User user){
        model.addAttribute("ADMIN",Role.ADMIN);
        model.addAttribute("isAuthorized", user);
        return "basket";
    }

    @PostMapping("/basket/clear")
    public String clearBasket(@AuthenticationPrincipal User user){
        user.setBasket(null);
        userRepository.save(user);
        return "redirect:/product/basket";
    }

    @GetMapping("/basket/buy")
    public String buyProducts(@AuthenticationPrincipal User user,
                              Model model){
        model.addAttribute("ADMIN",Role.ADMIN);
        model.addAttribute("isAuthorized", user);
        return "buyPage";
    }

    @PostMapping("/basket/buy")
    public String buyProducts(@AuthenticationPrincipal User user){

        return "redirect:/product/basket";
    }
}
