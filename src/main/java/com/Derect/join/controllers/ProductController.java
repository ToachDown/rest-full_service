package com.Derect.join.controllers;

import com.Derect.join.entity.User;
import com.Derect.join.repository.ProductRepository;
import com.Derect.join.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/home")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       @AuthenticationPrincipal User user,
                       Model model){
        Iterable<Product> prod;
        if(filter != null && !filter.isEmpty()){
            prod = productRepository.findByName(filter);
        } else {
            prod = productRepository.findAll();
        }
        model.addAttribute("isAuthorized", user);
        model.addAttribute("prod", prod);
        return "messList";
    }

    @GetMapping()
    public String list(@RequestParam(required = false, defaultValue = "") String filter,
                       @AuthenticationPrincipal User user,
                        Model model
    ){
        Iterable<Product> prod;
        if(filter != null && !filter.isEmpty()){
            prod = productRepository.findByName(filter);
        } else {
            prod = productRepository.findAll();
        }
        model.addAttribute("isAuthorized", user);
        model.addAttribute("prod", prod);
        return "home";
    }

    @PostMapping()
    public String add(
            @AuthenticationPrincipal User seller,
        @RequestParam("name") String name,
        @RequestParam("price") int price,
        @RequestParam("file") MultipartFile file
    ) throws IOException{
        Product product = new Product(name,price, seller);
        saveFile(product, file);
        productRepository.save(product);

        return "redirect:/product";
    }

    private void saveFile(Product product,
                          @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            product.setFilename(resultFilename);
        }
    }

    @GetMapping("/update")
    public String up(@RequestParam("id") Long id,
                     @AuthenticationPrincipal User user,
                         Model model
    ){
        Optional<Product> product = productRepository.findById(id);
        Product prod = product.isPresent() ? product.get() : null ;
        model.addAttribute("isAuthorized", user);
        model.addAttribute("prod", prod);
        return "updateMessage";
    }

    @PostMapping("/update")
    public String update(@RequestParam("name") String name,
                         @RequestParam("id") Long id,
                         @RequestParam("price") int price,
                         @RequestParam("file") MultipartFile file,
                         Model model
    ) throws  IOException{
        Optional<Product> product = productRepository.findById(id);
        Product prod = product.isPresent() ? product.get() : null ;
        if(prod == null){
            System.out.println("object is not exists");
        }
        saveFile(prod, file);
        if(name != prod.getName()) {
            prod.setName(name);
        }
        if(price != prod.getPrice()){
            prod.setPrice(price);
        }
        productRepository.save(prod);
        return "redirect:/product";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id,
                         @AuthenticationPrincipal User user,
                         Model model){
        Optional<Product> product = productRepository.findById(id);
        Product prod = product.isPresent() ? product.get() : null ;
        File file = new File(uploadPath + "/" + prod.getFilename());
        productRepository.deleteById(id);
        file.delete();
        model.addAttribute("isAuthorized", user);
        return "redirect:/product";
    }
}
