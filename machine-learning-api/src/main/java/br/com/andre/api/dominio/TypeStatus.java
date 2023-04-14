package br.com.andre.api.dominio;

public enum TypeStatus {

    REQUEST_ERROR("Erro na requisição", 400),
    SERVER_ERROR("Erro no servidor", 500),
    NOT_FOUND("Não encontrado", 404),
    BAD_REQUEST("Requisição inválida", 400),
    UNAUTHORIZED("Não autorizado", 401),
    FORBIDDEN("Proibido", 403),
    INTERNAL_SERVER_ERROR("Erro interno do servidor", 500),
    NOT_IMPLEMENTED("Não implementado", 501),
    BAD_GATEWAY("Gateway inválido", 502),
    SUCESS("Sucesso", 201),
    OK("Ok", 200);


    private String message;
    private int status;

    TypeStatus(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
