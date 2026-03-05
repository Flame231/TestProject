package org.example;

import dao.Car;
import dao.Car2;
import dao.DAO;
import dto.DAOClass;

import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException {
        DAO dao = new DAOClass();
        Car2 car = new Car2();
        System.out.println(car + "До get");
        System.out.println( dao.get(new Car(),2));

    }
}
