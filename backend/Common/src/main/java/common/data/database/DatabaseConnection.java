package common.data.database;

import common.config.Config;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private DataSource dataSource;

    public DatabaseConnection() {
        BasicDataSource bds = new BasicDataSource();

        bds.setUrl(Config.getInstance("core").getProperty("db.url"));
        bds.setUsername(Config.getInstance("core").getProperty("db.username"));
        bds.setPassword(Config.getInstance("core").getProperty("db.password"));

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
