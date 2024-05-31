package Vagas;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Servidor {
    private static final int PORT = 22222;
    private static Map<String, Candidato> users = new HashMap<>();
    private static Map<String, Empresa> companys = new HashMap<>();
    private static Map<String, String> loggedInUsers = new HashMap<>();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    JsonObject jsonObject = gson.fromJson(inputLine, JsonObject.class);
                    String operacao = jsonObject.get("operacao").getAsString();
                    Response response = processRequest(operacao, jsonObject);
                    String jsonResponse = gson.toJson(response);
                    writer.println(jsonResponse);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Response processRequest(String operacao, JsonObject jsonObject) {
            switch (operacao) {
                case "loginCandidato":
                    return loginCandidato(jsonObject);
                case "logout":
                    return logout(jsonObject);
                case "cadastrarCandidato":
                    return cadastrarCandidato(jsonObject);
                case "visualizarCandidato":
                    return visualizarCandidato(jsonObject);
                case "atualizarCandidato":
                    return atualizarCandidato(jsonObject);
                case "apagarCandidato":
                    return apagarCandidato(jsonObject);
                case "loginEmpresa":
                    return loginEmpresa(jsonObject);
                case "cadastrarEmpresa":
                    return cadastrarEmpresa(jsonObject);
                case "visualizarEmpresa":
                    return visualizarEmpresa(jsonObject);
                case "atualizarEmpresa":
                    return atualizarEmpresa(jsonObject);
                case "apagarEmpresa":
                    return apagarEmpresa(jsonObject);
                default:
                    return new Response(operacao, 401, "Unknown operation", null, null, null);
            }
        }

        private Response loginCandidato(JsonObject jsonObject) {
            String email = jsonObject.get("email").getAsString();
            String senha = jsonObject.get("senha").getAsString();

            if (!isValidEmail(email)) {
            	System.out.println("Formato de email ou senha invalido: { 400 }");
                return new Response("loginCandidato", 400, "Formato de email ou senha invalido", null, null, null);
            }

            if (users.containsKey(email)) {
                Candidato candidato = users.get(email);
                if (candidato.getSenha().equals(senha)) {
                    String token = UUID.randomUUID().toString();
                    loggedInUsers.put(token, email);
                    System.out.println("Login efetuado retornado: {" + token + "}");
                    return new Response("loginCandidato", 200, "Login successful", token, null, null);
                } else {
                	System.out.println("Login ou senha incorreto: { 401 }");
                    return new Response("loginCandidato", 401, "Login ou senha incorreto", null, null, null);
                }
            } else {
            	System.out.println("Login ou senha incorreto: { 401 }");
                return new Response("loginCandidato", 401, "Login ou senha incorreto", null, null, null);
            }
        }

        private Response logout(JsonObject jsonObject) {
            String token = jsonObject.get("token").getAsString();
            if (loggedInUsers.containsKey(token)) {
                loggedInUsers.remove(token);
                System.out.println("Logout realizado com sucesso");
                return new Response("logout", 204, "Logout successful", null, null, null);
            } else {
            	System.out.println("Token invalido: { 401 }");
                return new Response("logout", 401, "Invalid token", null, null, null);
            }
        }

        private Response cadastrarCandidato(JsonObject jsonObject) {
            String nome = jsonObject.get("nome").getAsString();
            String email = jsonObject.get("email").getAsString();
            String senha = jsonObject.get("senha").getAsString();

            if (!isValidEmail(email)) {
            	System.out.println("Formato de Email ou senha invalido: { 404 }");
                return new Response("cadastrarCandidato", 404, "Formato de email ou senha invalido", null, null, null);
            }

            if (users.containsKey(email)) {
            	System.out.println("Email já cadastrado: {" + email + "}");
                return new Response("cadastrarCandidato", 422, "Email já cadastrado", null, null, null);
            }

            Candidato newCandidato = new Candidato(nome, email, senha);
            users.put(email, newCandidato);
            String token = UUID.randomUUID().toString();
            loggedInUsers.put(token, email);
            System.out.println(new Response("cadastrarCandidato", 201, "Registration successful", token, null, null));
            System.out.println("Cadastro realizado com sucesso: {" + token + "}");
            return new Response("cadastrarCandidato", 201, "Registration successful", token, null, null);
        }

        private Response visualizarCandidato(JsonObject jsonObject) {
            String email = jsonObject.get("email").getAsString();
            if (users.containsKey(email)) {
                Candidato candidato = users.get(email);
                System.out.println("Canditado encontrado: {" + candidato + "}");
                return new Response("visualizarCandidato", 201, "Canditado encontrado", null, candidato, null);
            } else {
            	System.out.println("Candidato não encontrado: { 404 }");
                return new Response("visualizarCandidato", 404, "Candidato não encontrado", null, null, null);
            }
        }

        private Response atualizarCandidato(JsonObject jsonObject) {
            String email = jsonObject.get("email").getAsString();
            String nome = jsonObject.get("nome").getAsString();
            String senha = jsonObject.get("senha").getAsString();

            if (!isValidEmail(email)) {
            	System.out.println("Formato de email ou senha invalido: { 400 }");
                return new Response("atualizarCandidato", 400, "Formato de email ou senha invalido", null, null, null);
            }

            if (users.containsKey(email)) {
                Candidato candidato = users.get(email);
                candidato.setNome(nome);
                candidato.setSenha(senha);
                System.out.println("Candidato Atualizado: {" + candidato + "}");
                return new Response("atualizarCandidato", 201, "Candidato Atualizado", null, null, null);
            } else {
            	System.out.println("Candidato não encontrado: { 404 }");
                return new Response("atualizarCandidato", 404, "Candidato não encontrado", null, null, null);
            }
        }

        private Response apagarCandidato(JsonObject jsonObject) {
            String email = jsonObject.get("email").getAsString();
            if (users.containsKey(email)) {
                users.remove(email);
                System.out.println("Canditado deletado");
                return new Response("apagarCandidato", 201, "Candidate deleted", null, null, null);
            } else {
            	System.out.println("Candidato não encontrado: { 404 }");
                return new Response("apagarCandidato", 404, "Candidate not found", null, null, null);
            }
        }
           
        private Response loginEmpresa(JsonObject jsonObject) {
            String email = jsonObject.get("email").getAsString();
            String senha = jsonObject.get("senha").getAsString();

            if (!isValidEmail(email)) {
            	System.out.println("Formato de email ou senha invalido: { 400 }");
                return new Response("loginEmpresa", 400, "Formato de email ou senha invalido", null, null, null);
            }

            if (companys.containsKey(email)) {
                Empresa empresa = companys.get(email);
                if (empresa.getSenha().equals(senha)) {
                    String token = UUID.randomUUID().toString();
                    loggedInUsers.put(token, email);
                    System.out.println("Login efetuado retornado: {" + token + "}");
                    return new Response("loginEmpresa", 200, "Login successful", token, null, null);
                } else {
                	System.out.println("Login ou senha incorreto: { 401 }");
                    return new Response("loginEmpresa", 401, "Login ou senha incorreta", null, null, null);
                }
            } else {
            	System.out.println("Login ou senha incorreto: { 401 }");
                return new Response("loginEmpresa", 401, "Login ou senha incorreta", null, null, null);
            }
        }
        
        private Response cadastrarEmpresa(JsonObject jsonObject) {
            String razaoSocial = jsonObject.get("razaoSocial").getAsString();
            String email = jsonObject.get("email").getAsString();
            String cnpj = jsonObject.get("cnpj").getAsString();
            String senha = jsonObject.get("senha").getAsString();
            String descricao = jsonObject.get("descricao").getAsString();
            String ramo = jsonObject.get("ramo").getAsString();

            if (!isValidEmail(email)) {
            	System.out.println("Formato de email ou senha invalido");
                return new Response("cadastrarEmpresa", 404, "Invalid email or password format", null, null, null);
            }

            if (companys.containsKey(email)) {
            	System.out.println("Email já existe");
                return new Response("cadastrarEmpresa", 422, "Email already exists", null, null, null);
            }

            Empresa newEmpresa = new Empresa(razaoSocial, email, cnpj, senha, descricao, ramo);
            companys.put(email, newEmpresa);
            String token = UUID.randomUUID().toString();
            loggedInUsers.put(token, email);
            System.out.println(new Response("cadastrarEmpresa", 201, "Registration successful", token, null, null));
            System.out.println("Cadastro realizado com sucesso: {" + token + "}");
            return new Response("cadastrarEmpresa", 201, "Registration successful", token, null, null);
        }

        private Response visualizarEmpresa(JsonObject jsonObject) {
            String email = jsonObject.get("email").getAsString();
            if (companys.containsKey(email)) {
                Empresa empresa = companys.get(email);
                System.out.println("Empresa encontrada: {" + empresa + "}");
                return new Response("visualizarEmpresa", 201, "Empresa found", null, null, empresa);
            } else {
            	System.out.println("Candidato não encontrado: { 404 }");
                return new Response("visualizarEmpresa", 404, "Empresa not found", null, null, null);
            }
        }
        
        private Response atualizarEmpresa(JsonObject jsonObject) {
            String email = jsonObject.get("email").getAsString();
            String razaoSocial = jsonObject.get("razaoSocial").getAsString();
            String cnpj = jsonObject.get("cnpj").getAsString();
            String descricao = jsonObject.get("descricao").getAsString();
            String senha = jsonObject.get("senha").getAsString();
            String ramo = jsonObject.get("ramo").getAsString();

            if (!isValidEmail(email)) {
            	System.out.println("Email ou senha invalido");
                return new Response("atualizarEmpresa", 400, "Invalid email or password format", null, null, null);
            }

            if (companys.containsKey(email)) {
                Empresa empresa = companys.get(email);
                empresa.setRazaoSocial(razaoSocial);
                empresa.setCnpj(cnpj);
                empresa.setDescricao(descricao);
                empresa.setSenha(senha);
                empresa.setRamo(ramo);
                System.out.println("Empresa Atualizada: {" + empresa + "}");
                return new Response("atualizarEmpresa", 201, "Empresa updated", null, null, null);
            } else {
            	System.out.println("Empresa não encontrada");
                return new Response("atualizarEmpresa", 404, "Empresa not found", null, null, null);
            }
        }
        
        private Response apagarEmpresa(JsonObject jsonObject) {
            String email = jsonObject.get("email").getAsString();
            if (companys.containsKey(email)) {
                companys.remove(email);
                System.out.println("Empresa deletada");
                return new Response("apagarEmpresa", 201, "Empresa deleted", null, null, null);
            } else {
            	System.out.println("Empresa não encontrada");
                return new Response("apagarEmpresa", 404, "Empresa not found", null, null, null);
            }
        }
        
        private boolean isValidEmail(String email) {
            return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        }
    }
}
