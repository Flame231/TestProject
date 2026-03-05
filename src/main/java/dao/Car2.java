package dao;

import org.example.ClassAnn;

@ClassAnn("car2")
public class Car2 implements DAOInt{
    int id = 0;
    String name = null;
    String model = null;

    @Override
    public String toString() {
        return "Car2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
