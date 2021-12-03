package pipeline.condition;

import java.util.List;

public class AndCondition<I> extends PipelineCondition<I> {

    private final List<PipelineCondition<I>> conditions;

    public AndCondition(List<PipelineCondition<I>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean isTrue(I in) {
        return this.conditions.stream().allMatch(x -> x.isTrue(in));
    }

}
