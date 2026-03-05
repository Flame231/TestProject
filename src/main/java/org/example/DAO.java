package org.example;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;

public interface DAO {

   default void get(DAOInt t) throws NoSuchFieldException, IllegalAccessException {
        Class<? extends DAOInt> tClass = t.getClass();
       ClassAnn classAnn = tClass.getAnnotation(ClassAnn.class);


    }

/*    T save(T t) throws SQLException;

    T get(Serializable id) throws SQLException;

    void update(T t) throws SQLException;

    int delete(Serializable id) throws SQLException;*/
}
