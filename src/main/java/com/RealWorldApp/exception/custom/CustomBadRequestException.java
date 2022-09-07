package com.RealWorldApp.exception.custom;

import com.RealWorldApp.model.CustomError;

public class CustomBadRequestException extends BaseCustomException{
    public CustomBadRequestException(CustomError customError) {
        super(customError);
    }
}
