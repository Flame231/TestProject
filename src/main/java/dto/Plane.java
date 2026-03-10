package dto;

import annotations1.Column;
import annotations1.PrimaryKey;
import annotations1.Table;

import java.sql.Time;

@Table("plane")
public class Plane {
    @PrimaryKey("id")
    long id;
    @Column("model")
    String model;
    @Column("type")
    String type;
    @Column("time")
    java.sql.Time time;

    @Override
    public String toString() {
        return "Plane{" +
                "model='" + model + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", id=" + id +
                '}';
    }

    public Plane(long id, String model, String type, String time) {
        this.id = id;
        this.model = model;
        this.type = type;
        this.time = Time.valueOf(time);
    }

    public Plane() {
    }
    public Plane(String model, String type, String time) {
        this.model = model;
        this.type = type;
        this.time = Time.valueOf(time);
    }
}
