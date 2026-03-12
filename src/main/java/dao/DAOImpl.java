package dao;

import connection.Connector;
import myAnnotations.Column;
import myAnnotations.PrimaryKey;
import myAnnotations.Table;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DAOImpl<T> implements DAO<T> {

    Class<T> tclass;
    Class<Column> columnClass = Column.class;
    Class<PrimaryKey> primaryKeyClass = PrimaryKey.class;
    Class<?>[] primitiveTypes = {Byte.class, Short.class, Integer.class, Long.class};

    String saveQuery = "INSERT INTO %s (%s) VALUES (%s)";
    String getQuery = "SELECT * FROM %s WHERE %s = %s";
    String updateQuery = "UPDATE %s SET %s WHERE %s = %s";
    String deleteQuery = "DELETE FROM %s WHERE %s = %s";
    String quote = "'";
    String coma = ",";

    public String getTableAnnotationValue() {
        return tclass.getAnnotation(Table.class).value();
    }

    public Field[] getFields() {   //Получить массив полей и открыть доступ к ним
        Field[] fields = tclass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

    public T getNewInstance() throws InstantiationException, IllegalAccessException {   //Создать новый объект данного типа
        return tclass.newInstance();
    }

    public void getColumnValue(ResultSet rs, int columnType, Field field, T t) {

    }

    public DAOImpl(Class<T> tclass) {   //Конструктор передаёт тип класса dto с которым будем работать
        this.tclass = tclass;
    }

    @Override
    public T save(T t) throws SQLException {
        String tableName = getTableAnnotationValue();
        Field[] fields = getFields();//Получаем массив всех полей данного класса dto
        try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
        ) {
            String columns = Arrays.stream(fields).map(Field::getName).collect(Collectors.joining(coma));// Перебираем массив полей,
            // извлекаем их название и склеиваем
            //через запятую в одну строку
            String values = Arrays.stream(fields).map(f -> { //Перебираем массив значений полей и склеиваем в одну строку через запятую
                try {
                    for (Class<?> types : primitiveTypes) {//Проверка является ли значения поля числом
                        if (f.get(t).getClass() == types) {
                            return f.get(t).toString();
                        }
                    }
                    return quote + f.get(t).toString() + quote;//Добавить одинарные кавычки если значение - не число
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.joining(coma));
            String sql = String.format(saveQuery, tableName, columns, values); //Собрать запрос
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS); //Сохранить объект и вернуть его первичный ключ
            ResultSet resultSet = stmt.getGeneratedKeys();
            resultSet.next();
            long primary_key = resultSet.getLong(1);//Получить первичный ключ из объекта ResultSet (всегда индекс = "1")
            for (Field field : fields) {
                if (field.isAnnotationPresent(primaryKeyClass)) {
                    field.set(t, primary_key); //Установить первичный ключ нашему объекту
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    @Override
    public T get(Serializable id) throws SQLException,
            InstantiationException, IllegalAccessException, NoSuchFieldException {
        T t = getNewInstance(); //Создать новый объект указанного типа
        String tableName = getTableAnnotationValue(); // Извлекаем значение аннотации Table которое является названием таблицы в mySQL
        Field[] fields = getFields();
        String primaryKeyName = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(primaryKeyClass)) { //Перебираем поля нашего класса и ищем аннотацию PrimaryKey,
                // извлекаем её значение которое является названием ключа в mySQL
                primaryKeyName = field.getAnnotation(primaryKeyClass).value();
            }
        }
        String query = String.format(getQuery, tableName, primaryKeyName, id);
        try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            ResultSetMetaData rsmd = rs.getMetaData();
            rs.next();
            for (Field field : fields) {
                Column column = field.getAnnotation(columnClass);
                if (field.isAnnotationPresent(columnClass)) {
                    int columnType = rsmd.getColumnType(rs.findColumn(column.value()));
                    switch (columnType) {//в зависимости от типа получаемого из mySQL значения присваиваем в наш объект значение соответствующего типа
                        case 4:
                            field.set(t, rs.getInt(column.value()));
                            break;
                        case 12, -1:
                            field.set(t, rs.getString(column.value()));
                            break;
                        case 91:
                            field.set(t, rs.getDate(column.value()));
                            break;
                        case 92:
                            field.set(t, rs.getTime(column.value()));
                            break;
                        case 93:
                            field.set(t, rs.getTimestamp(column.value()));
                            break;
                    }
                }
                if (field.isAnnotationPresent(primaryKeyClass)) {
                    field.set(t, rs.getLong(field.getAnnotation(primaryKeyClass).value())); // Находим в объекте поле с аннотацией PrimaryKey
                    //и устанавливаем ей значение из колонки таблицы название которой равно значению аннотации PrimaryKey
                }
            }
        }
        return t;
    }

    @Override
    public void update(T t) throws SQLException {
        String tableName = getTableAnnotationValue();
        try (Connection connection = Connector.getConnection();
             Statement stmt = connection.createStatement();) {
            Field[] fields = getFields();
            String values = Arrays.stream(fields).map(f -> {
                try {
                    for (Class<?> types : primitiveTypes) {//Проверка является ли значения поля числом
                        if (f.get(t).getClass() == types) {
                            return f.get(t).toString();
                        }
                    }
                    return quote + f.get(t).toString() + quote;//Добавить одинарные кавычки если значение - не число
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.joining(coma));
            long primaryKeyFieldValue = 0;
            String primaryKeyName = null;
            for (Field field : fields) {
                if (field.isAnnotationPresent(primaryKeyClass)) {
                    primaryKeyName = field.getAnnotation(primaryKeyClass).value();
                    primaryKeyFieldValue = (long) field.get(t); //получаем значение первичного ключа из объекта для вставки в запрос
                }
            }
            String sql = String.format(updateQuery,
                    tableName, values, primaryKeyName, primaryKeyFieldValue);
            stmt.executeUpdate(sql); // выполнить обновление записи
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Serializable id, String table) throws SQLException {
        try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
        ) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, table);
            resultSet.next();
            String primaryKeyName = resultSet.getString("COLUMN_NAME");
            String query = String.format(deleteQuery, table, primaryKeyName, id);
            return stmt.executeUpdate(query);
        }
    }
}
