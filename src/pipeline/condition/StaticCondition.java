package pipeline.condition;

public class StaticCondition<I> extends PipelineCondition<I> {

    private boolean value;

    public StaticCondition(boolean value) {
        this.value = value;
    }

    @Override
    public boolean isTrue(I in) {
        return this.value;
    }

}
