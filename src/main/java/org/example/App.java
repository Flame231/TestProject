package org.example;

import sqlQueries.QueryBuilder;
import sqlQueries.StringIterable;

import java.sql.SQLException;
import java.util.Iterator;

public class App {
    public static void main(String[] args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
    /*    DAO<Car> dao = new DAOImpl<Car>(Car.class);
        Car car = dao.get(3);
        System.out.println(car);*/
        String[] strings = {"string1", "string2", "string3", "string3", "string3", "string3", "string3", "string3", "string3", "string3"};


        StringIterable stringIterable = new StringIterable(strings);
        StringBuffer stringBuffer = new StringBuffer();


        Iterator<String> iterator = stringIterable.iterator();

        String query = new QueryBuilder(stringIterable, stringBuffer).getQuery();
        System.out.println(query);
    }
}
