package dao;

import connection.Connector;
import dto.Car;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CarDaoImpl implements CarDao {


    public Car save(Car car) throws SQLException {
        try (Connection connection = Connector.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("INSERT INTO car (name,type) VALUES('" + car.getName() + "','" + car.getType() + "')",
                    Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            long primaryKey = rs.getLong(1);
            car.setId(primaryKey);
        }
        return car;
    }


    @Override
    public Car get(Serializable id) throws SQLException {
        Car car = new Car();
        try (Connection connection = Connector.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery("SELECT * FROM car WHERE id = " + id)) {
                while (rs.next()) {
                    car.setId(rs.getInt(1));
                    car.setType(rs.getString(2));
                    car.setName(rs.getString("name"));
                }
            }
        }
        return car;
    }


    @Override
    public void update(Car car) throws SQLException {

    }

    @Override
    public int delete(Serializable id) throws SQLException {
        return 0;
    }
}
