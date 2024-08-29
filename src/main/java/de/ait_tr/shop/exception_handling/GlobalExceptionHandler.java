package de.ait_tr.shop.exception_handling;

import de.ait_tr.shop.exception_handling.exceptions.ThirdTestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ThirdTestException.class)
    public ResponseEntity<Response> hendelThirdTestException(ThirdTestException e){
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //BindingResult - поля с ошибками
    // FieldError - класс, который представляет ошибку
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // Cоздаем список ошибок для накопления сообщений об ошибках
        List<String> errors = new ArrayList<>();

        //перебираем все ошибки
        for (FieldError error: ex.getBindingResult().getFieldErrors()) {
            // Добавляем сообщение об ошибке для текущего поля
            errors.add(error.getField() + " -> " +  error.getDefaultMessage());
        }

        //Создаем объект Response с накопленным сообщение
        ValidationResponse response = new ValidationResponse(errors);

        // Возвращаем ResponseEntity с объектом Response и статусом 400
        return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Response> hendleValidationException(MethodArgumentNotValidException ex){
//        // Создаем объект StringBuilder для накопления сообщений об ошибках
//        StringBuilder erroeMessages = new StringBuilder();
//
//        // Перебираем все ошибки
//        for (FieldError error: ex.getBindingResult().getFieldErrors()){
//            // Добавляем сообщение об ошибке для текущего поля
//            erroeMessages.append(error.getDefaultMessage()).append(": \n");
//        }
//
//        // Создаем объект Response с накопленным сообщение
//        Response response = new Response(erroeMessages.toString());
//
//        // Возвращаем ResponseEntity с объектом Response и статусом 400
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//
//    }
}
