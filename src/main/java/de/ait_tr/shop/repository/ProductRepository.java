package de.ait_tr.shop.repository;

import de.ait_tr.shop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findAllByActiveTrue();
//    List<Product> findAllByActive(boolean active);
}
