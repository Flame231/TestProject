package org.example;

import dao.DAO;
import dao.DAOClass;
import dao.ObjectClass;
import dto.Car;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        DAO dao = new DAOClass();
        Car car = new Car();
     /*   Car car2 = (Car)(dao.get(33,car));*/
        System.out.println(dao.delete(33, "car"));
    }
}
