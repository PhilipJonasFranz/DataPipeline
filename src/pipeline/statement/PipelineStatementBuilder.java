package pipeline.statement;

import pipeline.AbstractPipelineBuilder;
import pipeline.exception.MalformedPipelineException;
import pipeline.interfaces.FLambda;
import pipeline.interfaces.FMapper;
import pipeline.condition.PipelineConditionBuilder;
import pipeline.routing.PipelineRegistry;

import java.io.PrintStream;
import java.util.Stack;

public class PipelineStatementBuilder<I> extends AbstractPipelineBuilder<I> {

    public PipelineStatement head;

    private Stack<PipelineStatement> elements = new Stack<>();

    public PipelineStatementBuilder() {
        super(null);

        this.head = new EmptyStatement<I>();
        this.elements.push(head);
    }

    private PipelineStatementBuilder(PipelineStatement head, Stack<PipelineStatement> elements) {
        super(null);

        this.head = head;
        this.elements = elements;
    }

    /**
     * Labels this pipeline statement so it can be targeted by other pipeline statements
     * with {@link #To(String)}.
     * @param endpoint The Key for this endpoint. Must be unique across all other labels.
     */
    public PipelineStatementBuilder<I> Label(String endpoint) {
        FromStatement<I> from = new FromStatement<>(endpoint);
        PipelineRegistry.getInstance().registerPipeline(from);

        return this.add(from);
    }

    /**
     * Sends the current element to the pipeline statement that is labeled with the given
     * endpoint string. If the endpoint does not exist, an exception is thrown. After the
     * pipeline exits, the current pipeline resumes execution at the location it left off.
     * @param endpoint The pipeline label that is defined in a {@link #Label(String)} statement.
     */
    public PipelineStatementBuilder<I> To(String endpoint) {
        return this.add(new ToStatement<>(endpoint));
    }

    /**
     * Allows plugging in a custom pipeline statement.
     * @param custom The custom pipeline statement.
     * @param <O> The output type of the statement.
     */
    public <O> PipelineStatementBuilder<O> Custom(PipelineStatement<I> custom) {
        return this.addT(custom);
    }

    /**
     * Maps an element to a different type using the provided FMapper interface.
     * @param functionalMapper The mapper that performs the transformation.
     * @param <O> The output type of the mapper.
     */
    public <O> PipelineStatementBuilder<O> Map(FMapper<I, O> functionalMapper) {
        return this.addT(new MapperStatement<>(functionalMapper));
    }

    /**
     * Performs an operation for a given element. Does not change the elements type.
     * @param lambda The lambda to do the operation on/with the element.
     */
    public PipelineStatementBuilder<I> Do(FLambda<I> lambda) {
        return this.add(new LambdaStatement<>(lambda));
    }

    /**
     * Creates a log message based on the current element. A mapper creates the message
     * as a string. The message is logged to System.out.
     */
    public PipelineStatementBuilder<I> Log(FMapper<I, String> mapper) {
        return this.add(new LogStatement<>(System.out, mapper));
    }

    /**
     * Logs the given message to System.out.
     */
    public PipelineStatementBuilder<I> Log(String message) {
        return this.add(new LogStatement<>(System.out, x -> message));
    }

    /**
     * Creates a log message based on the current element. A mapper creates the message
     * as a string. The message is logged to the given PrintStream.
     */
    public PipelineStatementBuilder<I> Log(PrintStream out, FMapper<I, String> mapper) {
        return this.add(new LogStatement<>(out, mapper));
    }

    /**
     * Logs the given message to the given PrintStream.
     */
    public PipelineStatementBuilder<I> Log(PrintStream out, String message) {
        return this.add(new LogStatement<>(out, x -> message));
    }

    /**
     * Opens a new scope for an if-statement in the pipeline. When closing this scope
     * with {@link #End()}, the body of the if-statement will point to the next statement
     * in the pipeline outside the if-statement's scope. The statement is only executed
     * if the condition is true.
     * @return A condition builder to build the condition for the loop.
     */
    public PipelineConditionBuilder<I> If() {
        this.handleConditional(new IfStatement<>(null));
        return new PipelineConditionBuilder<>(this);
    }

    /**
     * Opens a new scope for a loop in the pipeline. When closing this scope
     * with {@link #End()}, the loop body will point back to the loop head.
     * The loop is executed while the condition of the loop is true.
     * @return A condition builder to build the condition for the loop.
     */
    public PipelineConditionBuilder<I> For() {
        this.handleConditional(new ForStatement<>(null));
        return new PipelineConditionBuilder<>(this);
    }

    /**
     * Closes a scope opened by a conditional statement. Inserts an empty statement
     * that casts different types that arrive from different pipeline statements to
     * a common type. Returns a builder with the new common type.
     * @param <O> The common output type that all elements have after merging at this point.
     */
    public <O> PipelineStatementBuilder<O> End() {
        this.handleConditional(new EmptyStatement<O>());
        return new PipelineStatementBuilder<>(this.head, this.elements);
    }

    /**
     * Closes a scope opened by a conditional statement. Uses a mapper to convert possibly different types
     * that arrive from different pipeline statements to a common type. Returns a builder with the new common
     * type.
     * @param fMapper A mapper that maps all incoming types to a common type.
     * @param <O> The common output type that all elements have after merging at this point.
     */
    public <O> PipelineStatementBuilder<O> End(FMapper<Object, O> fMapper) {
        this.handleConditional(new MapperStatement<>(fMapper));
        return new PipelineStatementBuilder<>(this.head, this.elements);
    }

    /**
     * Inserts a comment at this point in the pipeline. Does not modify the pipeline.
     * @param comment The comment to insert.
     */
    public PipelineStatementBuilder<I> Comment(String comment) {
        return this;
    }

    /**
     * Returns the entry to the pipeline.
     * @param <O> The type the entry elements to the pipeline have.
     */
    public <O> PipelineStatement<O> Build() {
        return this.head;
    }

    private void handleConditional(ConditionalStatement<I> cond) {
        EmptyStatement<I> empty = new EmptyStatement<>();
        empty.next = cond;

        this.elements.pop().next = empty;

        this.elements.push(empty);
        this.elements.push(cond);
    }

    private void handleConditional(PipelineStatement element) {
        if (this.elements.size() < 2)
            throw new MalformedPipelineException();

        PipelineStatement content = this.elements.pop();
        EmptyStatement empty = (EmptyStatement) this.elements.pop();

        if (empty.next instanceof IfStatement cond) {
            cond.ifTruePass = cond.next;

            cond.next = element;
            content.next = element;
        }
        else if (empty.next instanceof ForStatement loop) {
            loop.ifTruePass = loop.next;

            loop.next = element;
            content.next = loop;
        }

        elements.push(element);
    }

    public void closeCondition(PipelineConditionBuilder<I> builder) {
        ConditionalStatement<I> cond = (ConditionalStatement<I>) this.elements.peek();
        cond.condition = builder.getCond();
    }

    private PipelineStatementBuilder<I> add(PipelineStatement<I> statement) {
        this.elements.pop().next = statement;
        this.elements.push(statement);
        return this;
    }

    private <O> PipelineStatementBuilder<O> addT(PipelineStatement<I> statement) {
        this.elements.pop().next = statement;
        this.elements.push(statement);
        return new PipelineStatementBuilder<>(this.head, this.elements);
    }

}
