package pipeline.statement;

import java.util.List;

public abstract class PipelineElement<I> {

    public PipelineElement<?> next;

    public abstract void run(Object in);

    public void run(List<I> in) {
        for (I element : in)
            this.run(element);
    }

    public void run(I...elements) {
        for (I element : elements)
            this.run(element);
    }

}
