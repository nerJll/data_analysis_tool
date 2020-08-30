package com.ner.utils;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: jianglinle
 * @date: 18.9.4 15:25
 * @description: 批量处理返回sql
 */
public class SqlUtil {

    /**
     * 功能描述：返回批量插入sql语句
     * 注意：T中必须有TableName和Column注解，主键必须名id
     *
     * @param list
     * @param t1
     * @return java.lang.String
     * @author jianglinle
     * @date 18.9.4 15:28
     */
    public static <T> String getInsertSql(List<T> list, Class<T> t1) throws Exception {
        /** 拼接sql语句 */
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ");
        /** 获取类名 */
        Class c = Class.forName(t1.getName());
        /** 获取Table注解属性 */
        TableName table = (TableName) c.getAnnotation(TableName.class);
        sb.append(table.value()).append(" (id,");
        /** 获取方法 */
        Field[] fields = c.getDeclaredFields();
        List<String> methods = new ArrayList<>(fields.length);
        methods.add("getId");
        /** 拼接字段 */
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            /** 获取Column注解 */
            TableField column = fields[i].getAnnotation(TableField.class);
            if (null == column) {
                continue;
            }
            sb.append(column.value()).append(i == fields.length - 1 ? ") VALUES " : ",");
            methods.add("get" + fields[i].getName().substring(0, 1).toUpperCase() +
                    fields[i].getName().substring(1));
        }
        /** 拼接值 */
        for (int j = 0; j < list.size(); j++) {
            T t = list.get(j);
            sb.append("(");
            Method method;
            for (int i = 0; i < methods.size(); i++) {
                /** 反射get方法 */
                method = t.getClass().getMethod(methods.get(i), null);
                String type = method.getAnnotatedReturnType().getType().toString();

                // 如果type是类类型，则前面包含"class "，后面跟类名
                Object object = method.invoke(t, null);
                if (object == null) {
                    sb.append("null").append(i == methods.size() - 1 ? ")" : ",");
                    continue;
                }
                if (type.endsWith("Long")) {
                    Long value = (Long) object;
                    sb.append(value).append(i == methods.size() - 1 ? ")" : ",");
                } else if (type.endsWith("String")) {
                    String value = ((String) object).replaceAll("'", "\"");
                    sb.append("'").append(value).append("'").append(i == methods.size() - 1 ? ")" : ",");
                } else if (type.endsWith("Integer")) {
                    Integer value = (Integer) object;
                    sb.append("'").append(value).append("'").append(i == methods.size() - 1 ? ")" : ",");
                } else if (type.endsWith("Boolean")) {
                    Boolean value = (Boolean) object;
                    sb.append(value).append(i == methods.size() - 1 ? ")" : ",");
                } else if (type.endsWith("Date")) {
                    Date value = (Date) object;
                    sb.append("'").append(DateUtil.formatDate(value)).append("'").append(i == methods.size() - 1 ? ")" : ",");
                } else {
                    sb.append("'").append(object.toString()).append("'").append(i == methods.size() - 1 ? ")" : ",");
                }

            }
            sb.append(j == list.size() - 1 ? "" : ",");
        }
        return sb.toString();
    }

//    /**
//     * 功能描述：返回批量更新sql语句
//     * 注意：T中必须有Table和Column注解，主键必须名id
//     *
//     * @param list
//     * @param t1
//     * @return java.lang.String
//     * @author jianglinle
//     * @date 18.9.4 15:28
//     */
//    public static <T> String getUpdateSql(List<T> list, Class<T> t1) throws Exception {
//        /** sql语句 */
//        StringBuffer sb = new StringBuffer();
//        sb.append("UPDATE ");
//        //反射类和注解 */
//        Class c = Class.forName(t1.getName());
//        Table table = (Table) c.getAnnotation(Table.class);
//        sb.append(table.name()).append(" SET ");
//        /** 字段 */
//        Field[] fields = c.getDeclaredFields();
//        /** ids */
//        List<String> ids = new ArrayList<>(list.size());
//        for (int i = 0; i < list.size(); i++) {
//            Method method = list.get(i).getClass().getMethod("getId", null);
//            String id = null == method.invoke(list.get(i), null) ? "" : method.invoke(list.get(i), null).toString();
//            ids.add(id);
//        }
//        /** 拼接set语句 */
//        for (int i = 0; i < fields.length; i++) {
//            fields[i].setAccessible(true);
//            /** 获取Column注解 */
//            Column column = fields[i].getAnnotation(Column.class);
//            if (null == column) {
//                continue;
//            }
//            sb.append(column.name()).append(" = CASE id ");
//            for (int j = 0; j < list.size(); j++) {
//                T t = list.get(j);
//                /** 反射get方法 */
//                String methodName = "get" + fields[i].getName().substring(0, 1).toUpperCase() +
//                        fields[i].getName().substring(1);
//                Method method = t.getClass().getMethod(methodName, null);
//                sb.append(" WHEN '").append(ids.get(j)).append("' THEN ");
//                String type = method.getAnnotatedReturnType().getType().toString();
//                // 如果type是类类型，则前面包含"class "，后面跟类名
//                Object object = method.invoke(t, null);
//                //如果值为空则更新为原来的值（不更新）
//                if (object == null) {
//                    sb.append(table.name()).append(".").append(column.name());
//                } else if (type.endsWith("String")) {
//                    String value = (String) object;
//                    sb.append("'").append(value).append("'");
//                } else if (type.endsWith("Integer")) {
//                    Integer value = (Integer) object;
//                    sb.append("'").append(value).append("'");
//                } else if (type.endsWith("Boolean")) {
//                    Boolean value = (Boolean) object;
//                    sb.append(value);
//                } else if (type.endsWith("Date")) {
//                    Date value = (Date) object;
//                    sb.append("'").append(CalendarUtil.getStrByDateTime(value)).append("'");
//                } else {
//                    sb.append("'").append(object.toString()).append("'");
//                }
//                sb.append(j == list.size() - 1 ? i == fields.length - 1 && j == list.size() - 1 ? " END " : " END, " : " ")
//                        .append(i == fields.length - 1 && j == list.size() - 1 ? " WHERE id IN (" : "");
//            }
//        }
//        //后置where语句
//        for (int i = 0; i < ids.size(); i++) {
//            sb.append("'").append(ids.get(i)).append("'")
//                    .append(i == ids.size() - 1 ? ")" : ",");
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 功能描述：生成批量查询sql语句
//     * 举例： SELECT * FROM table WHERE id IN ('xx','cc')
//     *
//     * @param t         类型
//     * @param fieldName bean属性名称
//     * @param values    查询值的list
//     * @return java.lang.String
//     * @author jianglinle
//     * @date 18.9.13 17:33
//     */
//    public static <T> String getQuerySql(Class<T> t, String fieldName, List<String> values, Integer page, Integer pageSize) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("SELECT * FROM ");
//        /** 表名 */
//        Table table = t.getAnnotation(Table.class);
//        sb.append(table.name()).append(" WHERE ");
//        /** 字段名 */
//        Field[] fields = t.getDeclaredFields();
//        String value = null;
//        for (Field field : fields) {
//            field.setAccessible(true);
//            if (StringUtils.equals(field.getName(), fieldName)) {
//                Column column = field.getAnnotation(Column.class);
//                value = column.name();
//                break;
//            }
//        }
//        if (null == value) {
//            log.error("生成查询sql语句失败：" + fieldName + " 属性未找到");
//            return null;
//        }
//        sb.append(value);
//        sb.append(" IN (");
//        for (int i = 0; i < values.size(); i++) {
//            sb.append("'").append(values.get(i)).append("'").append(i == values.size() - 1 ? ")" : ",");
//        }
//        /* 分页 */
//        if (null != page && null != pageSize) {
//            sb.append(" LIMIT ").append((page - 1) * pageSize).append(",").append(page * pageSize);
//        }
//        return sb.toString();
//    }
}
