package com.Derect.join.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basket_user_id")
    private User currentUser;

    @OneToMany(mappedBy = "basket",fetch = FetchType.EAGER)
    private Set<Product> basketOfGoods;

    public Basket() {
    }

    public int sumPrice(){
        int sum = 0;
        for (Product prod : basketOfGoods) {
            sum += prod.getPrice();
        }
        return sum;
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

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
