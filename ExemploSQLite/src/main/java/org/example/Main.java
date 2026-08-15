package org.example;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String connectionString = "jdbc:sqlite:banco.db";
    static void main() throws Exception {
        //criar a tabela de alunos, caso ela não exista
        criartabela();

        Scanner scanner = new Scanner(System.in);

        int opcao = 0;

        do {
            exibirMenu();
            System.out.println("Digite a opção: ");
            opcao = scanner.nextInt();


            switch (opcao) {
                case 1 -> inserir();
                case 2 -> consultarTodos();
                case 3 -> buscarAluno();
                case 4 -> atualizarAluno();
            }
        }while(opcao != 0 );

    }

    public static void exibirMenu(){
        System.out.println();
        System.out.println("============================");
        System.out.println("      SISTEMA DE ALUNOS");
        System.out.println("============================");
        System.out.println("1 - Cadastrar aluno");
        System.out.println("2 - Listar alunos");
        System.out.println("3 - Buscar aluno");
        System.out.println("4 - Atualizar aluno");
        System.out.println("5 - Excluir aluno");
        System.out.println("6 - Sair");
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

    private static void inserir(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite seu nome: ");
        String nome = scanner.next();

        System.out.println("Digite seu email: ");
        String email = scanner.next();

        System.out.println("Digite seu idade: ");
        int idade =  scanner.nextInt();

        String sql = "INSERT INTO Alunos (nome,email,idade) VALUES (?, ?, ?)";

        try (var connection = DriverManager.getConnection(connectionString)) {
            var statement = connection.prepareStatement(sql);
            statement.setString(1, nome);
            statement.setString(2, email);
            statement.setInt(3, idade);
            statement.executeUpdate();
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

    private static void buscarAluno(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o id do aluno: ");
        int id  =  scanner.nextInt();
        String sql = ("SELECT *  FROM alunos WHERE id = ?");

        try (var connection = DriverManager.getConnection(connectionString)){
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            var resultado = statement.executeQuery();

            if (resultado.next()){
                String nome = resultado.getString("nome");
                String email = resultado.getString("email");
                int idade = resultado.getInt("idade");

                System.out.println("Aluno Encontrado:");
                System.out.println("Nome: " + nome);
                System.out.println("Email: " + email);
                System.out.println("Idade: " + idade);
            } else{
                System.out.println("Nenhum aluno encontrado com esse ID");
            }
        } catch (SQLException e){
            System.out.println("Error ao buscar aluno: " + e.getMessage());
        }
    }
}