package org.openapitools.util;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author xiakui
 * @DATE 2022/2/19
 */

public enum FieldDefinedTypeEnum {
    DEFINED_TYPE_1(FieldType.Integer, "integer"),
    DEFINED_TYPE_2(FieldType.Text, "text"),
    DEFINED_TYPE_3(FieldType.Long, "long"),
    DEFINED_TYPE_4(FieldType.Keyword, "keyword"),
    DEFINED_TYPE_5(FieldType.Date, "date");

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private FieldType type;
    private String value;

    FieldDefinedTypeEnum(FieldType type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Boolean containType(FieldType type) {
        FieldDefinedTypeEnum[] all = values();
        Boolean isContain = false;
        for (FieldDefinedTypeEnum fieldTypeEnum : all) {
            if (fieldTypeEnum.getType().equals(type)) {
                isContain = true;
            }
        }
        return isContain;
    }

    public static FieldDefinedTypeEnum getByType(FieldType type) {
        FieldDefinedTypeEnum[] all = values();
        for (FieldDefinedTypeEnum fieldTypeEnum : all) {
            if (fieldTypeEnum.getType().equals(type)) {
                return fieldTypeEnum;
            }
        }
        return null;
    }

}
