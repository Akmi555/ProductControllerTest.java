package de.ait_tr.shop.service;

import de.ait_tr.shop.exception_handling.exceptions.FirstTestException;
import de.ait_tr.shop.exception_handling.exceptions.SecondTestException;
import de.ait_tr.shop.exception_handling.exceptions.ThirdTestException;
import de.ait_tr.shop.model.dto.ProductDTO;
import de.ait_tr.shop.model.entity.Product;
import de.ait_tr.shop.repository.ProductRepository;
import de.ait_tr.shop.service.interfaces.ProductService;
import de.ait_tr.shop.service.mapping.ProductMappingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMappingService mapper;

//    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository repository, ProductMappingService mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        System.out.println("Method save work");
        Product product = mapper.mapDtoToEntity(productDTO);
//        product.setActive(true);
        return mapper.mapEntityToDto(repository.save(product));
    }

//    @Override
//    public ProductDTO getById(long id) {
//        logger.info("Method getById called with parameter: {}", id);
//        logger.warn("Method getById called with parameter: {}", id);
//        logger.error("Method getById called with parameter: {}", id);
//
//        Product product = repository.findById(id).orElse(null);
//
//        if (product == null || !product.isActive()){
//            return null;
//        }
//        return mapper.mapEntityToDto(product);
//    }

    @Override
    public ProductDTO getById(long id) {

        Product product = repository.findById(id).orElse(null);

        if (product == null ){
            throw  new ThirdTestException("Product with id" + id + " not found");
        }
        if (!product.isActive()){
            throw new FirstTestException("This is first Test Exception message");
        }

        return mapper.mapEntityToDto(product);
    }

    @Override
    public List<ProductDTO> getAll() {
        // создаем поток из элементов списка
        return repository.findAll().stream()
                // фильтруем
                .filter(Product::isActive)
               // превращием элемент срима из Product в срима ProductDTO
                .map(mapper::mapEntityToDto)
                // собираем обратно в список
                //.collect(Collectors.toList()) старая версия
                .toList();
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        return null;
    }

    @Override
    public ProductDTO remove(Long id) {
        return null;
    }



    @Override
    public ProductDTO removeByTitle(String title) {
        return null;
    }

    @Override
    public ProductDTO restoreById(Long id) {
        return null;
    }

    @Override
    public long getProductsCount() {
        return 0;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return null;
    }

    @Override
    public BigDecimal getAverageOrice() {
        return null;
    }
}
