package org.example;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String connectionString = "jdbc:sqlite:banco.db";
    static void main() throws Exception{
        //criar a tabela de alunos, caso ela não exista
        criartabela();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite seu nome: ");
        var nome = scanner.nextLine();

        System.out.println("Digite seu email: ");
        var email = scanner.nextLine();

        System.out.println("Digite seu idade: ");
        var idade = scanner.nextInt();

        inserir(nome, email, idade);
        consultarTodos();
    }

    private static void criartabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS alunos(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    email TEXT NOT NULL,
                    idade INTEGER
                )
            """;
        try(var connection =  DriverManager.getConnection(connectionString)){
           var statement = connection.createStatement();
           statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Erro ao abrir a conexão: " + e.getMessage());
        }
    }

    private static void inserir(String nome, String email, int idade){

        String sql = "INSERT INTO Alunos (nome,email,idade)";
        sql += String.format("VALUES ('%s', '%s', %d)", nome, email, idade);

        try (var connection = DriverManager.getConnection(connectionString)) {
            var statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao executar a inserção: " + e.getMessage());
        }

    }

    private static void consultarTodos(){
        String sql = """
                    SELECT id, nome, email, idade from Alunos;
                """;
        try(var connection = DriverManager.getConnection(connectionString)){
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                var id = resultSet.getInt("id");
                var nome = resultSet.getString("nome");
                var email = resultSet.getString("email");
                var idade = resultSet.getInt("idade");

                System.out.printf("Dados do Aluno: %s %s %s %s \n", id, nome, email, idade);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao executar a inserção: " + e.getMessage());
        }
    }
}