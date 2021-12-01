package org.pyruz.api.shop.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.pyruz.api.shop.configuration.ApplicationContextHolder;
import org.pyruz.api.shop.model.dto.BaseDTO;
import org.pyruz.api.shop.model.dto.MetaDTO;
import org.pyruz.api.shop.utility.TypesHelper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ServiceExceptionHandler extends ApplicationContextHolder {

    // --> ServiceLevelValidation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String errorMessage = applicationProperties.getProperty(error.getDefaultMessage());
            if (errorMessage == null)
                errorMessage = applicationProperties.getProperty("application.message.validationError.text");
            Integer errorCode = applicationProperties.getCode("application.message.validationError.code");
            String fieldName = (applicationProperties.getProperty(error.getField()) != null) ? applicationProperties.getProperty(error.getField()) : error.getField();
            String validationErrorType = (error.getCodes() != null && error.getCodes().length == 4) ? error.getCodes()[3] : null;
            String sizeError = "";
            if (validationErrorType != null) switch (validationErrorType) {
                case "NotBlank":
                    errorCode = applicationProperties.getCode("application.message.missingParameter.code");
                    errorMessage = applicationProperties.getProperty("application.message.missingParameter.text");
                    break;
                case "Min":
                    errorCode = applicationProperties.getCode("application.message.invalidMinLength.code");
                    errorMessage = applicationProperties.getProperty("application.message.invalidMinLength.text");
                    sizeError = (error.getArguments() != null && error.getArguments().length == 2) ? error.getArguments()[1].toString() : "";
                    break;
                case "Max":
                    errorCode = applicationProperties.getCode("application.message.invalidMaxLength.code");
                    errorMessage = applicationProperties.getProperty("application.message.invalidMaxLength.text");
                    sizeError = (error.getArguments() != null && error.getArguments().length == 2) ? error.getArguments()[1].toString() : "";
                    break;
                case "Size":
                    errorCode = applicationProperties.getCode("application.message.invalidLength.code");
                    errorMessage = applicationProperties.getProperty("application.message.invalidLength.text");
                    if (error.getArguments() != null && error.getArguments().length == 3) {
                        Integer maxValue = TypesHelper.tryParseInt(error.getArguments()[1]);
                        Integer minValue = TypesHelper.tryParseInt(error.getArguments()[2]);
                        if (minValue != null && minValue != 0) {
                            sizeError = applicationProperties.getProperty("application.message.less") + " " + minValue.toString() + " ";
                        }
                        if (maxValue != null && maxValue != Integer.MAX_VALUE) {
                            if (!sizeError.equals(""))
                                sizeError = String.format("%s %s ", sizeError, applicationProperties.getProperty("application.message.and"));
                            sizeError += applicationProperties.getProperty("application.message.more") + " " + maxValue.toString() + " ";
                        }
                    }
                    break;
                default:
                    sizeError = "";
                    break;
            }
            MetaDTO metaDTO = new MetaDTO(errorCode, String.format(errorMessage, fieldName, sizeError));
            baseDTO.setMeta(metaDTO);
            break;
        }
        return new ResponseEntity<>(baseDTO, HttpStatus.BAD_REQUEST);
    }

    // --> ServiceLevelValidation
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<BaseDTO> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO();
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            String errorMessage = applicationProperties.getProperty(error.getMessage());
            if (errorMessage == null)
                errorMessage = applicationProperties.getProperty("application.message.validationError.text");
            Integer errorCode = applicationProperties.getCode("application.message.validationError.code");
            String field = ((PathImpl) error.getPropertyPath()).getLeafNode().getName();
            String fieldName = (applicationProperties.getProperty(field) != null) ? applicationProperties.getProperty(field) : field;
            String sizeError = "";
            if (error.getConstraintDescriptor().getAnnotation().annotationType().equals(javax.validation.constraints.NotBlank.class)) {
                errorCode = applicationProperties.getCode("application.message.missingParameter.code");
                errorMessage = applicationProperties.getProperty("application.message.missingParameter.text");
            }
            if (error.getConstraintDescriptor().getAnnotation().annotationType().equals(javax.validation.constraints.Min.class)) {
                errorCode = applicationProperties.getCode("application.message.invalidMinLength.code");
                errorMessage = applicationProperties.getProperty("application.message.invalidMinLength.text");
                sizeError = (error.getConstraintDescriptor().getAttributes().get("value") != null) ? error.getConstraintDescriptor().getAttributes().get("value").toString() : "";
            }
            if (error.getConstraintDescriptor().getAnnotation().annotationType().equals(javax.validation.constraints.Max.class)) {
                errorCode = applicationProperties.getCode("application.message.invalidMaxLength.code");
                errorMessage = applicationProperties.getProperty("application.message.invalidMaxLength.text");
                sizeError = (error.getConstraintDescriptor().getAttributes().get("value") != null) ? error.getConstraintDescriptor().getAttributes().get("value").toString() : "";
            }
            if (error.getConstraintDescriptor().getAnnotation().annotationType().equals(javax.validation.constraints.Size.class)) {
                errorMessage = applicationProperties.getProperty("application.message.invalidLength.text");
                Integer maxValue = TypesHelper.tryParseInt(error.getConstraintDescriptor().getAttributes().get("max"));
                Integer minValue = TypesHelper.tryParseInt(error.getConstraintDescriptor().getAttributes().get("min"));
                if (minValue != null && minValue != 0) {
                    sizeError = applicationProperties.getProperty("application.message.less") + " " + minValue.toString() + " ";
                }
                if (maxValue != null && maxValue != Integer.MAX_VALUE) {
                    if (!sizeError.equals(""))
                        sizeError = String.format("%s %s ", sizeError, applicationProperties.getProperty("application.message.and"));
                    sizeError += applicationProperties.getProperty("application.message.more") + " " + maxValue.toString() + " ";
                }
            }
            MetaDTO metaDTO = new MetaDTO(errorCode, String.format(errorMessage, fieldName, sizeError));
            baseDTO.setMeta(metaDTO);
            break;
        }
        return new ResponseEntity<>(baseDTO, HttpStatus.BAD_REQUEST);
    }

    // --> Custom exceptions
    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<BaseDTO> handleAllExceptions(ServiceException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(new MetaDTO(ex.getCode(), ex.getMessage()));
        return new ResponseEntity<>(baseDTO, ex.getHttpStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : ex.getHttpStatus());
    }

    @ExceptionHandler(NoRollBackServiceException.class)
    public final ResponseEntity<BaseDTO> handleAllExceptions(NoRollBackServiceException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(new MetaDTO(ex.getCode(), ex.getMessage()));
        return new ResponseEntity<>(baseDTO, ex.getHttpStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : ex.getHttpStatus());
    }

    // --> Handler not found exceptions
    @ExceptionHandler(NoHandlerFoundException.class)
    public final ResponseEntity<BaseDTO> handleAllExceptions(NoHandlerFoundException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(new MetaDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        return new ResponseEntity<>(baseDTO, HttpStatus.NOT_FOUND);
    }

    // --> General exceptions
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<BaseDTO> handleAllExceptions(Exception ex, HttpServletRequest request) throws Exception {
        BaseDTO baseDTO = new ObjectMapper().readValue((((HttpServerErrorException) ex).getResponseBodyAsString()), BaseDTO.class);
        return new ResponseEntity<>(baseDTO, ((HttpServerErrorException) ex).getStatusCode());

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public final ResponseEntity<BaseDTO> handleMissingParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        Object convertedFieldName = applicationProperties.getProperty(ex.getParameterName());
        BaseDTO baseDTO = new BaseDTO(new MetaDTO(
                applicationProperties.getCode("application.message.missingParameter.code"),
                String.format(
                        applicationProperties.getProperty("application.message.missingParameter.text"),
                        convertedFieldName == null ? ex.getParameterName() : convertedFieldName.toString()
                )
        ));
        return new ResponseEntity<>(baseDTO, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<BaseDTO> handleNotReadableExceptions(HttpMessageNotReadableException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(
                new MetaDTO(
                        applicationProperties.getCode("application.message.requestNotReadable.code"),
                        ex.getMessage() == null ? applicationProperties.getProperty("application.message.requestNotReadable.text") : ex.getMessage())
        );
        return new ResponseEntity<>(baseDTO, HttpStatus.BAD_REQUEST);
    }


    // --> Runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<BaseDTO> handleAllExceptions(RuntimeException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(
                new MetaDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage() != null ? ex.getMessage() : applicationProperties.getProperty("application.message.unknownError.text")
                )
        );
        return new ResponseEntity<>(baseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<BaseDTO> methodArgumentTypeMismatchExceptionExceptions(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        Object convertedFieldName = applicationProperties.getProperty(ex.getName());
        BaseDTO baseDTO = BaseDTO.builder()
                .meta(MetaDTO.builder()
                        .code(applicationProperties.getCode("application.message.wrong.parameter.code"))
                        .message(String.format(
                                applicationProperties.getProperty("application.message.wrong.parameter.text"),
                                convertedFieldName != null ? convertedFieldName : ex.getName()
                        )).build()
                ).build();
        return new ResponseEntity<>(baseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<BaseDTO> handleIllegalArgumentExceptions(IllegalArgumentException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(
                new MetaDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage() != null ? ex.getMessage() : applicationProperties.getProperty("application.message.unknownError.text")
                )
        );
        return new ResponseEntity<>(baseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<BaseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(
                new MetaDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getCause().getCause().getLocalizedMessage().split("\n")[1]
                )
        );
        return new ResponseEntity<>(baseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<BaseDTO> handleAccessExceptions(AccessDeniedException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(
                new MetaDTO(
                        applicationProperties.getCode("application.message.accessDenied.code"),
                        applicationProperties.getProperty("application.message.accessDenied.text")
                )
        );
        return new ResponseEntity<>(baseDTO, HttpStatus.FORBIDDEN);
    }

    // --> Server or machine errors
    @ExceptionHandler(Error.class)
    public final ResponseEntity<BaseDTO> handleAllExceptions(Error ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(
                new MetaDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage()
                )
        );
        return new ResponseEntity<>(baseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --> Server or machine errors
    @ExceptionHandler(HttpClientErrorException.class)
    public final ResponseEntity<BaseDTO> handleHttpClientErrorException(HttpClientErrorException ex, HttpServletRequest request) throws Exception {
        BaseDTO baseDTO = new ObjectMapper().readValue(ex.getStatusText(), BaseDTO.class);
        return new ResponseEntity<>(baseDTO, ex.getStatusCode());
    }

    @ExceptionHandler(IllegalStateException.class)
    public final ResponseEntity<BaseDTO> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
        BaseDTO baseDTO = new BaseDTO(new MetaDTO(
                applicationProperties.getCode("application.message.illegalState.code"),
                applicationProperties.getProperty("application.message.illegalState.text"))
        );
        return new ResponseEntity<>(baseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
