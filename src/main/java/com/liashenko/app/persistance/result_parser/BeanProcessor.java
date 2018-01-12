package com.liashenko.app.persistance.result_parser;

import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialRef;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;

public abstract class BeanProcessor<T> {

    public T fillBeanWithResultData(ResultSet rs, Class<T> clazz) throws IllegalAccessException, InstantiationException, SQLException, NoSuchFieldException {
        T obj = clazz.newInstance();

        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    Annotation annotation = field.getAnnotation(Column.class);
                    Column columnAnnot = (Column) annotation;
                    String columnName = columnAnnot.name();

                    Field newObjField = obj.getClass().getField(field.getName());
                    newObjField.setAccessible(true);
                    setValue(obj, field, rs, columnName);
                }
            }
        }
        return obj;
    }

    private static <T> void setValue(T newInstanceObj, Field field, ResultSet rs, String columnName)
            throws IllegalAccessException, InstantiationException, SQLException {

        Class classType = field.getType();
        if (field.getType() == String.class) {
            field.set(newInstanceObj, rs.getString(columnName));
        } else if (classType == int.class || classType == Integer.class) {
            field.set(newInstanceObj, rs.getInt(columnName));
        } else if (classType == long.class || classType == Long.class) {
            field.set(newInstanceObj, rs.getLong(columnName));
        } else if (classType == float.class || classType == Float.class) {
            field.set(newInstanceObj, rs.getFloat(columnName));
        } else if (classType == short.class || classType == Short.class) {
            field.set(newInstanceObj, rs.getShort(columnName));
        } else if (classType == byte.class || classType == Byte.class) {
            field.set(newInstanceObj, rs.getByte(columnName));
        } else if (classType == boolean.class || classType == Boolean.class) {
            field.set(newInstanceObj, rs.getBoolean(columnName));
        } else if (classType == byte[].class || classType == Byte[].class) {
            field.set(newInstanceObj, rs.getBytes(columnName));
        } else if (classType == Timestamp.class) {
            field.set(newInstanceObj, rs.getTimestamp(columnName));
        } else if (classType == Array.class) {
            field.set(newInstanceObj, rs.getArray(columnName));
        } else if (classType == Time.class) {
           field.set(newInstanceObj, rs.getTime(columnName));
        } else if (classType == BigDecimal.class) {
           field.set(newInstanceObj, rs.getBigDecimal(columnName));
        } else if (classType == Blob.class) {
           field.set(newInstanceObj,  rs.getBlob(columnName));
        } else if (classType == SerialClob.class) {
            field.set(newInstanceObj, rs.getClob(columnName));
        } else if (classType == Date.class ) {
          field.set(newInstanceObj, rs.getDate(columnName));
        } else if (classType == SerialRef.class) {
            field.set(newInstanceObj, rs.getRef(columnName));
        } else {
            field.set(newInstanceObj, rs.getObject(columnName));
        }
    }
}
