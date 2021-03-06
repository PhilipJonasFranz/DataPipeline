package pipeline.statement;

public class FromStatement<I> extends PipelineStatement<I> {

    public String endpoint;

    public FromStatement(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void run(Object in) {
        this.next.run(in);
    }
}
