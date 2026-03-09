package dao;

import annotations1.Column;
import annotations1.PrimaryKey;
import annotations1.Table;
import connection.Connector;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public interface DAO<T> {


    default T save(T t) throws SQLException {
        try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
        ) {
            //1.Поиск всех полей данного объекта и установка доступа к приватным полям через рефлексию
            Class<?> tclass = t.getClass();
            Class<PrimaryKey> primaryKeyClass = PrimaryKey.class;
            Table tableAnnotation = tclass.getAnnotation(Table.class);
            String tableName = tableAnnotation.value();
            Field[] fields = tclass.getDeclaredFields();
            for (Field field : fields)
                field.setAccessible(true);

            //2.Склеивание запроса SQL c использованием названия полей и их значений
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


            //3.Возвращение сгенерированного ключа
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = stmt.getGeneratedKeys();
            resultSet.next();
            long primary_key = resultSet.getLong(1);

            //4. Поиск первичного ключа по соответствующей аннотации и присвоение ему значения через рефлексию

            for (Field field : fields) {
                if (field.isAnnotationPresent(primaryKeyClass)) {
                    field.set(t, primary_key);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    default T get(Serializable id, T objectClass) throws SQLException,
            InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class<?> tclass = objectClass.getClass();
        Field[] fields = tclass.getDeclaredFields();
        Class<Table> tableClass = Table.class;
        Class<Column> columnClass = Column.class;
        Class<PrimaryKey> primaryKeyClass = PrimaryKey.class;
        Table table = tclass.getAnnotation(tableClass);
        String primaryKeyName = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(primaryKeyClass)) {
                primaryKeyName = field.getAnnotation(primaryKeyClass).value();
            }
        }
        try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select * from " + table.value() + " where " + primaryKeyName + " = " + id)) {
            rs.next();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Column column = fields[i].getAnnotation(columnClass);
                if (fields[i].isAnnotationPresent(columnClass)) {
                    if (fields[i].getType().getName().equals("java.lang.String")) {
                        fields[i].set(objectClass, rs.getString(column.value()));
                    } else {
                        fields[i].set(objectClass, rs.getLong(column.value()));
                    }
                }
                if (fields[i].isAnnotationPresent(primaryKeyClass)) {
                    fields[i].set(objectClass, rs.getLong(fields[i].getAnnotation(primaryKeyClass).value()));
                }
            }
        }
        return objectClass;
    }

    void update(T t) throws SQLException;

    default int delete(Serializable id, String table) throws SQLException {
        try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
        ) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, table);
            resultSet.next();
            String primaryKeyName = resultSet.getString("COLUMN_NAME");
            int count = stmt.executeUpdate("delete from " + table + " where " + primaryKeyName + " = " + id);
            return count;
        }
    }
}
