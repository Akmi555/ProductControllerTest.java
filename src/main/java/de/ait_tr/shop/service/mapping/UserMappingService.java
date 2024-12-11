package de.ait_tr.shop.service.mapping;

import de.ait_tr.shop.model.dto.ProductDTO;
import de.ait_tr.shop.model.dto.UserRegisterDto;
import de.ait_tr.shop.model.entity.Product;
import de.ait_tr.shop.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMappingService {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userName", source = "username")
    User mapDtoToEntity(UserRegisterDto dto);


}
