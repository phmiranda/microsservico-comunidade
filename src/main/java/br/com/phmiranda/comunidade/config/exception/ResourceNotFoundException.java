package br.com.phmiranda.comunidade.config.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " não encontrado para o id " + id + ".");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
