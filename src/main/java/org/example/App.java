package org.example;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

      DAOInt car = new Car();
      DAO dao = new DAOclass();

      dao.get(car);
    }
}
