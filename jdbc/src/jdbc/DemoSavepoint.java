package jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

public class DemoSavepoint {
	static final String TABLE_INFO_QUERY = "SELECT id, name FROM member";
	static final String URL = "jdbc:mysql://localhost:3306/admin2?" + "useSSL=false";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		Connection conn = DriverManager.getConnection(URL, "root", "admin");
		Statement stmt = null;
		ResultSet rs;
		String sql;

		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			sql = TABLE_INFO_QUERY;
			rs = stmt.executeQuery(sql);
			printQueryResult(rs);

//			Deleting query: using rollback to the savepoint (delete row 3 only, doesn't delete row 2)
			System.out.println("Deleting row 3...");
			sql = "DELETE FROM member " + "WHERE ID = 3";
			stmt.executeUpdate(sql);

			Savepoint savepoint = conn.setSavepoint("Delete row 2 but then rollback...");

			System.out.println("Deleting another row:");
			sql = "DELETE FROM member " + "WHERE ID = 2";
			stmt.executeUpdate(sql);

			conn.rollback(savepoint);
			conn.commit();

			sql = TABLE_INFO_QUERY;
			rs = stmt.executeQuery(sql);
			printQueryResult(rs);

// 			Inserting query
			System.out.println("");
			sql = "INSERT INTO member " + "VALUES (7, 'inserted unit')";
			stmt.executeUpdate(sql);
			conn.commit();

			sql = "INSERT INTO member " + "VALUES (8, 'non-inserted unit')";
			stmt.executeUpdate(sql);

			sql = "";
			rs.close();
			stmt.close();
			conn.close();

//			Using procedure getMember() to print out all set of database
			Connection conn2 = DriverManager.getConnection(URL, "root", "admin");
			CallableStatement call_stmt = conn2.prepareCall("{Call getMember()}");
			rs = call_stmt.executeQuery();
			printQueryResult(rs);
			rs.close();
			call_stmt.close();
			conn2.close();

		} catch (SQLException se) {
			se.printStackTrace();
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException se2) {
				se2.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();			
		} 		
	}

	public static void printQueryResult(ResultSet rs) throws SQLException {
		rs.beforeFirst();
		System.out.println("List of result:");
		while (rs.next()) {
			System.out.print("ID: " + rs.getInt("id"));
			System.out.println(", Name: " + rs.getString("name"));
		}
		System.out.println();
	}
}
