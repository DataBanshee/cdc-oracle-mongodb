package ua.com.softsky;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;



/**
 * Hello world!
 *
 */
public class App {
	private static final class TorAssetFiller implements Runnable {
		Connection conn;
		public TorAssetFiller(Connection conn) {
			super();
			this.conn = conn;
		}
		@Override
		public void run() {
			try {
				final String sql = "INSERT INTO tor_asset (serv_acct_id, serial_num, shipt_dt, oper_status_cd, cfg_state_cd, last_upd, row_id, per_asset_id, prod_id, status_cd) VALUES (?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement insert = conn.prepareStatement(sql);
				conn.setAutoCommit(false);

				IntStream.range(0, 1000 * 1000).forEachOrdered(i -> { 
					try {
						insert.setString(1, getRandomString(16));
						insert.setString(2, getRandomString(30));
						insert.setDate(3, new Date(new java.util.Date().getTime()));
						insert.setString(4, getRandomString(10));
						insert.setString(5, getRandomString(10));
						insert.setDate(6, new Date(new java.util.Date().getTime()));
						insert.setString(7, getRandomString(16));
						insert.setString(8, getRandomString(16));
						insert.setString(9, getRandomString(16));
						insert.setString(10, getRandomString(10));
						insert.addBatch();
						
						if (i % 1000 == 0){
							long[] result = insert.executeLargeBatch();
							if((result.length > 0) && (result[0] == 1)){ // FIXME: change 1 to constant
								//System.out.println("Large batch executed with result:" + result[0]);
								if( i % 10000 == 0){
									conn.commit();
									System.out.println(i + " items inserted into tor_asset");
								}
							} else {
								throw new SQLException("executeLargeBatch failed with:" + result[0]);
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	private static final class TorContactFiller implements Runnable {
		Connection conn;
		public TorContactFiller(Connection conn) {
			super();
			this.conn = conn;
		}
		@Override
		public void run() {
			try {
				final String sql = "INSERT INTO tor_contact (per_title, last_name, fst_name, email_addr, home_ph_num) VALUES (?,?,?,?,?)";
				PreparedStatement insert = conn.prepareStatement(sql);
				conn.setAutoCommit(false);

				IntStream.range(0, 1000 * 1000).forEachOrdered(i -> { 
					try {
						insert.setString(1, getRandomString(15));
						insert.setString(2, getRandomString(100));
						insert.setString(3, getRandomString(100));
						insert.setString(4, getRandomString(150));
						insert.setString(5, getRandomString(40));
						insert.addBatch();
						
						if (i % 1000 == 0){
							long[] result = insert.executeLargeBatch();
							if((result.length > 0) && (result[0] == 1)){ // FIXME: change 1 to constant
								//System.out.println("Large batch executed with result:" + result[0]);
								if( i % 10000 == 0){
									conn.commit();
									System.out.println(i + " items inserted into tor_contact");
								}
							} else {
								throw new SQLException("executeLargeBatch failed with:" + result[0]);
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	private static final class TorOrgExtFiller implements Runnable {
		Connection conn;
		
		public TorOrgExtFiller(Connection conn) {
			super();
			this.conn = conn;
		}

		@Override
		public void run() {
			try {
				final String sql = "INSERT INTO tor_contact (per_title, last_name, fst_name, email_addr, home_ph_num) VALUES (?,?,?,?,?)";
				PreparedStatement insert = conn.prepareStatement(sql);
				conn.setAutoCommit(false);

				IntStream.range(0, 1000 * 1000).forEachOrdered(i -> { 
					try {
						insert.setString(1, getRandomString(15));
						insert.setString(2, getRandomString(100));
						insert.setString(3, getRandomString(100));
						insert.setString(4, getRandomString(150));
						insert.setString(5, getRandomString(40));
						insert.addBatch();
						
						if (i % 1000 == 0){
							long[] result = insert.executeLargeBatch();
							if((result.length > 0) && (result[0] == 1)){ // FIXME: change 1 to constant
								//System.out.println("Large batch executed with result:" + result[0]);
								if( i % 10000 == 0){
									conn.commit();
									System.out.println(i + " items inserted into tor_contact");
								}
							} else {
								throw new SQLException("executeLargeBatch failed with:" + result[0]);
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException, InterruptedException {

		System.out.println("-------- Oracle JDBC Connection Testing ------");

		Class.forName("oracle.jdbc.driver.OracleDriver");

		System.out.println("Oracle JDBC Driver Registered!");

		Connection connection = null;

		connection = DriverManager.getConnection("jdbc:oracle:thin:@oracle:1521:xe", "system", "oracle");

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

		DatabaseMetaData md = connection.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		System.out.println("--- User Tables  ---");
		while (rs.next()) {
			if(rs.getString("TABLE_NAME").startsWith("TOR_"))
			System.out.println(rs.getString("TABLE_NAME"));
		}
		System.out.println("-------------");
		
		Statement stmt = connection.createStatement();

		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("c", false, "create tables");
		options.addOption("d", false, "drop tables");
		options.addOption("f", false, "fill tables");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("c")) {
			System.out.println("--- Creating tables ---");
			createTables(stmt);
		}

		if (cmd.hasOption("d")) {
			System.out.println("--- Deleting tables ---");
			dropTables(stmt);
		}

		if (cmd.hasOption("f")) {
			System.out.println("--- Filling tables ---");
			fillTables(connection);
		}

		System.out.println("Done!");

		stmt.close();
		connection.close();
	}

	private static void createTables(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE tor_org_ext " + "(row_id VARCHAR2(16)," + "market_type_cd VARCHAR2(20),"
				+ "alias_name VARCHAR2(150)," + "x_system_fact VARCHAR2(50)," + "x_date_migration DATE,"
				+ "x_flag_pro  VARCHAR2(20)," + "last_upd DATE," + "x_id_nc  VARCHAR2(18)," + "x_rcs VARCHAR2(30))";

		stmt.executeUpdate(sql);
		
		sql = "CREATE TABLE tor_contact " + "(per_title VARCHAR2(15)," + "last_name VARCHAR2(100),"
				+ "fst_name VARCHAR2(100)," + "email_addr VARCHAR2(150)," + "home_ph_num VARCHAR2(40))";

		stmt.executeUpdate(sql);

		sql = "CREATE TABLE tor_asset " + "(serv_acct_id varchar2(16)," + "serial_num VARCHAR2(30)," + "shipt_dt DATE,"
				+ "oper_status_cd VARCHAR2(10)," + "cfg_state_cd VARCHAR2(10)," + "last_upd DATE,"
				+ "row_id VARCHAR2(16)," + "per_asset_id VARCHAR2(16)," + "prod_id VARCHAR2(16),"
				+ "status_cd VARCHAR2(16))";

		stmt.executeUpdate(sql);

	}

	private static void dropTables(Statement stmt) {
		dropTable(stmt, "tor_org_ext");
		dropTable(stmt, "tor_contact");
		dropTable(stmt, "tor_asset");
	}

	private static void dropTable(Statement stmt, final String tableName) {
		try {
			stmt.executeUpdate("DROP TABLE " + tableName);
			System.out.println("tor_org_ext deleted");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private static void fillTables(Connection conn) throws InterruptedException {
		ExecutorService service = Executors.newCachedThreadPool();
		service.submit(new TorOrgExtFiller(conn));
		service.submit(new TorContactFiller(conn));		
		service.submit(new TorAssetFiller(conn));
		
		service.awaitTermination(1, TimeUnit.DAYS);
	}


	private static String getRandomString(final int len) {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, Math.min(uuid.length() - 1, len - 1));
	}

}
