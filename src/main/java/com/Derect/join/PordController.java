package com.Derect.join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class PordController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public String list(Model model){
            Iterable<Product> prod = productRepository.findAll();
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

        return "redirect:home";
    }

}
