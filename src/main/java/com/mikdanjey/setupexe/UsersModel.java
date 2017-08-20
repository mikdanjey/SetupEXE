package com.mikdanjey.setupexe;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

class UsersModel {

    private static Validator validator;

    private static String reponseMessage = "No Message";

    static String validate(UsersEntity usersEntity){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UsersEntity>> constraintViolations = validator.validate(usersEntity);

        if(!constraintViolations.isEmpty()){
            String error = "";
            for(ConstraintViolation<UsersEntity> constraintViolation: constraintViolations){
                error += constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + "\n";
            }
            return error;
        }else{
            return reponseMessage;
        }

    }
}
