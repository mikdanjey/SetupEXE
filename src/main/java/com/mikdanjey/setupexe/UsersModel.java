package com.mikdanjey.setupexe;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class UsersModel {

    private static Map<String, Object> validate(UsersEntity usersEntity) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UsersEntity>> constraintViolations = validator.validate(usersEntity);

        Map<String, Object> reponseMessage;
        if (!constraintViolations.isEmpty()) {
            StringBuilder error = new StringBuilder();
            for (ConstraintViolation<UsersEntity> constraintViolation : constraintViolations) {
                error.append(constraintViolation.getPropertyPath()).append(" ").append(constraintViolation.getMessage()).append("\n");
            }
            reponseMessage = new HashMap<>();
            reponseMessage.put("status", false);
            reponseMessage.put("message", error.toString());
        } else {
            reponseMessage = new HashMap<>();
            reponseMessage.put("status", true);
        }
        return reponseMessage;
    }

    static String create(UsersEntity usersEntity) {
        Transaction transaction = null;
        String reponseMessage = null;
        try (Session session = Main.getSession()) {
            transaction = session.beginTransaction();
            Map<String, Object> responseMessage = validate(usersEntity);
            if (Boolean.parseBoolean(String.valueOf(responseMessage.get("status")))) {
                session.save(usersEntity);
                transaction.commit();
                reponseMessage = "User Saved";
            } else {
                reponseMessage = (String) responseMessage.get("message");
            }

        } catch (HibernateException ex) {
            if (transaction != null)
                transaction.rollback();
            ex.printStackTrace();
        }
        return reponseMessage;
    }
}
