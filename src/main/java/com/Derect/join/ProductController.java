package com.Derect.join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
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
                       Model model){
        Iterable<Product> prod;
        if(filter != null && !filter.isEmpty()){
            prod = productRepository.findByName(filter);
        } else {
            prod = productRepository.findAll();
        }

        model.addAttribute("prod", prod);
        return "messList";
    }

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
        Product product,
        @RequestParam("file") MultipartFile file
    ) throws IOException{
        saveFile(product, file);
        product.setName(name);
        product.setPrice(price);
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
    public String delete(@RequestParam("id") Long id){
        Optional<Product> product = productRepository.findById(id);
        Product prod = product.isPresent() ? product.get() : null ;
        File file = new File(uploadPath + "/" + prod.getFilename());
        productRepository.deleteById(id);
        file.delete();
        return "redirect:/product";
    }
}
