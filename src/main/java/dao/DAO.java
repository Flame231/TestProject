package dao;

import org.example.ClassAnn;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public interface DAO<DTOClass> {

    default void save(DAOInt t) throws SQLException {
        Class<? extends DAOInt> tClass = t.getClass();
        Field[] fields = tClass.getDeclaredFields();
        ClassAnn classAnn = tClass.getAnnotation(ClassAnn.class);
        try(Connection connection = Connector.getConnector();Statement stmt = connection.createStatement();){
            stmt.executeUpdate("insert into " + classAnn.value() + "VALUES ('" +  +"')");
        }
    }









    default DAOInt get(DAOInt daoInt, Serializable id) throws SQLException {
        Class<? extends DAOInt> tClass = daoInt.getClass();
        ClassAnn classAnn = tClass.getAnnotation(ClassAnn.class);
        Field[] fields = tClass.getDeclaredFields();

        try (Connection connection = Connector.getConnector();
             Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("select * from " + classAnn.value() + " where id = " + id)) {
            ResultSetMetaData rsm = rs.getMetaData();
            rs.next();
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                if (rsm.getColumnType(i) == 4)
                    fields[i - 1].set(daoInt, rs.getInt(i));
                if (rsm.getColumnType(i) == 12)
                    fields[i - 1].set(daoInt, rs.getString(i));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return daoInt;
    }


/*
    void update(myInt t) throws SQLException;

    int delete(Serializable id) throws SQLException;*/
}
