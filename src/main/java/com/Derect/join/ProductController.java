package com.Derect.join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public String list(@RequestParam(required = false, defaultValue = "") String filter,
                        Model model
    ){
        Iterable<Product> prod;
        if(filter != null && !filter.isEmpty()){
            prod = productRepository.findByName(filter);
        } else {
            prod = productRepository.findAll();
        }

        model.addAttribute("prod", prod);
        return "home";
    }

    @PostMapping()
    public String add(
        @RequestParam("name") String name,
        @RequestParam("price") int price,
        Product product
    ){
        product.setName(name);
        product.setPrice(price);
        productRepository.save(product);

        return "redirect:/product";
    }

    @GetMapping("/update")
    public String up(@RequestParam("id") Long id,
                         Model model
    ){
        Optional<Product> product = productRepository.findById(id);
        Product prod = product.isPresent() ? product.get() : null ;
        model.addAttribute("prod", prod);
        return "updateMessage";
    }

    @PostMapping("/update")
    public String update(@RequestParam("name") String name,
                         @RequestParam("id") Long id,
                         @RequestParam("price") int price,
                         Model model
    ){
        Optional<Product> product = productRepository.findById(id);
        Product prod = product.isPresent() ? product.get() : null ;
        prod.setPrice(price);
        prod.setName(name);
        productRepository.save(prod);
        return "redirect:/product";
    }
}
