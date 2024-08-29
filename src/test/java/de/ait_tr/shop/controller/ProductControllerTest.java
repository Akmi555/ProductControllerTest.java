package de.ait_tr.shop.controller;

import de.ait_tr.shop.model.dto.ProductDTO;
import de.ait_tr.shop.model.entity.Role;
import de.ait_tr.shop.model.entity.User;
import de.ait_tr.shop.repository.RoleRepository;
import de.ait_tr.shop.repository.UserRepository;
import de.ait_tr.shop.security.dto.LoginRequestDto;
import de.ait_tr.shop.security.dto.TokenResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate template;

    private HttpHeaders headers;

    private ProductDTO testProduct;

    private String adminAccessToken;
    private String userAccessToken;

    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private static final String TEST_PRODUCT_TITLE = "Test Product2";
    private static final int TEST_PRODUCT_PRICE = 778;
    private static final String TEST_ADMIN_NAME = "Test Admin";
    private static final String TEST_USER_NAME = "Test User";
    private static final String TEST_PASSWORD = "Test password";
    private static final String ROLE_ADMIN_TITLE = "ROLE_ADMIN";
    private static final String ROLE_USER_TITLE = "ROLE_USER";

    private final String URL_HOST = "http://localhost:";
    private final String AUTH_RESOURCE_NAME = "/api/auth";
    private final String PRODUCTS_RESOURCE_NAME = "/api/products";
    private final String LOGIN_ENDPOINT = "/login";
    private static String PRODUCT_ID = "/2";

    private final String BEARER_TOKEN_PREFIX = "Bearer ";


    @BeforeEach
    public void setUp() {
        template = new TestRestTemplate();
        headers = new HttpHeaders();

        // Создаем тестовый продукт
        testProduct = new ProductDTO();
        testProduct.setTitle(TEST_PRODUCT_TITLE);
        testProduct.setPrice(new BigDecimal(TEST_PRODUCT_PRICE));

        Role roleUser = null;
        Role roleAdmin;

        // Пытаемся вытащить тестовых пользователь из БД
        User admin = userRepository.findUserByUserName(TEST_ADMIN_NAME).orElse(null);
        User user = userRepository.findUserByUserName(TEST_USER_NAME).orElse(null);

        if (admin == null) {
            // Создать админа и запись в БД
            roleAdmin = roleRepository.findByTitle(ROLE_ADMIN_TITLE).orElseThrow(() -> new RuntimeException("Role ADMIN not found in DB"));
            roleUser = roleRepository.findByTitle(ROLE_USER_TITLE).orElseThrow(() -> new RuntimeException("Role USER not found in DB"));

            admin = new User();
            admin.setUserName(TEST_ADMIN_NAME);
            admin.setPassword(encoder.encode(TEST_PASSWORD));
            admin.setRoles(Set.of(roleUser, roleAdmin));

            userRepository.save(admin);
        }

        if (user == null){
            // Создать user и запись в БД
            roleUser = (roleUser == null)
                    ? roleRepository.findByTitle(ROLE_USER_TITLE).orElseThrow(() -> new RuntimeException("Role USER not found in DB"))
                    : roleUser;

            user = new User();
            user.setUserName(TEST_USER_NAME);
            user.setPassword(encoder.encode(TEST_PASSWORD));
            user.setRoles(Set.of(roleUser));

            userRepository.save(user);
        }

        // 2. Получить от сервера токены!
        // Создаем объекты LoginRequestDto для админа и пользователя
        LoginRequestDto loginAdminDto = new LoginRequestDto(TEST_ADMIN_NAME, TEST_PASSWORD);
        LoginRequestDto loginUserDto = new LoginRequestDto(TEST_USER_NAME, TEST_PASSWORD);

        // POST -> http://localhost:56500/api/auth/login
        String authUrl = URL_HOST + port + AUTH_RESOURCE_NAME + LOGIN_ENDPOINT;

        HttpEntity<LoginRequestDto> request = new HttpEntity<>(loginAdminDto, headers);

        ResponseEntity<TokenResponseDto> response = template.exchange(
                authUrl,
                HttpMethod.POST,
                request,
                TokenResponseDto.class
        );

        assertTrue(response.hasBody(), "Authorization Admin response body is empty");

        // Authorization: Bearer 234536474
        TokenResponseDto tokenResponseDto = response.getBody();
        adminAccessToken = BEARER_TOKEN_PREFIX + tokenResponseDto.getAccessToken();

        // Получаем токен user
        request = new HttpEntity<>(loginUserDto, headers);
        response = template.exchange(
                authUrl,
                HttpMethod.POST,
                request,
                TokenResponseDto.class
        );

        assertTrue(response.hasBody(), "Authorization User response body is empty");
        tokenResponseDto = response.getBody();
        userAccessToken = BEARER_TOKEN_PREFIX + tokenResponseDto.getAccessToken();
    }

    @Test
    public void positiveGettingAllProductsWithoutAuthorization(){
        // GET -> http://localhost:56500/api/products
        String url = URL_HOST + port + PRODUCTS_RESOURCE_NAME;

        // Класс Void - спец класс-пустышка
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<ProductDTO>> response = template.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<ProductDTO>>() {}
        );

        // Проверка статуса ответа
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Respons has unexpected ststus");
        System.out.println(response.getStatusCode());

        // Проверка наличия тела в ответе
        assertTrue(response.hasBody(), "Response doesn't have a body");
        System.out.println(response.getBody());

    }

    @Test
    public void negativeSavingProductWithoutAuthorization(){

        String url = URL_HOST + port + PRODUCTS_RESOURCE_NAME;

        HttpEntity<ProductDTO> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDTO> response = template.exchange(
                url,
                HttpMethod.POST,
                request,
                ProductDTO.class
        );

        // Проверка статуса: статус 403 Forbidden
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Respons has unexpected ststus");
        System.out.println(response.getStatusCode());

        // Проверка ответа: отсутствие тела
        assertFalse(response.hasBody(), "Response doesn't have a body");
        System.out.println(response.getBody());
    }

    @Test
    public void positiveSavingProductAdminAuthorization() {

        String url = URL_HOST + port + PRODUCTS_RESOURCE_NAME;

        headers.set("Authorization", adminAccessToken); // Добавление токена админа в запрос
        HttpEntity<ProductDTO> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDTO> response = template.exchange(
                url,
                HttpMethod.POST,
                request,
                ProductDTO.class
        );


        // Проверка статуса ответа
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Respons has unexpected ststus");
        System.out.println(response.getStatusCode());

        // Проверка наличия тела в ответе
        assertTrue(response.hasBody(), "Response doesn't have a body");
        System.out.println(response.getBody());
    }

    @Test
    public void positiveGettingProductsByIdUserAuthorization(){
        // GET -> http://localhost:56500/api/products/2
        String url = URL_HOST + port + PRODUCTS_RESOURCE_NAME + PRODUCT_ID;

        headers.set("Authorization", userAccessToken); // Добавление токена user в запрос
        HttpEntity<ProductDTO> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDTO> response = template.exchange(
                url,
                HttpMethod.GET,
                request,
                ProductDTO.class
        );

        // Проверка статуса ответа
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Respons has unexpected ststus");
        System.out.println(response.getStatusCode());

        // Проверка наличия тела в ответе
        assertTrue(response.hasBody(), "Response doesn't have a body");
        System.out.println(response.getBody());
    }

    @Test
    public void negativeGettingProductsByIdWithoutAuthorization(){
        // GET -> http://localhost:56500/api/products/2
        String url = URL_HOST + port + PRODUCTS_RESOURCE_NAME + PRODUCT_ID;

//        headers.set("Authorization", userAccessToken); // Добавление токена user в запрос
        HttpEntity<ProductDTO> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDTO> response = template.exchange(
                url,
                HttpMethod.GET,
                request,
                ProductDTO.class
        );

        // Проверка статуса ответа
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Respons has unexpected ststus");
        System.out.println(response.getStatusCode());

        // Проверка наличия тела в ответе
        assertFalse(response.hasBody(), "Response doesn't have a body");
        System.out.println(response.getBody());
    }

    @Test
    public void negativeGettingProductsByIdUserAuthorization(){
        // GET -> http://localhost:56500/api/products/2
        String url = URL_HOST + port + PRODUCTS_RESOURCE_NAME + PRODUCT_ID;

        headers.set("Authorization", userAccessToken); // Добавление токена user в запрос
        HttpEntity<ProductDTO> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDTO> response = template.exchange(
                url,
                HttpMethod.GET,
                request,
                ProductDTO.class
        );

        // Проверка статуса ответа
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Respons has unexpected ststus");
        System.out.println(response.getStatusCode());

        // Проверка наличия тела в ответе
        assertFalse(response.hasBody(), "Response doesn't have a body");
        System.out.println(response.getBody());
    }

    @Test
    public void negativeGettingProductsByIdUserAuthorizationInvalidToken(){
        // GET -> http://localhost:56500/api/products/2
        String url = URL_HOST + port + PRODUCTS_RESOURCE_NAME + PRODUCT_ID;

        headers.set("Authorization", userAccessToken + "211a"); // Добавление токена user в запрос и умышленная порча токена
        HttpEntity<ProductDTO> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDTO> response = template.exchange(
                url,
                HttpMethod.GET,
                request,
                ProductDTO.class
        );

        // Проверка статуса ответа
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Respons has unexpected ststus");
        System.out.println(response.getStatusCode());

        // Проверка наличия тела в ответе
        assertFalse(response.hasBody(), "Response doesn't have a body");
        System.out.println(response.getBody());
    }

    @Test
    public void positiveGettingBasicAuthorization(){
        // GET -> http://localhost:56500/api/products/2
        String url = URL_HOST + port + PRODUCTS_RESOURCE_NAME + PRODUCT_ID;

        headers.set("Authorization", userAccessToken); // Добавление токена user в запрос
        HttpEntity<ProductDTO> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDTO> response = template
                .withBasicAuth(TEST_USER_NAME, TEST_PASSWORD)
                .exchange(url, HttpMethod.GET, request, ProductDTO.class);

//        ResponseEntity<ProductDTO> response = template.exchange(
//                url,
//                HttpMethod.GET,
//                request,
//                ProductDTO.class
//        );

        // Проверка статуса ответа
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Respons has unexpected ststus");
        System.out.println(response.getStatusCode());

        // Проверка наличия тела в ответе
        assertTrue(response.hasBody(), "Response doesn't have a body");
        System.out.println(response.getBody());
    }
}