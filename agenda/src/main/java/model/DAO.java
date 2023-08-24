package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DAO {
	/** Módulo de conexão **/
	// Parametro de conexão
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/dbagenda?serverTimezone=America/Sao_Paulo";
	private String user = "root";
	private String password = "banco1.0";

	// Método de conexão
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

	// CRUD CREATE
	public void inserirContato(JavaBeans contato) {
		String create = "insert into contatos(nome, fone, email) values (?, ?, ?)";
		try {
			// abrir a conexão com o banco
			Connection con = conectar();
			// preparar a query para execução no BD
			PreparedStatement pst = con.prepareStatement(create);
			// substituir os parametros pelo conteudo das variaveis JavaBeans
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			// executar a query
			pst.executeUpdate();
			// encerrar a conexao com o banco
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
