package br.com.aurum.astrea.service;


/**
 * Classe responsavel por representar o JSON que sera retornado caso aconteca algum erro
 * durante a requisicao da API, o erro pode estar relacionado a algum problema de validacao, algum bug de codificacao
 * ou algum erro inesperado.
 */
public class ErrorResult {

    private String message;

    public ErrorResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
