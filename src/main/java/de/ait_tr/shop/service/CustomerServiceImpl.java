package de.ait_tr.shop.service;

import de.ait_tr.shop.exception_handling.exceptions.ThirdTestException;
import de.ait_tr.shop.model.dto.CustomerDTO;
import de.ait_tr.shop.model.entity.Customer;
import de.ait_tr.shop.repository.CustomerRepository;
import de.ait_tr.shop.service.interfaces.CustomerService;
import de.ait_tr.shop.service.mapping.CustomerMappingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMappingService mapper;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMappingService mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = mapper.mapDtoToEntity(customerDTO);
        customer.setActive(true);
        return mapper.mapEntityToDto(repository.save(customer));
    }

    @Override
    public CustomerDTO getById(long id) {
        if (id == 5 ){
            throw  new ThirdTestException("Customer controller exception");
        }
        Customer customer = repository.findById(id).orElse(null);
        if (customer == null || !customer.isActive()) {
            return null;
        }
        return mapper.mapEntityToDto(customer);
    }

    @Override
    public List<CustomerDTO> grtAll() {
        return repository.findAll().stream()
                .filter(Customer::isActive)
                .map(mapper::mapEntityToDto)
                .toList();
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public CustomerDTO remove(Long id) {
        return null;
    }

    @Override
    public CustomerDTO remuveByName(String name) {
        return null;
    }

    @Override
    public CustomerDTO restoreById(Long id) {
        return null;
    }

    @Override
    public long getCustomerCount() {
        return 0;
    }

    @Override
    public BigDecimal getTotalCostOfCustomerProducts(long customerId) {
        return null;
    }

    @Override
    public BigDecimal getAverageCostOfCustomerProducts(long customerId) {
        return null;
    }

    @Override
    public void addProductToCustomerCart(long customerId, long productId) {

    }

    @Override
    public void remouveProductToCustomerCart(long customerId, long productId) {

    }

    @Override
    public void remouveCustomerCart(long customerId) {

    }
}
