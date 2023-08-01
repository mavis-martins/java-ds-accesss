package br.com.dbconnection;

import java.sql.SQLException;
import java.util.Scanner;

public class AcessoBanco {
	public static void main(String[] args) throws SQLException {

		DataSource ds = new DataSource();
		ds.criarTabela();
		ds.inserirRegTab();
		ds.alteraRegistro(2,null);

		Scanner scanner = new Scanner(System.in);
		System.out.println("Informe o nome do café que deseja pesquisar:");
		String nomeCafe = scanner.nextLine();
		
		System.out.println(ds.getDadosCafe(nomeCafe));
		
	}
}
