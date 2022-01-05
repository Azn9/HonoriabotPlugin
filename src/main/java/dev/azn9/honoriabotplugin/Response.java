package dev.azn9.honoriabotplugin;

public class Response {

    private final ResponseStatus status;

    public Response(ResponseStatus status) {
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return this.status;
    }
}
