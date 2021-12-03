package pipeline.exception;

public class MalformedPipelineException extends RuntimeException {

    public MalformedPipelineException() {
        super("Found malformed pipeline!");
    }

}
