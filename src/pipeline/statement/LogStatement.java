package pipeline.statement;

import pipeline.interfaces.FMapper;

import java.io.PrintStream;

public class LogStatement<I> extends PipelineStatement<I> {

    public PrintStream out;

    public FMapper<I, String> mapper;

    public LogStatement(PrintStream out, FMapper<I, String> mapper) {
        this.out = out;
        this.mapper = mapper;
    }

    @Override
    public void run(Object in0) {
        I in = (I) in0;
        this.out.println(this.mapper.map(in));
        if (this.next != null) this.next.run(in);
    }
}
