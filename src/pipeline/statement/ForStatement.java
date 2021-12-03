package pipeline.statement;

import pipeline.condition.PipelineCondition;

public class ForStatement<I> extends ConditionalStatement<I> {

    public ForStatement(PipelineCondition<I> condition) {
        super(condition);
    }

}
