package grp4.common.data.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseCallback {
    void accept(Connection conn) throws SQLException;
}
