package br.com.dbconnection;

import java.sql.SQLException;
import java.util.Scanner;

public class AcessoBanco {
	public static void main (String [] args) throws SQLException {

		DataSource ds = new DataSource();
		 try {
		        ds.criarTabela();
		        ds.inserirRegTab();
		        ds.alteraRegistro(2, null);

		        Scanner scanner = new Scanner(System.in);
		        System.out.println("Informe o nome do café que deseja pesquisar:");
		        String nomeCafe = scanner.nextLine();
		        
		        System.out.println(ds.getDadosCafe(nomeCafe));
		    } catch (SQLException sqle) {
		        System.out.println("Erro ao executar operações no banco de dados: " + sqle.getMessage());
		    } finally {
		        try {
		            if (ds.getConnection() != null) {
		                ds.getConnection().close();
		            }
		        } catch (SQLException e) {
		            System.out.println("Erro ao fechar a conexão: " + e.getMessage());
		        }
		    }
	}
}
