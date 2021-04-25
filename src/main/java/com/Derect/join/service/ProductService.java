package com.Derect.join.service;

import com.Derect.join.entity.Product;
import com.Derect.join.entity.User;
import com.Derect.join.repository.ProductRepository;
import com.Derect.join.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

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

    public User findSellerById(Long id){
        Optional<User> preUser = userRepository.findById(id);
        return preUser.isPresent() ? preUser.get() : null ;
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
                               MultipartFile file) throws IOException {
        if(!file.isEmpty()) {
           saveFile(prod,file);
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

    public Iterable<Product> findProductBySellerById(Long id){
        Optional<User> user = userRepository.findById(id);
        User seller = user.isPresent() ? user.get() : null;
        if(seller != null) {
            return productRepository.findBySeller(seller);
        }
        return null;
    }


    public Iterable<Product> findProductBySeller(User user){
        Iterable<Product> prod = productRepository.findBySeller(user);
        return prod;
    }
}
