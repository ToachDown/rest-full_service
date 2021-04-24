package com.Derect.join.service;

import com.Derect.join.entity.Product;
import com.Derect.join.entity.User;
import com.Derect.join.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ProductRepository productRepository;

    public void saveFile(Product product,
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

    public Iterable<Product> checkFilter(String filter){
        Iterable<Product> prod;
        if(filter != null && !filter.isEmpty()){
            prod = productRepository.findByName(filter);
        } else {
            prod = productRepository.findAll();
        }
        return prod;
    }

    public void deleteProduct(Long id){
        Optional<Product> product = productRepository.findById(id);
        Product prod = product.isPresent() ? product.get() : null ;
        File file = new File(uploadPath + "/" + prod.getFilename());
        productRepository.deleteById(id);
        file.delete();
    }

    public void saveProduct(Product prod){
        productRepository.save(prod);
    }

    public Product checkUpdate(Product prod,
                               String name,
                               int price,
                               MultipartFile file){
        if(!file.isEmpty()) {
            try {
                saveFile(prod, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(name != prod.getName()) {
            prod.setName(name);
        }
        if(price != prod.getPrice()){
            prod.setPrice(price);
        }
        return prod;
    }

    public Product findProductById(Long id){
        Optional<Product> product = productRepository.findById(id);
        Product prod = product.isPresent() ? product.get() : null ;
        return prod;
    }

    public Iterable<Product> findProductBySeller(User user){
        Iterable<Product> prod = productRepository.findBySeller(user);
        return prod;
    }
}
