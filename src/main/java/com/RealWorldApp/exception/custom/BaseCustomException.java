package com.RealWorldApp.exception.custom;

import com.RealWorldApp.model.CustomError;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BaseCustomException extends  Exception{

    private Map<String, CustomError> customErrorMap;

    public BaseCustomException(CustomError customError) {
        this.customErrorMap = new HashMap<>();
        this.customErrorMap.put("error", customError);
    }
}
