package pipeline.condition;

import pipeline.AbstractPipelineBuilder;
import pipeline.exception.MalformedConditionException;
import pipeline.interfaces.FMapper;
import pipeline.statement.PipelineStatementBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PipelineConditionBuilder<I> extends AbstractPipelineBuilder<I> {

    private enum OP {

        AND,

        OR;

    }

    private final Stack<List<PipelineCondition<I>>> conditionStack = new Stack<>();

    private final Stack<OP> operators = new Stack<>();

    public PipelineConditionBuilder(AbstractPipelineBuilder<I> parentBuilder) {
        super(parentBuilder);
        conditionStack.add(new ArrayList<>());
    }

    public PipelineConditionBuilder<I> Static(boolean value) {
        this.conditionStack.peek().add(new StaticCondition<I>(value));
        return this;
    }

    /**
     * Implement a dynamic condition that takes the current element and decides based on the element
     * of the outcome of the condition.
     * @param mapper A mapper that maps the object to a Boolean value.
     */
    public PipelineConditionBuilder<I> Dynamic(FMapper<I, Boolean> mapper) {
        this.conditionStack.peek().add(new DynamicCondition<>(mapper));
        return this;
    }

    /**
     * Returns true for the given number of times. Each time the condition is evaluated for a given
     * Object passing through the pipeline, the counter for this object is decremented. If the counter
     * hits zero, the condition will return true, but will return false the next time it is evaluated.
     * @param times The number of times this condition should return true.
     */
    public PipelineConditionBuilder<I> Repeat(int times) {
        this.conditionStack.peek().add(new RepeatCondition<>(times));
        return this;
    }

    public PipelineConditionBuilder<I> Custom(PipelineCondition<I> customCondition) {
        this.conditionStack.peek().add(customCondition);
        return this;
    }

    public PipelineConditionBuilder<I> Any() {
        this.conditionStack.push(new ArrayList<>());
        this.operators.push(OP.OR);
        return this;
    }

    public PipelineConditionBuilder<I> All() {
        this.conditionStack.push(new ArrayList<>());
        this.operators.push(OP.AND);
        return this;
    }

    public PipelineConditionBuilder<I> End() {
        if (this.operators.isEmpty() || this.conditionStack.isEmpty())
            throw new MalformedConditionException();

        List<PipelineCondition<I>> conditions = conditionStack.pop();
        OP operator = operators.pop();

        if (operator == OP.AND) {
            AndCondition<I> cond = new AndCondition<I>(conditions);
            conditionStack.peek().add(cond);
        }
        else {
            OrCondition<I> cond = new OrCondition<I>(conditions);
            conditionStack.peek().add(cond);
        }

        return this;
    }

    /**
     * Ends the condition builder to return to the statement builder.
     * @return The statement builder to continue declaring statements.
     */
    public PipelineStatementBuilder<I> Then() {
        PipelineStatementBuilder<I> parent = (PipelineStatementBuilder<I>) this.parentBuilder;

        parent.closeCondition(this);

        return parent;
    }

    public PipelineCondition<I> getCond() {
        return this.conditionStack.peek().get(0);
    }

}
