package de.ait_tr.shop.controller;

import de.ait_tr.shop.exception_handling.exceptions.ThirdTestException;
import de.ait_tr.shop.model.dto.CustomerDTO;
import de.ait_tr.shop.service.interfaces.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        // Todo обращаемся к сервису для сохранения клиента
        return customerService.saveCustomer(customerDTO);
    }

    @GetMapping("/{id}")
    public CustomerDTO getById(@PathVariable long id){

        // Todo обращаемся к сервису для получения клиента по id
        return customerService.getById(id);
    }


    @PutMapping
    public CustomerDTO updateCustomer (@RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer( customerDTO);
    }

    @DeleteMapping("/{id}")
    public CustomerDTO remove(@PathVariable Long id){
        return customerService.remove(id);
    }

    @GetMapping
    public List<CustomerDTO> grtAll(){
        return customerService.grtAll();
    }

    @DeleteMapping
    public CustomerDTO remuveByName(@RequestParam String name) {
        return customerService.remuveByName(name);
    }

    @PutMapping("/restore/{id}")
    public CustomerDTO restoreById(Long id) {
        return customerService.restoreById(id);
    }

    @GetMapping("/count")
    public long getCustomerCount() {
        return customerService.getCustomerCount();
    }

    @GetMapping("/customers/total-cost")
    public BigDecimal getTotalCostOfCustomerProducts(long customerId) {
        return customerService.getTotalCostOfCustomerProducts(customerId);
    }


    public BigDecimal getAverageCostOfCustomerProducts(long customerId) {
        return null;
    }


    public void addProductToCustomerCart(long customerId, long productId) {

    }


    public void remouveProductToCustomerCart(long customerId, long productId) {

    }


    public void remouveCustomerCart(long customerId) {

    }

}
