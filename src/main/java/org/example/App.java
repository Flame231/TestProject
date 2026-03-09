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
        Car car3 = (Car)dao.save(car);
        System.out.println(car.getId());
        Car car2 = new Car();
        dao.get(4,car2);
        System.out.println(car2);
    }
}
