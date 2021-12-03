package pipeline.condition;

import java.util.List;

public class OrCondition<I> extends PipelineCondition<I> {

    private final List<PipelineCondition<I>> conditions;

    public OrCondition(List<PipelineCondition<I>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean isTrue(I in) {
        return this.conditions.stream().anyMatch(x -> x.isTrue(in));
    }

}
