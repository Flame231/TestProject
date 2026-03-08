package dao;

import annotations1.Column;
import annotations1.Table;
import connection.Connector;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface DAO<T> {


    default ObjectClass save(ObjectClass t) throws SQLException {
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
            Column column = tclass.getAnnotation(Column.class);
            Arrays.stream(t.getClass().getDeclaredFields()) // 1. Берем все поля
                    .filter(f -> f.isAnnotationPresent(Column.class)) // 2. Фильтруем по аннотации
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

    default ObjectClass get(Serializable id, ObjectClass objectClass) throws SQLException,
            InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class<?> tclass = objectClass.getClass();
        Field[] fields =tclass.getDeclaredFields();
        List<Integer> list = new ArrayList<>();
        Table table = tclass.getAnnotation(Table.class);
        for(int i = 0; i< fields.length;i++){
            Column column = fields[i].getAnnotation(Column.class);
            list.add(column.value());
        }
           try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
           ResultSet rs = stmt.executeQuery("select * from " + table.value() + " where id = " + id)){
               rs.next();
               for(int i = 0; i< fields.length;i++){
                   fields[i].setAccessible(true);
                   Column column = fields[i].getAnnotation(Column.class);
                   if(fields[i].getType().getName().equals("java.lang.String")) {
                       fields[i].set(objectClass, rs.getString(column.value()));
                   }
                   else{
                       fields[i].set(objectClass, rs.getLong(column.value()));
                   }
               }

           }
        return objectClass;
    }

    void update(T t) throws SQLException;

    default int delete(Serializable id, String table) throws SQLException{
        try(Connection connection = Connector.getConnection();Statement stmt = connection.createStatement();
        ){
            int count = stmt.executeUpdate("delete from " + table + " where id = " + id);
            return count;
        }
    }
}
