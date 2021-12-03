package pipeline;

public abstract class AbstractPipelineBuilder<I> {

    protected AbstractPipelineBuilder parentBuilder;

    public AbstractPipelineBuilder(AbstractPipelineBuilder parentBuilder) {
        this.parentBuilder = parentBuilder;
    }

}
