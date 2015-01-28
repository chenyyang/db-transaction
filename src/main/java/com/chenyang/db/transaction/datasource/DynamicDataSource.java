package com.chenyang.db.transaction.datasource;

import com.chenyang.db.transaction.utils.DBContextHolder;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author 作者 E-mail:
 * @version 创建时间：Aug 21, 2014 3:22:27 PM
 * 类说明
 */
public class DynamicDataSource extends BasicDataSource {


    private static Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    private String defaultCatelog;

    public String getDefaultCatelog() {
        return defaultCatelog;
    }

    public void setDefaultCatelog(String defaultCatelog) {
        this.defaultCatelog = defaultCatelog;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public DataSource getDataSource() {
        //return this.jadeDataSourceFactory.getDataSource(getCatelog(), DbRole.MASTER, null);
         //获取DataSource链接
        return null;
    }

    private String getCatelog() {
        String threadCatelog = DBContextHolder.getCateloge();
        String catelog = StringUtils.isBlank(threadCatelog) ? defaultCatelog : threadCatelog;
        logger.debug(" get thread catelog : {} return catelog : {}", threadCatelog, catelog);
        return catelog;
    }

}


