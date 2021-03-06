package pipeline.statement;

public class EmptyStatement<I> extends PipelineStatement<I> {

    @Override
    public void run(Object in0) {
        I in = (I) in0;
        if (this.next != null)
            this.next.run(in);
    }

}
