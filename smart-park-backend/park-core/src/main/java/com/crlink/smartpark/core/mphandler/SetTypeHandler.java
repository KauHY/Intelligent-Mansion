package com.crlink.smartpark.core.mphandler;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.mysql.cj.MysqlType;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.result.Field;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@MappedTypes(Set.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class SetTypeHandler extends BaseTypeHandler<Set> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set parameter, JdbcType jdbcType)
            throws SQLException {
        String param = parameter.toString().replaceAll("\\[|\\]| ", "");
        ps.setString(i, param);
    }

    @Override
    public Set<?> getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String sqlSet = rs.getString(columnName);
        if (StrUtil.isNotBlank(sqlSet)) {
            ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
            boolean flag = false;
            for (Field field : metaData.getFields()) {
                if (field.getOriginalName().equals(columnName)) {
                    flag = MysqlType.VARCHAR.equals(field.getMysqlType());
                    break;
                }
            }
            String[] split = sqlSet.split(",");
            if (flag) {
                return Sets.newHashSet(split);
            } else if (NumberUtil.isInteger(split[0])) {
                return Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toSet());
            } else {
                return Sets.newHashSet(split);
            }
        }
        return new HashSet();
    }

    @Override
    public Set<?> getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String sqlSet = rs.getString(columnIndex);
        return getSet(sqlSet);
    }

    @Override
    public Set<?> getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String sqlSet = cs.getString(columnIndex);
        return getSet(sqlSet);
    }

    private Set<?> getSet(String sqlSet) {
        if (StringUtils.isNotBlank(sqlSet)) {
            String[] split = sqlSet.split(",");
            if (NumberUtil.isInteger(split[0])) {
                return Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toSet());
            } else {
                return Sets.newHashSet(split);
            }
        }
        return new HashSet();
    }
}

