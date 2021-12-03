package pipeline.statement;

public class FromStatement<I> extends PipelineElement<I> {

    public String endpoint;

    public FromStatement(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void run(Object in) {
        this.next.run(in);
    }
}
