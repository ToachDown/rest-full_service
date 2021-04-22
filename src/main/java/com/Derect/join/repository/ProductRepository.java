package com.Derect.join.repository;

import com.Derect.join.entity.Product;
import com.Derect.join.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    Iterable<Product> findByName(String name);
    Iterable<Product> findBySeller(User user);
}
