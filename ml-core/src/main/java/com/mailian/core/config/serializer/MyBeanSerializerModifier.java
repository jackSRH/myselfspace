package com.mailian.core.config.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/9/26
 * @Description:
 */
public class MyBeanSerializerModifier extends BeanSerializerModifier {
    private JsonSerializer _nullArrayJsonSerializer = new NullArrayJsonSerializer();
    private JsonSerializer _nullStringSerializer = new NullStringSerializer();
    private JsonSerializer _nullNumberSerializer = new NullNumberJsonSerializer();
    private JsonSerializer _nullBooleanSerializer = new NullBooleanJsonSerializer();

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);

            final JavaType javaType = writer.getType();
            final Class<?> rawClass = javaType.getRawClass();
            if (javaType.isArrayType() || javaType.isCollectionLikeType()) {
                writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
            } else if (Number.class.isAssignableFrom(rawClass) && rawClass.getName().startsWith("java.lang")) {
                writer.assignNullSerializer(this.defaultNullNumberSerializer());
            } else if (Boolean.class.equals(rawClass)) {
                writer.assignNullSerializer(this.defaultNullBooleanSerializer());
            } else if (String.class.equals(rawClass)) {
                writer.assignNullSerializer(this.defaultNullStringSerializer());
            }
        }
        return beanProperties;
    }

    protected boolean isArrayType(BeanPropertyWriter writer) {
        return writer.getType().isArrayType() || writer.getType().isCollectionLikeType();
    }

    protected boolean isStringType(BeanPropertyWriter writer) {

        Class<?> rawClass = writer.getType().getRawClass();
        return rawClass.equals(String.class);

    }

    protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
        return _nullArrayJsonSerializer;
    }

    protected JsonSerializer<Object> defaultNullStringSerializer() {
        return _nullStringSerializer;
    }

    protected JsonSerializer<Object> defaultNullNumberSerializer() {
        return _nullNumberSerializer;
    }

    protected JsonSerializer<Object> defaultNullBooleanSerializer() {
        return _nullBooleanSerializer;
    }

}
