package org.example;

import dao.DAO;
import dao.DAOImpl;
import dto.Car;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        DAO<Car> dao = new DAOImpl<Car>(Car.class);
        Car car = dao.save(new Car());
        System.out.println(car);
    }
}
