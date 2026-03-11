package dto;

import myAnnotations.Column;
import myAnnotations.PrimaryKey;
import myAnnotations.Table;

import java.sql.Time;
import java.sql.Timestamp;

@Table("test")
public class Test {
    @PrimaryKey("id")
    long id;
    @Column("name")
    String name;
    @Column("description")
    String description;
    @Column("price")
    int price;
    @Column("quantity")
    int quantity;
    @Column("is_active")
    boolean is_active;
    @Column("created_at")
    java.sql.Timestamp created_at;

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", is_active=" + is_active +
                ", created_at=" + created_at +
                ", event_time=" + event_time +
                '}';
    }

    @Column("event_time")
    java.sql.Time event_time;

    public Test(long id, String name, String description, int price, int quantity, boolean is_active, Timestamp created_at, Time event_time) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.is_active = is_active;
        this.created_at = created_at;
        this.event_time = event_time;
    }

    public Test() {
    }
}
