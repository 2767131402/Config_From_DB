package com.zhenglei.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBUtils {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static DataSource createDataSource() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, JDBC_DRIVER);
        map.put(DruidDataSourceFactory.PROP_URL, DB_URL);
        map.put(DruidDataSourceFactory.PROP_USERNAME, USERNAME);
        map.put(DruidDataSourceFactory.PROP_PASSWORD, PASSWORD);
        return DruidDataSourceFactory.createDataSource(map);
    }

    public static SysConfig getObject(String groupId, String dataId) {
        SysConfig sysConfig = new SysConfig();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            DataSource datasource = createDataSource();
            connection = datasource.getConnection();
            String sql = "SELECT id,dataId,groupId,description FROM sys_config where dataId=? and groupId=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, dataId);
            ps.setString(2, groupId);
            ResultSet resultSet = ps.executeQuery();
            // 展开结果集数据库
            while (resultSet.next()) {
                sysConfig.setId(resultSet.getInt("id"));
                sysConfig.setDataId(resultSet.getString("dataId"));
                sysConfig.setGroupId(resultSet.getString("groupId"));
                sysConfig.setDescription(resultSet.getString("description"));
            }
            return sysConfig;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
