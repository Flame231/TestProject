package dto;

import annotations1.Column;
import annotations1.PrimaryKey;
import annotations1.Table;

@Table("plane")
public class Plane {
    @PrimaryKey("id")
    long id;
    @Column("type")
    String type;
    @Column("model")
    String model;

    public Plane(String model, String type) {
        this.model = model;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", type='" + type + '\'' +
                '}';
    }


}
