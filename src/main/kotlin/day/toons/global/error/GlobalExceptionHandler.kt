package day.toons.global.error

import day.toons.global.error.exception.BusinessException
import day.toons.global.error.exception.ErrorCode
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


private val logger = KotlinLogging.logger {}
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse?>? {
        logger.error("handleMethodArgumentNotValidException", e)
        val response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException?): ResponseEntity<ErrorResponse?>? {
        logger.error("handleMethodArgumentTypeMismatchException", e)
        val response = ErrorResponse.of(e!!)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException?): ResponseEntity<ErrorResponse?>? {
        logger.error("handleHttpRequestMethodNotSupportedException", e)
        val response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED)
        return ResponseEntity(response, HttpStatus.METHOD_NOT_ALLOWED)
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException::class)
    protected fun handleAccessDeniedException(e: AccessDeniedException?): ResponseEntity<ErrorResponse> {
        logger.error("handleAccessDeniedException", e)
        val response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED)
        return ResponseEntity<ErrorResponse>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.status))
    }

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        logger.error("handleEntityNotFoundException", e)
        val errorCode: ErrorCode = e.errorCode
        val response = ErrorResponse.of(errorCode)
        return ResponseEntity<ErrorResponse>(response, HttpStatus.valueOf(errorCode.status))
    }


    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception?): ResponseEntity<ErrorResponse?>? {
        logger.error("handleEntityNotFoundException", e)
        val response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}