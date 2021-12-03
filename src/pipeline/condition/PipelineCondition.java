package pipeline.condition;

public abstract class PipelineCondition<I> {

    public abstract boolean isTrue(I in);

}
