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
    Class<Table> tableClass = Table.class;
    Class<Column> columnClass = Column.class;
    Class<PrimaryKey> primaryKeyClass = PrimaryKey.class;

    public String getTableAnnotationValue() {
        return tclass.getAnnotation(Table.class).value();
    }

    public PrimaryKey getPrimaryKeyAnnotation(Field field) {
        return tclass.getAnnotation(PrimaryKey.class);
    }

    public String getColumnAnntationValue() {
        return tclass.getAnnotation(Column.class).value();
    }

    public Field[] getFields() {
        Field[] fields = tclass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

    public T getNewInstance() throws InstantiationException, IllegalAccessException {
        return tclass.newInstance();
    }

    public DAOImpl(Class<T> tclass) {
        this.tclass = tclass;
    }

    @Override
    public T save(T t) throws SQLException {
        String tableName = getTableAnnotationValue();
        try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
        ) {
            Field[] fields = getFields();
            String columns = Arrays.stream(fields).map(Field::getName).collect(Collectors.joining(", "));
            String values = Arrays.stream(fields).map(f -> {
                try {
                    if (!(f.getType().isPrimitive()
                            && f.getType() != boolean.class
                            && f.getType() != char.class
                            && f.getType() != void.class)) {
                        return "'" + f.get(t).toString() + "'";
                    } else {
                        return f.get(t).toString();
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.joining(", ")); // Сгенерировано по количеству полей
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                    tableName, columns, values);
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = stmt.getGeneratedKeys();
            resultSet.next();
            long primary_key = resultSet.getLong(1);
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

    @Override
    public T get(Serializable id) throws SQLException,
            InstantiationException, IllegalAccessException, NoSuchFieldException {
        String tableName = getTableAnnotationValue();
        T t = getNewInstance();
        Field[] fields = t.getClass().getDeclaredFields();


        String primaryKeyName = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(primaryKeyClass)) {
                primaryKeyName = field.getAnnotation(primaryKeyClass).value();
            }
        }
        try (Connection connection = Connector.getConnection(); Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select * from " + tableName + " where " + primaryKeyName + " = " + id)) {
            ResultSetMetaData rsmd = rs.getMetaData();
            rs.next();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Column column = fields[i].getAnnotation(columnClass);
                if (fields[i].isAnnotationPresent(columnClass)) {
                    int columnType = rsmd.getColumnType(rs.findColumn(column.value()));
                    switch (columnType) {
                        case 4:
                            fields[i].set(t, rs.getInt(column.value()));
                            break;
                        case 12:
                            fields[i].set(t, rs.getString(column.value()));
                            break;
                        case -1:
                            fields[i].set(t, rs.getString(column.value()));
                            break;
                        case 91:
                            fields[i].set(t, rs.getDate(column.value()));
                            break;
                        case 92:
                            fields[i].set(t, rs.getTime(column.value()));
                            break;
                        case 93:
                            fields[i].set(t, rs.getTimestamp(column.value()));
                            break;
                    }
                }
                if (fields[i].isAnnotationPresent(primaryKeyClass)) {
                    fields[i].set(t, rs.getLong(fields[i].getAnnotation(primaryKeyClass).value()));
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
            Field[] fields = tclass.getDeclaredFields();
            String values = Arrays.stream(fields).map(f -> {
                try {
                    f.setAccessible(true);
                    String name = f.getName() + " = ";
                    if (!(f.getType().isPrimitive()
                            && f.getType() != boolean.class
                            && f.getType() != char.class
                            && f.getType() != void.class)) {
                        return name + "'" + f.get(t).toString() + "'";
                    } else {
                        return name + f.get(t).toString();
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.joining(", "));
            long primaryKeyFieldValue = 0;
            String primaryKeyName = null;
            for (Field field : fields) {
                if (field.isAnnotationPresent(primaryKeyClass)) {
                    primaryKeyName = field.getAnnotation(primaryKeyClass).value();
                    primaryKeyFieldValue = (long) field.get(t);
                }
            }

            String sql = String.format("UPDATE %s SET %s WHERE %s = %s",
                    tableName, values, primaryKeyName, primaryKeyFieldValue);
            System.out.println(sql);
            stmt.executeUpdate(sql);

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
            int count = stmt.executeUpdate("delete from " + table + " where " + primaryKeyName + " = " + id);
            return count;
        }
    }


}
