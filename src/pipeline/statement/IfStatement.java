package pipeline.statement;

import pipeline.condition.PipelineCondition;

public class IfStatement<I> extends ConditionalStatement<I> {

    public IfStatement(PipelineCondition<I> condition) {
        super(condition);
    }

}
