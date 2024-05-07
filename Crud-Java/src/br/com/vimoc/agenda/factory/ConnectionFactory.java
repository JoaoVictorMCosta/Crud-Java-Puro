package br.com.vimoc.agenda.factory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

	//nome do usuario h2
	private static final String USERNAME = "sa";
	
	//Senha do banco
	private static final String Password = "";
	
	//Url de acesso do banco de dados
	private static final String DATABASE_URL = "jdbc:h2:mem:test";
	
//	
//	Conexão com o banco
//
	
	public static Connection createConnetionToH2() throws Exception  {
		//Faz a classe carregar na JVM
		Class.forName("org.h2.Driver");
		
		//Cria a conexao com o banco de dados
		Connection connection = DriverManager.getConnection(DATABASE_URL,USERNAME,Password);
		
		return connection;
	}
	
	
	public static void main(String[] args) throws Exception {
		//Recuperar a conexão com o banco
		Connection con = createConnetionToH2();
		
		//Testar se a conexão com o banco
		if(con!=null) {
			System.out.println("Conexão obtida com sucesso!");
			con.close();
		}
		
	}
}