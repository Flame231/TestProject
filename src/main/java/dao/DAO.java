package dao;

import annotations1.PrimaryKey;
import annotations1.Table;
import connection.Connector;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface DAO<T> {


    default T save(T t) throws SQLException {
        try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
        ) {
            Class<?> tclass = t.getClass();
            Table table = tclass.getAnnotation(Table.class);
            Field[] fields = tclass.getDeclaredFields();
            for (Field field : fields)
                field.setAccessible(true);
            String tableName = table.value(); // Получено из аннотации
            String columns = Arrays.stream(fields).map(Field::getName).collect(Collectors.joining(", "));
            String questions = Arrays.stream(fields).map(f -> {
                try {
                    return "'" + f.get(t).toString() + "'";
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.joining(", ")); // Сгенерировано по количеству полей
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                    tableName, columns, questions);
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            long primary_key = rs.getLong(1);
            PrimaryKey primaryKey = tclass.getAnnotation(PrimaryKey.class);
            Arrays.stream(t.getClass().getDeclaredFields()) // 1. Берем все поля
                    .filter(f -> f.isAnnotationPresent(PrimaryKey.class)) // 2. Фильтруем по аннотации
                    .findFirst() // 3. Берем первое найденное (возвращает Optional<Field>)
                    .ifPresent(field -> { // 4. Если нашли — выполняем действия
                        try {
                            field.setAccessible(true); // Открываем доступ к private
                            field.set(t, primary_key);  // Меняем значение
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Ошибка доступа к полю", e);
                        }
                    });
        }
        return t;
    }

   default T get(Serializable id) throws SQLException{
        T t;
        return t;
   };

    void update(T t) throws SQLException;

    int delete(Serializable id) throws SQLException;
}
