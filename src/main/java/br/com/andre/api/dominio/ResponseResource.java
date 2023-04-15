package br.com.andre.api.dominio;

import spark.Response;

import java.util.Objects;

public class ResponseResource<E> {

    private E resource;
    private String messageError;
    private String messageStatus;
    private int status;

    private ResponseResource(E resource, String messageError, TypeStatus typeStatus) {
        this.resource = resource;
        this.messageError = messageError;
        this.messageStatus = typeStatus.getMessage();
        this.status = typeStatus.getStatus();
    }

    public static <E> ResponseResource<E> of(Response response, E resource, TypeStatus typeStatus) {
        response.status(typeStatus.getStatus());
        response.type("application/json");
        return new ResponseResource<>(resource, null, typeStatus);
    }

    public static ResponseResource ofError(Response response, String messageError, TypeStatus typeStatus) {
        response.status(typeStatus.getStatus());
        response.type("application/json");
        return new ResponseResource<>(null, messageError, typeStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseResource<?> that = (ResponseResource<?>) o;

        if (status != that.status) return false;
        if (!Objects.equals(resource, that.resource)) return false;
        if (!Objects.equals(messageError, that.messageError)) return false;
        return Objects.equals(messageStatus, that.messageStatus);
    }

    @Override
    public int hashCode() {
        int result = resource != null ? resource.hashCode() : 0;
        result = 31 * result + (messageError != null ? messageError.hashCode() : 0);
        result = 31 * result + (messageStatus != null ? messageStatus.hashCode() : 0);
        result = 31 * result + status;
        return result;
    }

    @Override
    public String toString() {
        return "ResponseResource{" +
                "resource=" + resource +
                ", messageError='" + messageError + '\'' +
                ", messageStatus='" + messageStatus + '\'' +
                ", status=" + status +
                '}';
    }

}
