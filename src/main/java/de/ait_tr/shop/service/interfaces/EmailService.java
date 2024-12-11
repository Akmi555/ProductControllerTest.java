package de.ait_tr.shop.service.interfaces;

import de.ait_tr.shop.model.entity.User;

public interface EmailService {
    // Метод для отправки email пользователю
    void sendConfirmationEmail(User user);

}
