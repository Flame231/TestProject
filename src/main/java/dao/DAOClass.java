package dao;

import java.io.Serializable;
import java.sql.SQLException;

public class DAOClass implements DAO{


    @Override
    public void update(Object o) throws SQLException {

    }

    @Override
    public int delete(Serializable id) throws SQLException {
        return 0;
    }
}
