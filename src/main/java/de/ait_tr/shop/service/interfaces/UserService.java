package de.ait_tr.shop.service.interfaces;

import de.ait_tr.shop.model.dto.UserRegisterDto;
import jakarta.transaction.Transactional;

public interface UserService {

    void register(UserRegisterDto registerDto);

    String confirmationMailByCode(String code);

}
