package pipeline.statement;

import java.util.List;

public abstract class PipelineStatement<I> {

    /**
     * The next statement to be called after this pipeline statement.
     * May be null.
     */
    public PipelineStatement<?> next;

    /**
     * Run the pipeline with the given input on this thread.
     * @param element The input element to the pipeline.
     */
    public abstract void run(Object element);

    /**
     * Run the pipeline on a new thread with the given input.
     * @param element The input element to the pipeline.
     */
    public void runAsync(Object element) {
        PipelineStatement<I> pipeline = this;
        Thread runThread = new Thread(new Runnable() {
            @Override
            public void run() {
                pipeline.run(element);
            }
        });

        runThread.start();
    }

    /**
     * Run the pipeline for all the elements in the list.
     */
    public void run(List<I> elements) {
        for (I element : elements)
            this.run(element);
    }

    /**
     * Run the pipeline for each element in the list on a new
     * thread.
     */
    public void runAsync(List<I> elements) {
        for (I element : elements)
            runAsync(element);
    }

    /**
     * Run the pipeline for all passed elements in the array.
     */
    public void run(I...elements) {
        for (I element : elements)
            this.run(element);
    }

    /**
     * Run the pipeline for each element in the array on a new
     * thread.
     */
    public void runAsync(I...elements) {
        for (I element : elements)
            runAsync(element);
    }

    /**
     * Create a new Builder instance to define a new Pipeline.
     * @param <I> The input type to the pipeline.
     * @return A builder with the current input type of I.
     */
    public static <I> PipelineStatementBuilder<I> builder() {
        return new PipelineStatementBuilder<I>();
    }

}
