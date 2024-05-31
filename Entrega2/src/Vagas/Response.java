package Vagas;

public class Response {
    private String operacao;
    private int status;
    private String mensagem;
    private String token;
    private Candidato candidato;
    private Empresa empresa;

    public Response(String operacao, int status, String mensagem, String token, Candidato candidato, Empresa empresa) {
        this.operacao = operacao;
        this.status = status;
        this.mensagem = mensagem;
        this.token = token;
        this.candidato = candidato;
        this.empresa = empresa;
    }

    // Getters and setters
    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

}

