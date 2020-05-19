package org.saladframework.dao.util;


import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author CuiShiFeng
 * @date
 */
public class StringChecker {

    /**
     * 只允许包含：字母，数字，点(.)，下划线(_)
     */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("^[a-z0-9\\._]+$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern COLUMN_PATTERN = Pattern.compile("^[a-z0-9\\._\\*\\(\\)\\s*+\\-/]+$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern ORDER_BY_PATTERN = Pattern.compile("^([a-z0-9\\._]+\\s+(ASC|DESC))(\\s*,\\s*[a-z0-9\\._]+\\s+(ASC|DESC))*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);


    /**
     * 检测字母和数字
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public static boolean checkTableName(String tableName) throws Exception {
        if (tableName == null || tableName.length() == 0) {
            throw new Exception("表名：为空");
        }
        if (tableName.length() > 50) {
            throw new Exception("表名：长度超过50个字符");
        }

        tableName = tableName.trim();
        boolean result = VARIABLE_PATTERN.matcher(tableName).matches();
        if (!result) {
            throw new Exception("表名：'" + tableName + "' 不合法(只能是字母，数字，下划线，点)");
        }

        return true;
    }

    public static boolean checkColumn(String column) throws Exception {
        if (column == null || column.length() == 0) {
            throw new Exception("列名：为空");
        }
        if (column.length() > 50) {
            throw new Exception("列名：长度超过50个字符");
        }

        column = column.trim();
        if (column.equalsIgnoreCase("*")) {
            return true;
        }

        boolean result = COLUMN_PATTERN.matcher(column).matches();
        if (!result) {
            throw new Exception("列名：'" + column + "' 不合法(只能是字母，数字，下划线，点，*, +, -, /)");
        }

        return true;
    }

    /**
     * 检测orderby
     *
     * @param orderBy
     * @return
     * @throws Exception
     */
    public static boolean checkOrderBy(String orderBy) throws Exception {
        if (orderBy == null || orderBy.length() == 0) {
            return true;
        }
        if (orderBy.length() > 50) {
            throw new Exception("orderBY：长度超过50个字符");
        }

        orderBy = orderBy.trim();
        boolean result = ORDER_BY_PATTERN.matcher(orderBy).matches();
        if (!result) {
            throw new Exception("orderBY：'" + orderBy + "' 不合法(只能是字母，数字，下划线，点，* 再跟上 DESC|ASC结尾)");
        }

        return true;
    }

    public static boolean checkColumn(Class<?> cls, Map<String, Object>... maps) throws Exception {
        Map<String, String> columnMap = ClassHelper.getAllIgnoreCaseCN(cls);
        for (Map<String, Object> map : maps) {
            if (map == null) {
                continue;
            }

            Set<String> keys = map.keySet();
            for (String key : keys) {
                if (!columnMap.containsKey(key.toLowerCase())) {
                    throw new Exception("列名：'" + key + "' 不合法");
                }
            }
        }
        return true;
    }

    public static boolean checkColumn(Class<?> cls, String columns) throws Exception {
        columns = columns.trim();
        if (columns.equalsIgnoreCase("*")) {
            return true;
        }

        Map<String, String> columnMap = ClassHelper.getAllIgnoreCaseCN(cls);

        String[] aryCol = columns.split(",");
        for (String col : aryCol) {
            if (!columnMap.containsKey(col.toLowerCase())) {
                throw new Exception("列名：'" + col + "' 不合法");
            }
        }

        return true;
    }

}
