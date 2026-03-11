package dao;

import java.io.Serializable;
import java.sql.SQLException;

public interface DAO<T> {
    T save(T t) throws SQLException;

    T get(Serializable id) throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException;

    void update(T t) throws SQLException;

    int delete(Serializable id, String table) throws SQLException;
}
