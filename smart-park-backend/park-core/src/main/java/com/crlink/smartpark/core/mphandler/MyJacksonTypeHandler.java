package com.crlink.smartpark.core.mphandler;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


@Slf4j
@MappedTypes({Object.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MyJacksonTypeHandler<T> extends BaseTypeHandler<T> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(parameter);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ps.setString(i, json);
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(rs.getMetaData().getTableName(1));
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        TableFieldInfo fieldInfo = fieldList.stream().filter(f -> Objects.equals(f.getColumn(), columnName)).findFirst().orElse(null);
        Field field = fieldInfo.getField();
        final String json = rs.getString(columnName);
        T value = null;
        try {
            value = objectMapper.readValue(json, parseType(field));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return StringUtils.isBlank(json) ? null : value;
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        final String json = rs.getString(columnIndex);
        TableInfo tableInfo = TableInfoHelper.getTableInfo(rs.getMetaData().getTableName(1));
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        Field field = fieldList.get(columnIndex).getField();
        T value = null;
        try {
            value = objectMapper.readValue(json, parseType(field));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return StringUtils.isBlank(json) ? null : value;
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        final String json = cs.getString(columnIndex);
        //return StringUtils.isBlank(json) ? null : parse(json);
        return null;
    }


    private JavaType parseType(Field field) {
        ParameterizedTypeImpl genericType = (ParameterizedTypeImpl) field.getGenericType();
        Type[] arguments = genericType.getActualTypeArguments();
        Class[] classes = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            classes[i] = (Class) arguments[i];
        }
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(genericType.getRawType(), classes);
        return javaType;
    }
}


