package de.ait_tr.shop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;


@OpenAPIDefinition(
        info = @Info(
                title = "Application Shop",
                description = "Application for various operations with Customers and Products",
                version = "1.0.0",
                contact = @Contact(
                        name = "Evgen Grushchenco",
                        email = "evgen.tm959@gmail.com",
                        url = "https://ait-tr.de"
                )
        )
)
public class SwaggerConfig {
}
