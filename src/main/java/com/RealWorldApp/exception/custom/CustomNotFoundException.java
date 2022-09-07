package com.RealWorldApp.exception.custom;

import com.RealWorldApp.model.CustomError;

public class CustomNotFoundException extends BaseCustomException{
    public CustomNotFoundException(CustomError customError) {
        super(customError);
    }
}
