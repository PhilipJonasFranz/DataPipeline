package pipeline.statement;

import pipeline.interfaces.FLambda;

public class LambdaStatement<I> extends PipelineStatement<I> {

    public FLambda<I> lambda;

    public LambdaStatement(FLambda<I> lambda) {
        this.lambda = lambda;
    }

    @Override
    public void run(Object in0) {
        I in = (I) in0;
        this.lambda.map(in);
        if (this.next != null)
            this.next.run(in);
    }

}
