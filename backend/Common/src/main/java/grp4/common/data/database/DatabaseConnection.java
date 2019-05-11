package grp4.common.data.database;

import grp4.common.config.Config;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private DataSource dataSource;

    public DatabaseConnection() {
        BasicDataSource bds = new BasicDataSource();

        bds.setUrl(Config.getInstance().getProperty("db.url"));
        bds.setUsername(Config.getInstance().getProperty("db.username"));
        bds.setPassword(Config.getInstance().getProperty("db.password"));

        dataSource = bds;
    }

    protected DataSource getDataSource() {
        return dataSource;
    }


    protected void executeQuery(IDatabaseCallback callback) {
        try (Connection connection = dataSource.getConnection()) {
            callback.accept(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
