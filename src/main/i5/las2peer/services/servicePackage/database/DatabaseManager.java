package i5.las2peer.services.servicePackage.database;

import i5.las2peer.api.Configurable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * This class manages database credentials and provides connection from a connection pooling system
 *
 */
public class DatabaseManager extends Configurable {

	private static BasicDataSource dataSource;
	private String jdbcDriverClassName;
	private String jdbcLogin;
	private String jdbcPass;
	private String jdbcUrl;
	private String jdbcSchema;
	private final int DEFAULT_JDBC_DEFAULT_QUERY_TIMEOUT = 1000;
	private int jdbcDefaultQueryTimeout = DEFAULT_JDBC_DEFAULT_QUERY_TIMEOUT;
	private final int DEFAULT_JDBC_MAC_CONN_LIFETIME_MILLIS = 100000;
	private int jdbcMaxConnLifetimeMillis = DEFAULT_JDBC_MAC_CONN_LIFETIME_MILLIS;

	public DatabaseManager() throws ClassNotFoundException {
		// read and set properties values
		setFieldValues();
		// request classloader to load JDBC driver class
		Class.forName(jdbcDriverClassName);
		// prepare and configure data source
		dataSource = new BasicDataSource();
		dataSource.setDefaultAutoCommit(true);
		dataSource.setDriverClassName(jdbcDriverClassName);
		dataSource.setUsername(jdbcLogin);
		dataSource.setPassword(jdbcPass);
		dataSource.setUrl(jdbcUrl + jdbcSchema);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setDefaultQueryTimeout(jdbcDefaultQueryTimeout);
		dataSource.setMaxConnLifetimeMillis(jdbcMaxConnLifetimeMillis);
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public static void main(String[] args) throws Exception {
		DatabaseManager db = new DatabaseManager();
		// select example
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		try {
			conn = db.getConnection();
			stmnt = conn.prepareStatement("SELECT email FROM users WHERE username = ?");
			stmnt.setString(1, "userA");
			rs = stmnt.executeQuery();
			if (rs.isBeforeFirst()) {
				System.out.println("No entry found for userA");
			} else {
				System.out.println("Email for userA is " + rs.getString(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// free resources if exception or not
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stmnt != null) {
				try {
					stmnt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// update example
		try {
			conn = db.getConnection();
			stmnt = conn.prepareStatement("UPDATE users SET email = ? WHERE username = ?");
			stmnt.setString(1, "userA@example.org");
			stmnt.setString(2, "userA");
			int rows = stmnt.executeUpdate(); // same works for insert
			System.out.println("Database updated. "+rows+" rows affected");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// free resources if exception or not
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stmnt != null) {
				try {
					stmnt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
