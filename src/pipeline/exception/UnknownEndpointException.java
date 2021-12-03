package pipeline.exception;

public class UnknownEndpointException extends RuntimeException {

    public UnknownEndpointException(String endpointKey) {
        super("Unknown endpoint: '" + endpointKey + "'");
    }

}
