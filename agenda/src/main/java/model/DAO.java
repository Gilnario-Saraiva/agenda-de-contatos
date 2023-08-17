package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {
	/**Módulo de conexão**/
	//Parametro de conexão
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/dbagenda?serverTimezone=America/Sao_Paulo";
	private String user = "root";
	private String password = "banco1.0";
		
	//Método de conexão
	private Connection conectar() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
}
