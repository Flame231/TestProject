package dto;


import myAnnotations.Column;
import myAnnotations.PrimaryKey;
import myAnnotations.Table;

@Table("car")
public class Car {

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @PrimaryKey("id")
    private long id;

    public Car(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Car() {
        name = "name1222";
        type = "type1222";
    }

    @Column("name")
    private String name;
    @Column("type")
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
