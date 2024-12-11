package de.ait_tr.shop.service;

import de.ait_tr.shop.model.dto.UserRegisterDto;
import de.ait_tr.shop.model.entity.ConfirmationCode;
import de.ait_tr.shop.model.entity.User;
import de.ait_tr.shop.repository.UserRepository;
import de.ait_tr.shop.service.interfaces.ConfirmationCodeService;
import de.ait_tr.shop.service.interfaces.EmailService;
import de.ait_tr.shop.service.interfaces.RoleService;
import de.ait_tr.shop.service.interfaces.UserService;
import de.ait_tr.shop.service.mapping.UserMappingService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private  final RoleService roleService;
    private final EmailService emailService;
    private  final UserMappingService userMappingService;
    private final ConfirmationCodeService confirmationCodeService;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           RoleService roleService,
                           EmailService emailService,
                           UserMappingService userMappingService, ConfirmationCodeService confirmationCodeService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.emailService = emailService;
        this.userMappingService = userMappingService;
        this.confirmationCodeService = confirmationCodeService;
    }

    @Transactional
    @Override
    public void register(UserRegisterDto registerDto) {
        User user = userMappingService.mapDtoToEntity(registerDto);

        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        // Проверка, существует ли уже пользователь с таким email
        if (optionalUser.isPresent() && optionalUser.get().isActive()) {
            throw new RuntimeException("Email" + user.getEmail() + " already in use");
        }

        if (optionalUser.isPresent()) {
            // Пользователь в базе со статусом active - false
            user = optionalUser.get();
//            ConfirmationCode codeOld = confirmationCodeService.findCodeByUser(user).orElse(null);
//            if (codeOld != null) {
//                confirmationCodeService.remove(codeOld);
//            }
        } else {
            // Регистрация нового пользователя
            // Присваиваем роль User новому пользователю
            user.setRoles(Set.of(roleService.getRoleUser()));
        }

        // Устанавливаем зашифрованный пароль
        user.setPassword(passwordEncoder.encode(registerDto.password()));

        // На всякий случай
        user.setActive(false);

        // Сохранить пользователя в БД
        userRepository.save(user);

        // Отправляем письмо с кодом подтверждения
        emailService.sendConfirmationEmail(user);
    }



    @Transactional
    @Override
    public String confirmationMailByCode(String code) {
        ConfirmationCode confirmationCode = confirmationCodeService.findByCode(code).orElseThrow(
                () -> new RuntimeException("Code not found")
        );

        if (confirmationCode.getExpired().isAfter(LocalDateTime.now())) {
            User user = confirmationCode.getUser();
            user.setActive(true);
            userRepository.save(user);
            return user.getEmail() + " confirmed!";
        }

        throw new RuntimeException("Wrong code");
    }
}
