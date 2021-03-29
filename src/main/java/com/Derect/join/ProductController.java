package com.Derect.join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}
