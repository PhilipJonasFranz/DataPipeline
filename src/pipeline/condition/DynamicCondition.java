package pipeline.condition;

import pipeline.interfaces.FMapper;

public class DynamicCondition<I> extends PipelineCondition<I> {

    public FMapper<I, Boolean> mapper;

    public DynamicCondition(FMapper<I, Boolean> mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean isTrue(I in) {
        return this.mapper.map(in);
    }

}
