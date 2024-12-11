package de.ait_tr.shop.service;

import de.ait_tr.shop.model.entity.ConfirmationCode;
import de.ait_tr.shop.model.entity.User;
import de.ait_tr.shop.repository.ConfirmationCodeRepository;
import de.ait_tr.shop.service.interfaces.ConfirmationCodeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {

    private final ConfirmationCodeRepository repository;

    public ConfirmationCodeServiceImpl(ConfirmationCodeRepository repository) {
        this.repository = repository;
    }


    @Override
    public String generationConfirmationCode(User user) {
        // Генерация уникального кода (использую UUID)
        String code = UUID.randomUUID().toString();

        // Создание объ
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setCode(code);
        confirmationCode.setUser(user);
        confirmationCode.setExpired(LocalDateTime.now().plusDays(1)); // срок действия 1 день

        repository.save(confirmationCode);

        // Вернуть код
        return code;
    }

    @Override
    public Optional<ConfirmationCode> findCodeByUser(User user) {
        return repository.findCodeByUser(user);
    }
    @Override
    public void remove(ConfirmationCode code) {
        repository.delete(code);
    }

    @Override
    public Optional<ConfirmationCode> findByCode(String code) {
        return repository.findByCode(code);
    }

}
