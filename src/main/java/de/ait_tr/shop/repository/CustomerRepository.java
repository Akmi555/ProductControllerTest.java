package de.ait_tr.shop.repository;

import de.ait_tr.shop.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
