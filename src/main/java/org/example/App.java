package org.example;

import dao.DAO;
import dao.DAOClass;
import dto.Plane;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        DAO<Plane> dao = new DAOClass();
        Plane plane = new Plane("model2!", "type2!", "11:20:20");
        Plane plane1 = new Plane(8,"model2!", "type2!", "12:21:30");
        dao.update(plane1);
        /*  Plane plane2 = dao.get(8,new Plane());*/
        /*      Plane plane1 = dao.get(14,plane);*/

    }
}
