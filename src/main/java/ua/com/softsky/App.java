package ua.com.softsky;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
public class App 
{
    public static void main( String[] args ) throws SQLException, ClassNotFoundException, ParseException
    {

        System.out.println("-------- Oracle JDBC Connection Testing ------");

        Class.forName("oracle.jdbc.driver.OracleDriver");

        System.out.println("Oracle JDBC Driver Registered!");

        Connection connection = null;


            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@oracle:1521:xe", "system", "oracle");


        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
        
        Statement stmt = connection.createStatement();
        
        // create Options object
        Options options = new Options();

        // add t option
        options.addOption("c", false, "create tables");
        options.addOption("d", false, "drop tables");
        options.addOption("f", false, "fill tables");
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);
        
        if(cmd.hasOption("c")){
        	createTables(stmt);
        }
        
		if (cmd.hasOption("d")) {
			dropTables(stmt);
		}
        
		if (cmd.hasOption("f")) {
			fillTables(connection);
		}

		System.out.println( "Done!" );
    }

	private static void createTables(Statement stmt) throws SQLException {
		
		
		String sql = "CREATE TABLE tor_org_ext"
        		+ "(row_id VARCHAR2(16),"
        		+ "market_type_cd VARCHAR2(20),"
        		+ "alias_name VARCHAR2(150),"
        		+ "x_system_fact VARCHAR2(50),"
        		+ "x_date_migration DATE,"
        		+ "x_flag_pro  VARCHAR2(20),"
        		+ "last_upd DATE,"
        		+ "x_id_nc  VARCHAR2(18),"
        		+ "x_rcs VARCHAR2(30))";
		
		
		stmt.executeUpdate("DROP TABLE tor_contact");
		sql = "CREATE TABLE tor_contact"
        		+ "(per_title VARCHAR2(15),"
        		+ "last_name VARCHAR2(100),"
        		+ "fst_name VARCHAR2(100),"
        		+ "email_addr VARCHAR2(150),"
        		+ "home_ph_name VARCHAR2(40))";
		
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE tor_asset"
        		+ "(serv_acct_id varchar2(16),"
        		+ "serial_num VARCHAR2(30),"
        		+ "shipt_dt DATE,"
        		+ "oper_status_cd VARCHAR2(10),"
        		+ "cfg_state_cd VARCHAR2(10),"
        		+ "last_upd DATE,"
        		+ "row_id VARCHAR2(16),"
        		+ "par_asset_id VARCHAR2(16),"
        		+ "prod_id VARCHAR2(16),"
        		+ "status_cd VARCHAR2(16))";
		
		stmt.executeUpdate(sql);
        
	}

	private static void dropTables(Statement stmt) throws SQLException {
		stmt.executeUpdate("DROP TABLE tor_org_ext");
		stmt.executeUpdate("DROP TABLE tor_contact");
		stmt.executeUpdate("DROP TABLE tor_asset");
	}
	
	private static void fillTables(Connection conn) throws SQLException {
		
		final String sql = "INSERT INTO tor_org_ext SET row_id=?, market_type_cd=?, alias_name=?, x_system_fact=?, x_date_migration=?,"
				+ "x_flag_pro=?, last_upd=?, x_id_nc=?, x_rcs=?";
		PreparedStatement insert = conn.prepareStatement(sql);
		conn.setAutoCommit(false);
		
		IntStream.range(0, 10).forEachOrdered(n -> {
			IntStream.range(0, 1000).forEachOrdered(i -> {
				try {
					insert.setString(0, new Integer(n * i).toString());
					insert.addBatch();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			try {
				insert.executeLargeBatch();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}
	
}
