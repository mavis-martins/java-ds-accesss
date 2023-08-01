package br.com.dbconnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DataSource {
	private java.sql.Connection connection;

	public DataSource() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = (Connection) DriverManager
					.getConnection("jdbc:mysql://localhost:3306/cafeteria?allowPublicKeyRetrieval=true&useSSL=false", "root", "159753");
			System.out.println("Conexão estabelecida!!");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Erro Driver Jdbc!" + cnfe.getLocalizedMessage());
		} catch (SQLException sqle) {
			System.out.println("Conexão falhou!");
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void criarTabela() throws SQLException {

		Statement stat = (Statement) connection.createStatement();
		stat.executeUpdate("drop table if exists cafes;");
		stat.executeUpdate(
				"create table cafes (codigo int not null auto_increment , nome varchar(20), custo numeric(10,2), descricao varchar(1000),"
						+ "primary key (codigo));");

		// Verificar se existe a tabela cafes no banco de dados
		DatabaseMetaData dbm = (DatabaseMetaData) connection.getMetaData();
		ResultSet tabela = dbm.getTables(null, null, "cafes", null);

		if (tabela.next()) {
			System.out.println("Tabela Criada Com Sucesso!!");
		} else {
			System.out.println("Tabela Não Existe");
		}
		// connection.close();
	}

	public void inserirRegTab() throws SQLException {

		PreparedStatement prep = (PreparedStatement) connection
				.prepareStatement("INSERT INTO cafes (nome,custo,descricao) values (?, ?, ?);");

		prep.setString(1, "Café Expresso");
		prep.setDouble(2, 12.50);
		prep.setString(3,
				"servido apenas com o mais puro café, sem qualquer mistura com leite ou outro ingrediente. \r\n"
						+ "A famosa espuma que se forma sobre a bebida é originária do próprio grão moído, e é produzida no momento da extração");
		prep.addBatch();
		prep.setString(1, "Café com leite");
		prep.setDouble(2, 22.33);
		prep.setString(3, "O Café com Leite não tem segredo. Ele é servido com uma proporção \r\n"
				+ "igual de café e leite fervido ou aquecido.");
		prep.addBatch();
		prep.setString(1, "Capuccino");
		prep.setDouble(2, 21.05);
		prep.setString(3, "Uma das preferências nacionais, o Cappucino é servido com proporções iguais de café, \r\n"
				+ "leite vaporizado e creme. A espuma cremosa que fica sobre o leite é produzida na própria vaporização da bebida láctea.\r\n"
				+ "Para ficar mais incrementado, o cappucino pode ser servido com canela ou chocolate em pó");
		prep.addBatch();
		prep.setString(1, "Mocha");
		prep.setDouble(2, 29.15);
		prep.setString(3, "Quem gosta de uma bebida mais docinha, vai curtir apreciar um Mocha. \r\n"
				+ "Ele traz uma combinação entre calda de chocolate, café, leite vaporizado e creme. É irresistível.");
		prep.addBatch();

		connection.setAutoCommit(false);
		prep.executeBatch();
		connection.setAutoCommit(true);

		
	}

	public void alteraRegistro(int codigo, Double custo) throws SQLException {

		String sqlConsultaPorCod = "Select codigo,nome,custo from " + "cafes where codigo = ?";

		String sqlAlteracao = "update cafes set custo = ?" + " where codigo = ?";

		if (custo == null) {
			PreparedStatement prep = (PreparedStatement) connection.prepareStatement(sqlConsultaPorCod);
			prep.setInt(1, codigo);
			ResultSet rs = prep.executeQuery();

			if (rs.next()) {
				System.out.println("========================================================");
				System.out.println("Dados do Café Para Alteração");
				System.out.println("Código..: " + rs.getString("codigo"));
				System.out.println("Nome..: " + rs.getString("nome"));
				System.out.println("Custo.: " + rs.getString("custo"));
				System.out.println("========================================================");
			} else {
				System.out.println("Registro não Encontrado!");
				System.exit(0);
			}
		} else {
			StringBuffer srtRet = new StringBuffer();
			try {
				PreparedStatement prep = (PreparedStatement) connection.prepareStatement(sqlAlteracao);
				prep.setDouble(1, custo);
				prep.setInt(2, codigo);
				prep.execute();
				System.out.println("Registro alterado com sucesso!");
			} catch (Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}

		}

	}
	
	public void deletarRegistro(int codigo) throws SQLException {
		PreparedStatement prep = (PreparedStatement) connection.prepareStatement("DELETE FROM cafes where codigo = ? ");
		connection.setAutoCommit(true);
		prep.setInt(1, codigo);
		prep.executeUpdate();
	}

    public String getDadosCafe(String nomeCafe) {

		String sqlConsulta = "Select codigo,nome,custo,descricao from cafes where nome like ?";

    	PreparedStatement prepareStmt = null;
        ResultSet retQuery = null;
        StringBuffer dadodRet = new StringBuffer();

        try {
            prepareStmt = (PreparedStatement) this.getConnection().prepareStatement(sqlConsulta);
            prepareStmt.setString(1, "%" + nomeCafe + "%");
            retQuery = prepareStmt.executeQuery();

            if (retQuery.next()) {
                dadodRet.append("Código: ");
                dadodRet.append(retQuery.getInt("codigo"));
                dadodRet.append("\nNome: ");
                dadodRet.append(retQuery.getString("nome"));
                dadodRet.append("\n\tDescrição: ");
                dadodRet.append(retQuery.getString("descricao"));
                dadodRet.append("\nValor: R$ ");
                dadodRet.append(retQuery.getFloat("custo"));
            }
        } catch (Exception e) {
            dadodRet.append("Erro ao executar a consulta: "+e.getMessage());
        }
        return dadodRet.toString();
    }
}
