package pipeline.statement;

import java.util.List;

public abstract class PipelineStatement<I> {

    public PipelineStatement<?> next;

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
