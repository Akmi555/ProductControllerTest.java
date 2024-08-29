package de.ait_tr.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.ait_tr.shop.model.entity.Product;

import java.util.List;
import java.util.Objects;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CartDTO {

    private Long id;
    @JsonBackReference
    private CustomerDTO customer;
    List<Product> products;

    @Override
    public String toString() {
        return String.format("CartDTO: id: %d, products: %s, customer: %s", id, products == null ? "null" : products, customer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartDTO cartDTO)) return false;
        return Objects.equals(id, cartDTO.id) && Objects.equals(customer, cartDTO.customer) && Objects.equals(products, cartDTO.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, products);
    }
}
