package de.ait_tr.shop.service.interfaces;

import de.ait_tr.shop.model.entity.ConfirmationCode;
import de.ait_tr.shop.model.entity.User;

import java.util.Optional;

public interface ConfirmationCodeService {

    // Метод для генерации кода подтверждения
    String generationConfirmationCode(User user);
    Optional<ConfirmationCode> findCodeByUser(User user);
    void remove(ConfirmationCode code);
    Optional<ConfirmationCode> findByCode(String code);
}
