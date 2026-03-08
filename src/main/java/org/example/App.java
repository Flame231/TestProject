package org.example;

import dao.*;
import dto.Car;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException, NoSuchFieldException {
        DAO dao = new DAOClass();
        Car car = new Car("car1","car2");
        dao.save(car);
        System.out.println(car.toString());
    }
}
