package com.Derect.join.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "basket",fetch = FetchType.EAGER)
    private Set<Product> basketOfGoods;

    public Basket() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Product> getBasketOfGoods() {
        return basketOfGoods;
    }

    public void setBasketOfGoods(Set<Product> basketOfGoods) {
        this.basketOfGoods = basketOfGoods;
    }
}
