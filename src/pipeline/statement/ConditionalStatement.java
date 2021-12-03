package pipeline.statement;

import pipeline.condition.PipelineCondition;

public class ConditionalStatement<I> extends PipelineElement<I> {

    public PipelineCondition<I> condition;

    public PipelineElement<I> ifTruePass;

    public ConditionalStatement(PipelineCondition<I> condition) {
        this.condition = condition;
    }

    @Override
    public void run(Object in0) {
        I in = (I) in0;
        if (this.condition.isTrue(in)) {
            this.ifTruePass.run(in);
        }
        else this.next.run(in);
    }

}
