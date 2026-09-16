package com.spently.dto.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.spently.enums.RestData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"status", "message", "data"})
@Builder
public class ResponseResult {
    private RestData status;

    private String message;

    private Object data;

    /**
     * CASE SUCCESS
     */

    // success without message
    public static ResponseEntity<ResponseResult> ok(Object data) {
        return success(HttpStatus.OK, null, data);
    }

    public static ResponseEntity<ResponseResult> ok(String message, Object data) {
        return success(HttpStatus.OK, message, data);
    }

    // create success without message
    public static ResponseEntity<ResponseResult> created(Object data) {
        return success(HttpStatus.CREATED, null, data);
    }

    // create success message
    public static ResponseEntity<ResponseResult> created(String message, Object data) {
        return success(HttpStatus.CREATED, message, data);
    }

    /**
     * CASE ERROR
     */
    public static ResponseEntity<ResponseResult> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message, null);
    }

    public static ResponseEntity<ResponseResult> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message, null);
    }

    public static ResponseEntity<ResponseResult> conflict(String message) {
        return error(HttpStatus.CONFLICT, message, null);
    }

    public static ResponseEntity<ResponseResult> unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED, message, null);
    }

    public static ResponseEntity<ResponseResult> forbidden(String message) {
        return error(HttpStatus.FORBIDDEN, message, null);
    }

    public static ResponseEntity<ResponseResult> unprocessableEntity(String message) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, message, null);
    }

    public static ResponseEntity<ResponseResult> internalServerError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }

    /**
     * BASE RESPONSE FOR 2 CASE : SUCCESS AND ERROR
     */
    public static ResponseEntity<ResponseResult> success(HttpStatus httpStatus, String message, Object data) {
        return ResponseEntity
                .status(httpStatus)
                .body(
                        ResponseResult.builder()
                                .status(RestData.SUCCESS)
                                .message(message)
                                .data(data)
                                .build()
                );
    }

    public static ResponseEntity<ResponseResult> error(HttpStatus httpStatus, String message, Object data) {
        return ResponseEntity
                .status(httpStatus)
                .body(
                        ResponseResult.builder()
                                .status(RestData.ERROR)
                                .message(message)
                                .data(data)
                                .build()
                );
    }
}

