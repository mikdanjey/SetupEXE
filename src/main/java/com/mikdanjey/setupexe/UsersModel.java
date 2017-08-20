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

    private static Map<String, Object> reponseMessage;

    private static Map<String, Object> validate(UsersEntity usersEntity) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UsersEntity>> constraintViolations = validator.validate(usersEntity);

        if (!constraintViolations.isEmpty()) {
            String error = "";
            for (ConstraintViolation<UsersEntity> constraintViolation : constraintViolations) {
                error += constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + "\n";
            }
            reponseMessage = new HashMap<>();
            reponseMessage.put("status", false);
            reponseMessage.put("message", error);
        } else {
            reponseMessage = new HashMap<>();
            reponseMessage.put("status", true);
        }
        return reponseMessage;
    }

    static String create(UsersEntity usersEntity) {
        Session session = Main.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Map<String, Object> reponseMessage = validate(usersEntity);
            if (Boolean.parseBoolean(String.valueOf(reponseMessage.get("status")))){
                session.save(usersEntity);
                transaction.commit();
                System.out.println("User Saved");
            }else {
                System.out.println(reponseMessage.get("message"));
            }

        } catch (HibernateException ex) {
            if (transaction != null)
                transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return "";
    }
}
