package dao;

import org.example.ClassAnn;

@ClassAnn("car")
public class Car implements DAOInt{
    int id = 0;
    String name = null;
    String model = null;

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
