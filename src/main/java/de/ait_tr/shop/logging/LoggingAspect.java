package de.ait_tr.shop.logging;

import de.ait_tr.shop.model.dto.ProductDTO;
import de.ait_tr.shop.service.ProductServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Aspect
@Component
public class LoggingAspect {

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Pointcut("execution(* de.ait_tr.shop.service.ProductServiceImpl.saveProduct(..))")
    public void  saveProduct(){
        // Метод без тела, используется только для задания Pointcut
    }

    @Before("saveProduct()")
    public void beforeSavingProduct(JoinPoint joinPoint){
        // Извлекаем параметры вызова метода
        Object[] params = joinPoint.getArgs();
        logger.info("Method save in class ProductServiceImpl was called with parameter: {}", params[0]);
    }

    @After("saveProduct()")
    public void afterSavingProduct(){
        logger.info("Method save in class ProductServiceImpl finished its work");
    }

    @Pointcut("execution(* de.ait_tr.shop.service.ProductServiceImpl.getById())")
    public void getProductById(){ }

    @AfterReturning(pointcut = "getProductById()", returning = "result")
    public void afterReturningFromGetById(Object result){
        logger.info("Method getById successfully returned result: {}", result);
    }

    @AfterThrowing(pointcut = "execution(* de.ait_tr.shop.service.ProductServiceImpl.getById())", throwing = "ex")
    public void afterThrowingExceptionFromGetBYId(JoinPoint joinPoint, Exception ex){
        Object[] params = joinPoint.getArgs();
        logger.info("Method getById with param {} throw an exception: {}", params[0], ex.getMessage());
    }

    @Pointcut("execution(* de.ait_tr.shop.service.ProductServiceImpl.getAll(..))")
    public void getAllProduct(){}

    @Around("getAllProduct()")
    public Object aroundGetAllProduct(ProceedingJoinPoint joinPoint) throws  Throwable{
        Object result = null;
        try {
            // Логирование до выполнения основного метода
            logger.info("Method getAllProduct called");

            // Выполняем основной метод и сохраняем результат
            result = joinPoint.proceed();

            // Логируем после успешного выполнения основного метода
            logger.info("Method getAll successfully returned result: {}", result);

            // Изменяем результат, добавляя дополнительную логику
            result = ((List<ProductDTO>) result).stream()
                    .filter(product -> product.getPrice().doubleValue() > 0)
                    .toList();
        } catch (Throwable ex) {
           logger.error("Method getAllProduct: {} ", ex.getMessage());
        }

        return result == null ? new ArrayList<>() : result;
    }

    @Pointcut("execution(* de.ait_tr.shop.service.ProductServiceImpl.*(..))")
    public void  productServiceImpl(){}

    @Around("productServiceImpl()")
    public Object allMethodsClassProductServiceImpl(ProceedingJoinPoint joinPoint){
        Object result = null;
        Object[] params = joinPoint.getArgs();// вытягиваем параметры которые
                                                // приходят в метод
        String name = joinPoint.getSignature().getName();// вытягиваем имя метода
        try {
            logger.info("Method {} with param: {}", name, params);

             result = joinPoint.proceed(); // запускаем метод и вытягиваем результат

             logger.info("Method {} successfully returned result: {}", name, result);

        } catch (Throwable ex) {
            logger.error("Method {}: {} ", name, ex.getMessage());
        }


        return result;
    }

}

