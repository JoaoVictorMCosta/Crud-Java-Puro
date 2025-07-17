package main;

import dao.UserDAO;
import model.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static UserDAO userDAO = new UserDAO();
    private static User loggedInUser = null; // Para manter o usuário logado

    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
        int option;
        do {
            if (loggedInUser == null) {
                System.out.println("\n=== BEM-VINDO ===");
                System.out.println("1 - Registrar");
                System.out.println("2 - Login");
                System.out.println("0 - Sair");
            } else {
                System.out.println("\n=== OLA, " + loggedInUser.getUsername() + " ===");
                System.out.println("1 - Fazer algo (ex: ver perfil)"); // Exemplo de funcionalidade pós-login
                System.out.println("0 - Logout e Sair"); // Opção para deslogar
            }
            System.out.print("Escolha: ");
            try {
                option = sc.nextInt();
                sc.nextLine(); // Consumir a nova linha

                if (loggedInUser == null) { // Menu pré-login
                    switch (option) {
                        case 1:
                            registerUser();
                            break;
                        case 2:
                            loginUser();
                            break;
                        case 0:
                            System.out.println("Saindo...");
                            break;
                        default:
                            System.out.println("Opção inválida.");
                    }
                } else { // Menu pós-login
                    switch (option) {
                        case 1:
                            System.out.println("Funcionalidade em desenvolvimento para " + loggedInUser.getUsername() + "!");
                            // Aqui você adicionaria as funcionalidades da aplicação
                            break;
                        case 0:
                            loggedInUser = null; // Desloga o usuário
                            System.out.println("Logout realizado. Saindo...");
                            break;
                        default:
                            System.out.println("Opção inválida.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.nextLine(); // Consumir a entrada inválida
                option = -1; // Para garantir que o loop continue
            }
        } while (option != 0 || loggedInUser != null); // Continua se não for 0 ou se estiver logado
        sc.close(); // Fecha o scanner ao sair da aplicação
    }

    private static void registerUser() {
        System.out.println("\n--- REGISTRAR NOVO USUARIO ---");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        User newUser = new User(username, password);
        userDAO.register(newUser);
    }

    private static void loginUser() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        User user = userDAO.login(username, password);
        if (user != null) {
            loggedInUser = user;
            System.out.println("Login bem-sucedido! Bem-vindo, " + user.getUsername() + "!");
        } else {
            System.out.println("Falha no login. Nome de usuário ou senha incorretos.");
        }
    }
}