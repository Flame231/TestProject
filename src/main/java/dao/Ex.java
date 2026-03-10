package dao;

import connection.Connector;

import java.sql.*;

public class Ex {
    public static void main(String[] args) {
        try(Connection connection = Connector.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from plane")){
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnType = resultSetMetaData.getColumnType(4);
            switch(columnType){
                case(4):
                    System.out.println("int!");
                    break;
                case(-5):
                    System.out.println("long!");
                    break;
                case(12):
                    System.out.println("String(varchar)");
                    break;
                case(91):
                    System.out.println("Date!");
                    break;
                case(92):
                    System.out.println("Time!");
                    break;
                case(-7):
                    System.out.println("boolean!");
                    break;
                case(93):
                    System.out.println("TimeStap!");
                    break;
                default:
                    System.out.println("nothing");
                    break;
            }
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
