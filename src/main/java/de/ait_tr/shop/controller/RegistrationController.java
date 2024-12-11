package de.ait_tr.shop.controller;


import de.ait_tr.shop.exception_handling.Response;
import de.ait_tr.shop.model.dto.UserRegisterDto;
import de.ait_tr.shop.service.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

        @PostMapping
    public Response register (@RequestBody UserRegisterDto userRegisterDto){
        userService.register(userRegisterDto);
        return new Response("Registration Complete. Please check your email");
    }
}
