package i5.las2peer.services.servicePackage.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * This class manages database credentials and provides connection from a connection pooling system
 *
 */
public class DatabaseManager {

	private static BasicDataSource dataSource;

	public DatabaseManager(String jdbcDriverClassName, String jdbcLogin, String jdbcPass, String jdbcUrl,
			String jdbcSchema) {
		// prepare and configure data source
		dataSource = new BasicDataSource();
		dataSource.setDefaultAutoCommit(true);
		dataSource.setDriverClassName(jdbcDriverClassName);
		dataSource.setUsername(jdbcLogin);
		dataSource.setPassword(jdbcPass);
		dataSource.setUrl(jdbcUrl + jdbcSchema);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setDefaultQueryTimeout(1000);
		dataSource.setMaxConnLifetimeMillis(100000);
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
