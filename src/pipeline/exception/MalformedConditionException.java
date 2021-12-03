package pipeline.exception;

public class MalformedConditionException extends RuntimeException {

    public MalformedConditionException() {
        super("Found malformed condition!");
    }

}
