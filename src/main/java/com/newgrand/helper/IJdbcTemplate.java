package com.newgrand.helper;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

public class IJdbcTemplate extends JdbcTemplate {

    public IJdbcTemplate(DataSource dataSource) {
//        this.setDataSource(dataSource);
//        this.afterPropertiesSet();
        super(dataSource);
    }

    /**
     * 重写JdbcTemplate里面的queryForObject方法源码调用的requiredSingleResult，当查询到的结果为空时返回null(原来是抛出异常)
     */

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
        return queryForObject(sql, getSingleColumnRowMapper(requiredType));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        List<T> results = query(sql, rowMapper);
        return requiredSingleResult(results);
    }

    public <T> T queryForObject(String sql, @Nullable Object[] args, Class<T> requiredType) throws DataAccessException {
        return this.queryForObject(sql, args, this.getSingleColumnRowMapper(requiredType));
    }

    public <T> T requiredSingleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        int size = (results != null ? results.size() : 0);
        if (size == 0) {
            return null;
        }
        if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.iterator().next();
    }
}
