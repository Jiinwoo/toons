package day.toons.global.error

import day.toons.global.error.exception.ErrorCode
import org.springframework.validation.BindingResult
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.*
import java.util.stream.Collectors


class ErrorResponse {
    val message: String
    val status: Int
    val errors: List<FieldError>
    val code: String

    companion object {
        fun of(code: ErrorCode, bindingResult: BindingResult): ErrorResponse {
            return ErrorResponse(code, FieldError.of(bindingResult))
        }

        fun of(code: ErrorCode): ErrorResponse {
            return ErrorResponse(code)
        }

        fun of(code: ErrorCode, errors: List<FieldError>): ErrorResponse {
            return ErrorResponse(code, errors)
        }

        fun of(e: MethodArgumentTypeMismatchException): ErrorResponse {
            val value = if (e.value == null) "" else e.value.toString()
            val errors: List<FieldError> = FieldError.of(e.name, value, e.errorCode)
            return ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors)
        }
    }

    constructor(code: ErrorCode, errors: List<FieldError>) {
        this.message = code.message
        this.status = code.status
        this.errors = errors
        this.code = code.code
    }

    constructor(code: ErrorCode) {
        this.message = code.message
        this.status = code.status
        this.errors = Collections.emptyList()
        this.code = code.code
    }

    class FieldError private constructor(
        val field: String,
        val value: String,
        val reason: String?
    ) {
        companion object {
            fun of(field: String, value: String, reason: String): List<FieldError> {
                val fieldErrors: MutableList<FieldError> = ArrayList()
                fieldErrors.add(FieldError(field, value, reason))
                return fieldErrors
            }

            fun of(bindingResult: BindingResult): List<FieldError> {
                val fieldErrors = bindingResult.fieldErrors
                return fieldErrors.stream()
                    .map { error: org.springframework.validation.FieldError ->
                        FieldError(
                            error.field,
                            if (error.rejectedValue == null) "" else error.rejectedValue.toString(),
                            error.defaultMessage
                        )
                    }
                    .collect(Collectors.toList())
            }
        }
    }

}