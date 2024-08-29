package de.ait_tr.shop.service.interfaces;

import de.ait_tr.shop.model.dto.CustomerDTO;
import de.ait_tr.shop.model.entity.Customer;

import java.math.BigDecimal;
import java.util.List;

/*
Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным)
Вернуть всех покупателей из базы данных (активных)
Вернуть одного покупателя из базы данных по его идентификатору (если он активен)
Изменить одного покупателя в базе данных по его идентификатору
Удалить покупателя из базы данных по его идентификатору
Удалить покупателя из базы данных по его имени
Восстановить удалённого покупателя в базе данных по его идентификатору
Вернуть общее количество покупателей в базе данных (активных)
Вернуть стоимость корзины покупателя по его идентификатору (если он активен)
Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
Удалить товар из корзины покупателя по их идентификаторам
Полностью очистить корзину покупателя по его идентификатору (если он активен)

 */

public interface CustomerService {

    public CustomerDTO saveCustomer(CustomerDTO customerDTO);

    public CustomerDTO getById(long id);

    public List<CustomerDTO> grtAll();

    public CustomerDTO updateCustomer (CustomerDTO customerDTOr);

    public CustomerDTO remove(Long id);

    public CustomerDTO remuveByName (String name);

    public CustomerDTO restoreById(Long id);

    public long getCustomerCount();

    BigDecimal getTotalCostOfCustomerProducts(long customerId);

    BigDecimal getAverageCostOfCustomerProducts(long customerId);

    void addProductToCustomerCart(long customerId, long productId);

    void remouveProductToCustomerCart(long customerId, long productId);

    void remouveCustomerCart(long customerId);
}
