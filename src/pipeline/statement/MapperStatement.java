package pipeline.statement;

import pipeline.interfaces.FMapper;

public class MapperStatement<I, O> extends PipelineStatement<I> {

    public FMapper<I, O> mapper;

    public MapperStatement(FMapper<I, O> mapper) {
        this.mapper = mapper;
    }

    @Override
    public void run(Object in0) {
        I in = (I) in0;
        if (this.next != null)
            this.next.run(mapper.map(in));
    }

}
