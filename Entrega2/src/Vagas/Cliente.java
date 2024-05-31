package Vagas;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
    	Scanner entradaTeclado = new Scanner(System.in);
    	System.out.println("Qual IP para conexão: ");
    	final String SERVER_IP = entradaTeclado.nextLine();
        final int PORT = 22222;
        try (Socket socket = new Socket(SERVER_IP, PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server.");
            Scanner scanner = new Scanner(System.in);
            String operation;
            System.out.println("\n(1) User (2)Company");
        	int i = entradaTeclado.nextInt();
            do {
            	
            	if(i == 1) {
                System.out.println("\nEscolha uma opção:");
                System.out.println("1. Login");
                System.out.println("2. Logout");
                System.out.println("3. Cadastrar");
                System.out.println("4. Visualizar");
                System.out.println("5. Atualizar");
                System.out.println("6. Apagar");
                System.out.println("0. Exit");
                System.out.print("Enter operation number: ");}
            	else {System.out.println("\nEscolha uma opção:");
            	System.out.println("10. Login");
                System.out.println("20. Logout");
                System.out.println("30. Cadastrar Empresa");
                System.out.println("40. Visualizar Empresa");
                System.out.println("50. Atualizar Empresa");
                System.out.println("60. Apagar Empresa");
                System.out.println("0. Exit");}
                operation = scanner.nextLine();

                switch (operation) {
                    case "1":
                        loginOperation(scanner, writer, reader);
                        break;
                    case "2":
                        logoutOperation(scanner, writer, reader);
                        break;
                    case "3":
                        registerOperation(scanner, writer, reader);
                        break;
                    case "4":
                        viewOperation(scanner, writer, reader);
                        break;
                    case "5":
                        updateOperation(scanner, writer, reader);
                        break;
                    case "6":
                        deleteOperation(scanner, writer, reader);
                        break;
                    case "10":
                        loginOperationEmpresa(scanner, writer, reader);
                        break;
                    case "20":
                        logoutOperation(scanner, writer, reader);
                        break;
                    case "30":
                        registerOperationEmpresa(scanner, writer, reader);
                        break;
                    case "40":
                        viewOperationEmpresa(scanner, writer, reader);
                        break;
                    case "50":
                        updateOperationEmpresa(scanner, writer, reader);
                        break;
                    case "60":
                        deleteOperationEmpresa(scanner, writer, reader);
                        break;
                    case "0":
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid operation number. Please try again.");
                }
            } while (!operation.equals("0"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loginOperation(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nLogin:");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String senha = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "loginCandidato");
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("senha", senha);
        System.out.println("Enviado: {" + "operacao, " + "loginCandidato, " + email + ", " + senha +"}");
        sendRequest(writer, jsonObject, reader);
    }

    private static void logoutOperation(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nLogout:");
        System.out.print("Enter token: ");
        String token = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "logout");
        jsonObject.addProperty("token", token);
        System.out.println("Enviado: {" + "operacao, " + "logout, " + token +"}");
        sendRequest(writer, jsonObject, reader);
    }

    private static void registerOperation(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nRegister:");
        System.out.print("Enter name: ");
        String nome = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String senha = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "cadastrarCandidato");
        jsonObject.addProperty("nome", nome);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("senha", senha);
        System.out.println("Enviado: {" + "operacao, " + "cadastrarCandidato, " + nome + ", " + email + ", " + senha +"}");
        sendRequest(writer, jsonObject, reader);
    }

    private static void viewOperation(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nView:");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "visualizarCandidato");
        jsonObject.addProperty("email", email);
        System.out.println("Enviado: {" + "operacao, " + "visualizarCandidato, " +  email + "}");
        sendRequest(writer, jsonObject, reader);
    }

    private static void updateOperation(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nUpdate:");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new name: ");
        String nome = scanner.nextLine();
        System.out.print("Enter new password: ");
        String senha = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "atualizarCandidato");
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("nome", nome);
        jsonObject.addProperty("senha", senha);
        System.out.println("Enviado: {" + "operacao, " + "atualizarCandidato, " + nome + ", " + email + ", " + senha +"}");
        sendRequest(writer, jsonObject, reader);
    }

    private static void deleteOperation(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nDelete:");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "apagarCandidato");
        jsonObject.addProperty("email", email);
        System.out.println("Enviado: {" + "operacao, " + "apagarCandidato, " + email + "}");
        sendRequest(writer, jsonObject, reader);
    }

    private static void sendRequest(PrintWriter writer, JsonObject jsonObject, BufferedReader reader) throws IOException {
        String jsonRequest = gson.toJson(jsonObject);
        writer.println(jsonRequest);
        String response = reader.readLine();
        System.out.println("Server response: " + response);
    }


    private static void loginOperationEmpresa(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
    System.out.println("\nLogin:");
    System.out.print("Enter email: ");
    String email = scanner.nextLine();
    System.out.print("Enter password: ");
    String senha = scanner.nextLine();

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("operacao", "loginEmpresa");
    jsonObject.addProperty("email", email);
    jsonObject.addProperty("senha", senha);
    System.out.println("Enviado: {" + "operacao, " + "loginEmpresa, " + email + ", " + senha + "}");
    sendRequest(writer, jsonObject, reader);
}

    private static void registerOperationEmpresa(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nRegister:");
        System.out.print("Enter Razão Social: ");
        String razaoSocial = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter cnpj: ");
        String cnpj = scanner.nextLine();
        System.out.print("Enter senha: ");
        String senha = scanner.nextLine();
        System.out.print("Enter descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Enter ramo: ");
        String ramo = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "cadastrarEmpresa");
        jsonObject.addProperty("razaoSocial", razaoSocial);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("cnpj", cnpj);
        jsonObject.addProperty("senha", senha);
        jsonObject.addProperty("descricao", descricao);
        jsonObject.addProperty("ramo", ramo);
        System.out.println("Enviado: {"+ "operacao, " + "cadastrarEmpresa, " + razaoSocial + ", " + email + ", " + cnpj + ", " + senha + ", " + descricao + ", " + ramo +"}");
        sendRequest(writer, jsonObject, reader);
    }

    private static void viewOperationEmpresa(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nView:");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "visualizarEmpresa");
        jsonObject.addProperty("email", email);
        System.out.println("Enviado: {" + "operacao, " + "visualizarEmpresa, " + email + "}");
        sendRequest(writer, jsonObject, reader);
    }

    private static void updateOperationEmpresa(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nUpdate:");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter nova Razão Social: ");
        String razaoSocial = scanner.nextLine();
        System.out.print("Enter novo Cnpj: ");
        String cnpj = scanner.nextLine();
        System.out.print("Enter nova Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Enter nova Descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Enter novo Ramo: ");
        String ramo = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "atualizarEmpresa");
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("razaoSocial", razaoSocial);
        jsonObject.addProperty("cnpj", cnpj);
        jsonObject.addProperty("senha", senha);
        jsonObject.addProperty("descricao", descricao);
        jsonObject.addProperty("ramo", ramo);
        System.out.println("Enviado: {"+ "operacao, " + "atualizarEmpresa, " + email + ", " + razaoSocial + ", " +  cnpj + ", " + senha + ", " + descricao + ", " + ramo +"}");
        sendRequest(writer, jsonObject, reader);
    }
    
    private static void deleteOperationEmpresa(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\nDelete:");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("operacao", "apagarEmpresa");
        jsonObject.addProperty("email", email);
        System.out.println("Enviado: {" + "operacao, " + "apagarEmpresa, " + email + "}");
        sendRequest(writer, jsonObject, reader);
    }

}