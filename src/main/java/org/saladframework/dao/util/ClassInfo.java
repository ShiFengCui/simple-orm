package org.saladframework.dao.util;


import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.saladframework.dao.annotation.*;

/**
 * @author CuiShiFeng
 * @date
 */
class ClassInfo {

    /**
     * key: field.getName()  value:field
     */
    private Map<String, Field> mapIDField;

    /**
     * key: field.getName()  value:field
     */
    private Map<String, Field> mapAllDBField;

    /**
     * key: field.getName()  value:field
     */
    private Map<String, Field> mapInsertableField;

    /**
     * key: field.getName()  value:field
     */
    private Map<String, Field> mapUpdatableField;

    /**
     * key: field.getName()  value:field
     */
    private Map<String, Field> mapIdentityField;

    /**
     * key: field.getName()  value:db column name
     */
    private Map<String, String> mapDBColumnName;

    /**
     * 忽略大写的列名
     */
    private Map<String, String> mapIgnoreCaseCN;

    /**
     * key: field.getName()  value:Method
     */
    private Map<String, Method> mapSetMethod;

    /**
     * key: field.getName()  value:Method
     */
    private Map<String, Method> mapGetMethod;


    private String tableName;

    private ProcedureName procdure;


    public ClassInfo(Class<?> clazz) throws Exception {
        this.setTableName(getTableName(clazz));
        this.setMapIDField(getIdFields(clazz));
        this.setMapAllDBField(getAllDBFields(clazz));
        this.setMapInsertableField(getInsertableFields(clazz));
        this.setMapUpdatableField(getUpdatableFields(clazz));
        this.setMapDBColumnName(getCloumnName(clazz));
        this.setMapSetMethod(getSetterMethod(clazz));
        this.setMapGetMethod(getGetterMethod(clazz));
        this.setProcdure(getProc(clazz));
        this.setMapIdentityField(getIdentityFields(clazz));

        initIgnoreCaseCN(this.getMapDBColumnName());
    }

    private void initIgnoreCaseCN(Map<String, String> map) {
        mapIgnoreCaseCN = new HashMap<String, String>();
        Collection<String> values = map.values();
        for (String value : values) {
            mapIgnoreCaseCN.put(value.toLowerCase(), "");
        }
    }

    /**
     * 获得表名
     *
     * @param clazz
     * @return
     */
    private String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            if (!table.name().equalsIgnoreCase("className")) {
                return table.name();
            }
        }
        String name = clazz.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * 获得所有允许插入的字段
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getInsertableFields(Class<?> clazz) {
        Map<String, Field> mapFfields = new HashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(NotDBColumn.class) || Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            if (f.isAnnotationPresent(Id.class)) {
                Id id = (Id) f.getAnnotation(Id.class);
                if (id.insertable()) {
                    mapFfields.put(f.getName(), f);
                }
            } else {
                Column column = (Column) f.getAnnotation(Column.class);
                if (column != null) {
                    if (!column.defaultDBValue()) {
                        mapFfields.put(f.getName(), f);
                    }
                } else {
                    mapFfields.put(f.getName(), f);
                }
            }
        }
        return mapFfields;
    }


    /**
     * 获得所有字段
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getAllDBFields(Class<?> clazz) {
        Map<String, Field> mapFfields = new HashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(NotDBColumn.class) || Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            mapFfields.put(f.getName(), f);
        }
        return mapFfields;
    }

    /**
     * 获得主键字段
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getIdFields(Class<?> clazz) {
        Map<String, Field> mapFfields = new HashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Id.class)) {
                mapFfields.put(f.getName(), f);
            }
        }
        return mapFfields;
    }

    /**
     * 获得所有允许更新的字段
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getUpdatableFields(Class<?> clazz) {
        Map<String, Field> mapFfields = new HashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(NotDBColumn.class) || Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            if (f.isAnnotationPresent(Id.class)) {
                Id id = (Id) f.getAnnotation(Id.class);
                if (id.updatable()) {
                    mapFfields.put(f.getName(), f);
                }
            } else {
                Column column = (Column) f.getAnnotation(Column.class);
                if (column != null) {
                    if (!column.defaultDBValue()) {
                        mapFfields.put(f.getName(), f);
                    }
                } else {
                    mapFfields.put(f.getName(), f);
                }
            }
        }
        return mapFfields;
    }

    /**
     * 获得字段名
     *
     * @param clazz
     * @return
     */
    private Map<String, String> getCloumnName(Class<?> clazz) {
        Map<String, String> mapNames = new HashMap<String, String>();
        Collection<Field> fList = mapAllDBField.values();
        for (Field f : fList) {
            if (f.isAnnotationPresent(Column.class)) {
                Column column = (Column) f.getAnnotation(Column.class);
                if (!column.name().equalsIgnoreCase("fieldName")) {
                    mapNames.put(f.getName(), column.name());
                } else {
                    mapNames.put(f.getName(), f.getName());
                }
            } else {
                mapNames.put(f.getName(), f.getName());
            }
        }
        return mapNames;
    }


    /**
     * 获得字段对应的set方法
     */
    private Map<String, Method> getSetterMethod(Class<?> clazz) throws Exception {
        Map<String, Method> mapMethod = new HashMap<String, Method>();
        Collection<Field> fList = mapAllDBField.values();
        PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(clazz, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();

        for (Field f : fList) {
            for (PropertyDescriptor aLPropDesc : lPropDesc) {
                if (aLPropDesc.getName().equalsIgnoreCase(f.getName())) {
                    Method setMethod = aLPropDesc.getWriteMethod();
                    mapMethod.put(f.getName(), setMethod);
                    break;
                }
            }
        }

        for (Field f : fList) {
            Method setterMethod = mapMethod.get(f.getName());
            if (setterMethod == null) {
                String setFunName = "";
                if (f.isAnnotationPresent(Column.class)) {
                    Column column = (Column) f.getAnnotation(Column.class);
                    if (!column.setFuncName().equalsIgnoreCase("setField")) {
                        setFunName = column.setFuncName();
                    } else {
                        setFunName = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    }
                } else {
                    setFunName = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                }

                for (Method m : clazz.getMethods()) {
                    if (m.getName().equals(setFunName)) {
                        setterMethod = m;
                        break;
                    }
                }

                mapMethod.put(f.getName(), setterMethod);
            }
        }

        for (Field f : fList) {
            Method setterMethod = mapMethod.get(f.getName());
            if (setterMethod == null) {
                throw new Exception("can't find set method field:" + f.getName() + "  class:" + clazz.getName());
            }
        }

        return mapMethod;
    }

    /**
     * 获得字段对应的get方法
     */
    private Map<String, Method> getGetterMethod(Class<?> clazz) throws Exception {
        Map<String, Method> mapMethod = new HashMap<String, Method>();
        Collection<Field> fList = mapAllDBField.values();
        PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(clazz, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();

        for (Field f : fList) {
            for (PropertyDescriptor aLPropDesc : lPropDesc) {
                if (aLPropDesc.getName().equalsIgnoreCase(f.getName())) {
                    Method getMethod = aLPropDesc.getReadMethod();
                    mapMethod.put(f.getName(), getMethod);
                    break;
                }
            }
        }

        for (Field f : fList) {
            Method getterMethod = mapMethod.get(f.getName());
            String getFunName = "";
            if (f.isAnnotationPresent(Column.class)) {
                Column column = (Column) f.getAnnotation(Column.class);
                if (!column.getFuncName().equalsIgnoreCase("getField")) {
                    getFunName = column.getFuncName();
                } else {
                    getFunName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                }
            } else {
                getFunName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
            }

            for (Method m : clazz.getMethods()) {
                if (m.getName().equals(getFunName)) {
                    getterMethod = m;
                    break;
                }
            }
            mapMethod.put(f.getName(), getterMethod);
        }

        for (Field f : fList) {
            Method getterMethod = mapMethod.get(f.getName());
            if (getterMethod == null) {
                throw new Exception("can't find get method field:" + f.getName() + "  class:" + clazz.getName());
            }
        }

        return mapMethod;
    }

    private ProcedureName getProc(Class<?> clazz) {
        return clazz.getAnnotation(ProcedureName.class);
    }

    /*
     * 获得自增字段
     */
    private Map<String, Field> getIdentityFields(Class<?> clazz) {
        Map<String, Field> mapField = new HashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Id.class)) {
                Id id = (Id) f.getAnnotation(Id.class);
                if (!id.insertable() && !id.updatable()) {
                    mapField.put(f.getName(), f);
                }
            }
        }
        return mapField;
    }


    public Map<String, Field> getMapIDField() {
        return mapIDField;
    }

    public void setMapIDField(Map<String, Field> mapIDField) {
        this.mapIDField = mapIDField;
    }

    public Map<String, Field> getMapAllDBField() {
        return mapAllDBField;
    }

    public void setMapAllDBField(Map<String, Field> mapAllDBField) {
        this.mapAllDBField = mapAllDBField;
    }

    public Map<String, Field> getMapInsertableField() {
        return mapInsertableField;
    }

    public void setMapInsertableField(Map<String, Field> mapInsertableField) {
        this.mapInsertableField = mapInsertableField;
    }

    public Map<String, Field> getMapUpdatableField() {
        return mapUpdatableField;
    }

    public void setMapUpdatableField(Map<String, Field> mapUpdatableField) {
        this.mapUpdatableField = mapUpdatableField;
    }

    public Map<String, String> getMapDBColumnName() {
        return mapDBColumnName;
    }

    public void setMapDBColumnName(Map<String, String> mapDBColumnName) {
        this.mapDBColumnName = mapDBColumnName;
    }

    public Map<String, Method> getMapSetMethod() {
        return mapSetMethod;
    }

    public void setMapSetMethod(Map<String, Method> mapSetMethod) {
        this.mapSetMethod = mapSetMethod;
    }

    public Map<String, Method> getMapGetMethod() {
        return mapGetMethod;
    }

    public void setMapGetMethod(Map<String, Method> mapGetMethod) {
        this.mapGetMethod = mapGetMethod;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setProcdure(ProcedureName procdure) {
        this.procdure = procdure;
    }

    public ProcedureName getProcdure() {
        return procdure;
    }

    public void setMapIdentityField(Map<String, Field> mapIdentityField) {
        this.mapIdentityField = mapIdentityField;
    }

    public Map<String, Field> getMapIdentityField() {
        return mapIdentityField;
    }


    public void setMapIgnoreCaseCN(Map<String, String> mapIgnoreCaseCN) {
        this.mapIgnoreCaseCN = mapIgnoreCaseCN;
    }


    public Map<String, String> getMapIgnoreCaseCN() {
        return mapIgnoreCaseCN;
    }
}