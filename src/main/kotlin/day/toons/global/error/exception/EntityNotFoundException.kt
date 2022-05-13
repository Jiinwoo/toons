package day.toons.global.error.exception

import day.toons.global.error.exception.BusinessException
import day.toons.global.error.exception.ErrorCode

open class EntityNotFoundException(message: String?) : BusinessException(message, ErrorCode.ENTITY_NOT_FOUND)