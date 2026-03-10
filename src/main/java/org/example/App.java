package org.example;

import dao.DAO;
import dao.DAOClass;
import dto.Test;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        DAO<Test> dao = new DAOClass();
        Test test = dao.get(3, new Test());
        System.out.println(test);
    }
}
