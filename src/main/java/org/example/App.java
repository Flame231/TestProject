package org.example;

import dao.DAO;
import dao.DAOClass;
import dao.ObjectClass;
import dto.Car;
import dto.Plane;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        DAO dao = new DAOClass();
        Plane plane = new Plane("model1","type1");
        dao.save(new Plane("model1","type1"));
        dao.get(14,plane);
        System.out.println(plane);
    }
}
