package jdbc;

import java.sql.*;

class TestJDBC {
	static String dbname = "admin2?userSSL=false";
	static String username = "root";
	static String password = "admin";
	
	public static void main(String args[]) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		printTable();
	}
	
	public static void printTable() {
		try {
			System.out.println("The data in the table");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname, username, password);
			CallableStatement getMember = con.prepareCall("{call getMember()}");
			ResultSet rs = getMember.executeQuery();
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2));
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
	
	