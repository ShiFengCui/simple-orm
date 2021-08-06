package org.saladframework.dao.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.saladframework.dao.annotation.Column;
import org.saladframework.dao.annotation.ProcedureName;

/**
 * @author CuiShiFeng
 * @date
 */
public class ClassHelper {

    private static Map<Class<?>, ClassInfo> classInfoCache = new HashMap<Class<?>, ClassInfo>();

    public static Map<Class<?>, ClassInfo> getAllClassInfo() {
        return classInfoCache;
    }


    private static ClassInfo getClassInfo(Class<?> clazz) {
        ClassInfo ci = classInfoCache.get(clazz);
        if (ci == null) {
            try {
                ci = new ClassInfo(clazz);
                classInfoCache.put(clazz, ci);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return ci;
    }


    public static ProcedureName getProcedureName(Class<?> clazz) {
        return getClassInfo(clazz).getProcdure();
    }

    /**
     * 获得字段对应的set方法
     */
    public static Method getSetterMethod(Class<?> clazz, Field field) throws Exception {
        ClassInfo ci = getClassInfo(clazz);
        Map<String, Method> mapSetterMethod = ci.getMapSetMethod();
        return mapSetterMethod.get(field.getName());
    }

    /**
     * 获得字段对应的get方法
     */
    public static Method getGetterMethod(Class<?> clazz, Field field) {
        ClassInfo ci = getClassInfo(clazz);
        Map<String, Method> mapGetterMethod = ci.getMapGetMethod();
        return mapGetterMethod.get(field.getName());
    }

    /**
     * 获得主键字段
     */
    public static List<Field> getIdFields(Class<?> clazz) {
        ClassInfo ci = getClassInfo(clazz);
        Collection<Field> coll = ci.getMapIDField().values();
        List<Field> fields = new ArrayList<Field>();
        for (Field f : coll) {
            fields.add(f);
        }
        return fields;
    }

    /**
     * 获得所有字段
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        ClassInfo ci = getClassInfo(clazz);
        Collection<Field> coll = ci.getMapAllDBField().values();
        List<Field> fields = new ArrayList<Field>();
        for (Field f : coll) {
            fields.add(f);
        }
        return fields;
    }

    /**
     * 获得所有允许插入的字段
     */
    public static List<Field> getInsertableFields(Class<?> clazz) {
        ClassInfo ci = getClassInfo(clazz);
        Collection<Field> coll = ci.getMapInsertableField().values();
        List<Field> fields = new ArrayList<Field>();
        for (Field f : coll) {
            fields.add(f);
        }
        return fields;
    }

    /**
     * 获得所有允许更新的字段
     */
    public static List<Field> getUpdatableFields(Class<?> clazz) {
        ClassInfo ci = getClassInfo(clazz);
        Collection<Field> coll = ci.getMapUpdatableField().values();
        List<Field> fields = new ArrayList<Field>();
        for (Field f : coll) {
            fields.add(f);
        }
        return fields;
    }

    public static String getTableName(Class<?> clazz) {
        return getClassInfo(clazz).getTableName();
    }

    public static Map<String, String> getAllDBCloumnName(Class<?> clazz) {
        ClassInfo ci = getClassInfo(clazz);
        return ci.getMapDBColumnName();
    }

    public static Map<String, String> getAllIgnoreCaseCN(Class<?> clazz) {
        ClassInfo ci = getClassInfo(clazz);
        return ci.getMapIgnoreCaseCN();
    }

    /**
     * 获得字段名
     */
    public static String getDBCloumnName(Class<?> clazz, Field f) {
        ClassInfo ci = getClassInfo(clazz);
        return ci.getMapDBColumnName().get(f.getName());
    }

    /**
     * 获得自增字段
     */
    public static List<Field> getIdentityFields(Class<?> clazz) {
        ClassInfo ci = getClassInfo(clazz);
        Collection<Field> coll = ci.getMapIdentityField().values();
        List<Field> fields = new ArrayList<Field>();
        for (Field f : coll) {
            fields.add(f);
        }
        return fields;
    }

    /**
     * 获得ProcedureName
     *
     * @param clazz
     * @return
     */
    public static ProcedureName getProc(Class<?> clazz) {
        return getClassInfo(clazz).getProcdure();
    }


    /**
     * 是否使用数据库默认值
     *
     * @param f
     * @return
     */
    public static boolean defaultDBValue(Field f) {
        if (f.isAnnotationPresent(Column.class)) {
            Column column = (Column) f.getAnnotation(Column.class);
            return column.defaultDBValue();
        }
        return false;
    }


    /**
     * @param ps
     * @param value
     * @param columnIndex
     * @return
     * @throws Exception
     */
    public static void setPara(PreparedStatement ps, Object value, int columnIndex) throws Exception {
        if (value != null) {
            Class<?> valueType = value.getClass();
            if (valueType.equals(String.class)) {
                ps.setString(columnIndex, value.toString());
            } else if (valueType.equals(int.class) || valueType.equals(Integer.class)) {
                ps.setInt(columnIndex, Integer.parseInt(value.toString(), 10));
            } else if (valueType.equals(long.class) || valueType.equals(Long.class)) {
                ps.setLong(columnIndex, Long.parseLong(value.toString()));
            } else if (valueType.equals(short.class) || valueType.equals(Short.class)) {
                ps.setShort(columnIndex, Short.parseShort(value.toString()));
            } else if (valueType.equals(Date.class)) {
                ps.setTimestamp(columnIndex, new Timestamp(((Date) value).getTime()));
            } else if (valueType.equals(boolean.class) || valueType.equals(Boolean.class)) {
                ps.setBoolean(columnIndex, Boolean.parseBoolean(value.toString()));
            } else if (valueType.equals(double.class) || valueType.equals(Double.class)) {
                ps.setDouble(columnIndex, Double.parseDouble(value.toString()));
            } else if (valueType.equals(float.class) || valueType.equals(Float.class)) {
                ps.setFloat(columnIndex, Float.parseFloat(value.toString()));
            } else if (valueType.equals(byte.class) || valueType.equals(Byte.class)) {
                ps.setByte(columnIndex, Byte.parseByte(value.toString()));
            } else if (valueType.equals(byte[].class) || valueType.equals(Byte[].class)) {
                ps.setBytes(columnIndex, (byte[]) value);
            } else if (valueType.equals(BigDecimal.class)) {
                ps.setBigDecimal(columnIndex, new BigDecimal(value.toString()));
            } else if (valueType.equals(Timestamp.class)) {
                ps.setTimestamp(columnIndex, (Timestamp) value);
            } else if (valueType.equals(java.sql.Date.class)) {
                ps.setTimestamp(columnIndex, new Timestamp(((java.sql.Date) value).getTime()));
            } else {
                ps.setObject(columnIndex, value);
            }
        } else {
            ps.setObject(columnIndex, null);
        }
    }


    /**
     * @param cstmt
     * @param bean
     * @param f
     * @return
     * @throws Exception
     */
    public static CallableStatement setPara(CallableStatement cstmt, Object bean, Field f) throws Exception {
        Class<?> clazz = bean.getClass();
        Method m = getGetterMethod(clazz, f);
        if (m == null) {
            System.out.println("method is null fn:" + f.getName() + "---" + bean.toString());
        }
        Object value = m.invoke(bean, new Object[]{});
        if (value != null) {
            String columnName = getDBCloumnName(clazz, f);
            Class<?> valueType = m.getReturnType();

            if (valueType.equals(String.class)) {
                cstmt.setString(columnName, value.toString());
            } else if (valueType.equals(BigDecimal.class)) {
                cstmt.setBigDecimal(columnName, new BigDecimal(value.toString()));
            } else if (valueType.equals(int.class) || valueType.equals(Integer.class)) {
                cstmt.setInt(columnName, Integer.parseInt(value.toString(), 10));
            } else if (valueType.equals(boolean.class) || valueType.equals(Boolean.class)) {
                cstmt.setBoolean(columnName, Boolean.parseBoolean(value.toString()));
            } else if (valueType.equals(double.class) || valueType.equals(Double.class)) {
                cstmt.setDouble(columnName, Double.parseDouble(value.toString()));
            } else if (valueType.equals(long.class) || valueType.equals(Long.class)) {
                cstmt.setLong(columnName, Long.parseLong(value.toString()));
            } else if (valueType.equals(float.class) || valueType.equals(Float.class)) {
                cstmt.setFloat(columnName, Float.parseFloat(value.toString()));
            } else if (valueType.equals(short.class) || valueType.equals(Short.class)) {
                cstmt.setShort(columnName, Short.parseShort(value.toString()));
            } else if (valueType.equals(byte.class) || valueType.equals(Byte.class)) {
                cstmt.setByte(columnName, Byte.parseByte(value.toString()));
            } else if (valueType.equals(byte[].class) || valueType.equals(Byte[].class)) {
                cstmt.setBytes(columnName, (byte[]) value);
            } else if (valueType.equals(Timestamp.class)) {
                cstmt.setTimestamp(columnName, (Timestamp) value);
            } else if (valueType.equals(Date.class)) {
                cstmt.setTimestamp(columnName, new Timestamp(((Date) value).getTime()));
            } else if (valueType.equals(java.sql.Date.class)) {
                cstmt.setTimestamp(columnName, new Timestamp(((java.sql.Date) value).getTime()));
            } else {
                throw new Exception("not define column type in WWW58COM.Utility.DAO.Utility.CallableStatement  Class:" + bean.getClass().getName() + " Method:" + f.getName());
            }
        } else {
            String columnName = getDBCloumnName(clazz, f);
            Class<?> valueType = m.getReturnType();

            if (valueType.equals(String.class)) {
                cstmt.setString(columnName, "");
            } else if (valueType.equals(BigDecimal.class)) {
                cstmt.setBigDecimal(columnName, new BigDecimal("0"));
            } else if (valueType.equals(int.class) || valueType.equals(Integer.class)) {
                cstmt.setInt(columnName, 0);
            } else if (valueType.equals(boolean.class) || valueType.equals(Boolean.class)) {
                cstmt.setBoolean(columnName, false);
            } else if (valueType.equals(double.class) || valueType.equals(Double.class)) {
                cstmt.setDouble(columnName, 0.0);
            } else if (valueType.equals(long.class) || valueType.equals(Long.class)) {
                cstmt.setLong(columnName, 0);
            } else if (valueType.equals(float.class) || valueType.equals(Float.class)) {
                cstmt.setFloat(columnName, (float) 0.0);
            } else if (valueType.equals(short.class) || valueType.equals(Short.class)) {
                cstmt.setShort(columnName, (short) 0);
            } else if (valueType.equals(byte.class) || valueType.equals(Byte.class)) {
                cstmt.setByte(columnName, (byte) 0);
            } else if (valueType.equals(byte[].class) || valueType.equals(Byte[].class)) {
                cstmt.setBytes(columnName, null);
            } else if (valueType.equals(Timestamp.class)) {
                cstmt.setTimestamp(columnName, new Timestamp(new java.util.Date().getTime()));
            } else if (valueType.equals(Date.class)) {
                cstmt.setTimestamp(columnName, new Timestamp(new java.util.Date().getTime()));
            } else if (valueType.equals(java.sql.Date.class)) {
                cstmt.setTimestamp(columnName, new Timestamp(new java.util.Date().getTime()));
            } else {
                throw new Exception("not define column type in WWW58COM.Utility.DAO.Utility.CallableStatement  Class:" + bean.getClass().getName() + " Method:" + f.getName());
            }
        }
        return cstmt;
    }


    public static String getValue(Object bean, Method method) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retValue = "";

        Object valueObj = method.invoke(bean, new Object[]{});
        Class<?> valueType = method.getReturnType();
        if (valueType.equals(String.class)) {
            if (valueObj == null) {
                retValue = "''";
            } else {
                retValue = "'" + valueObj.toString() + "'";
            }
        } else if (valueType.equals(Timestamp.class)
                || valueType.equals(java.sql.Date.class)
                || valueType.equals(Date.class)) {
            if (valueObj == null) {
                retValue = "''";
            } else {
                retValue = "'" + sdf.format(valueObj) + "'";
            }
        } else if (valueType.equals(boolean.class)
                || valueType.equals(Boolean.class)) {
            if (valueObj != null && valueObj.toString().equalsIgnoreCase("true")) {
                retValue = "1";
            } else {
                retValue = "0";
            }
        } else {
            if (valueObj == null) {
                retValue = "null";
            } else {
                retValue = valueObj.toString();
            }
        }

        return retValue;
    }

    public static String getValue(Object value) throws Exception {
        String retValue = "";
        Class<?> clazz = value.getClass();
        if (clazz == String.class ||
                clazz == Date.class ||
                clazz == java.sql.Date.class) {
            retValue = "'" + value.toString() + "'";
        } else {
            retValue = value.toString();
        }

        return retValue;
    }
}