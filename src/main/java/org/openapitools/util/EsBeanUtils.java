package org.openapitools.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xiakui
 * @DATE 2022/3/2
 */
@Slf4j
public class EsBeanUtils {


    /**
     * 对象转map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> getProperties(Object obj) {

        Map<String, Object> properties = new LinkedHashMap<>();
        try {
            //key: 字段名称 value: 字段类型
            //目前统一的es的对应类型只有四种种 String:text  Integer、Long 、List: keyword

            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field f : fields) {

                if(f.isSynthetic()){
                    continue;
                }

                f.setAccessible(true);
                properties.put(f.getName(), f.get(obj));

            }
        } catch (Exception e) {

        }

        return properties;
    }

    /**
     * 跟进实体的注解标识映射map
     *
     * @param obj
     * @return
     */
    public static Map<String, String> getBasicFieldMap(Object obj) {

        //key: 字段名称 value: 字段类型
        //目前统一的es的对应类型只有三种 String:text  Integer、Long
        Map<String, String> fieldMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            //获取Field属性上的es定义类型
            boolean annotationPresent = f.isAnnotationPresent(org.springframework.data.elasticsearch.annotations.Field.class);
            if (annotationPresent) {
                // 获取自定义注解对象
                org.springframework.data.elasticsearch.annotations.Field esField = f.getAnnotation(
                        org.springframework.data.elasticsearch.annotations.Field.class);
                // 根据对象获取注解值
                if (FieldDefinedTypeEnum.containType(esField.type())) {
                    String typeValue = FieldDefinedTypeEnum.getByType(esField.type()).getValue();
                    fieldMap.put(f.getName(), typeValue);
                }

            }

        }
        return fieldMap;
    }

    /**
     * 处理业务和自定义映射聚合
     *
     * @param fieldMap
     * @param obj
     */
    public static Map<String, Object> handleFieldMap(Map<String, Object> fieldMap, Object obj) {

        Map<String, Object> properties = new HashMap<>();
        try {

            //基础字段map
            Map<String, Object> basicFieldMap = getProperties(obj);
            properties.putAll(basicFieldMap);

            if (!CollectionUtils.isEmpty(fieldMap)) {

                fieldMap.keySet().forEach(f -> {
                    //不是基本类型的字段，比如是数组、实体对象的
                    if (!(fieldMap.get(f) instanceof String)
                            && !(fieldMap.get(f) instanceof Integer)
                            && !(fieldMap.get(f) instanceof Long)) {
                        properties.put(f, JSONObject.toJSONString(fieldMap.get(f)));
                    } else {

                        properties.put(f, fieldMap.get(f));
                    }
                });

            }

        } catch (Exception e) {

        }
        return properties;
    }




    private EsBeanUtils(){

    }


}
