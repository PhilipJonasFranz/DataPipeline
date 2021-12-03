package pipeline.statement;

import pipeline.routing.PipelineRegistry;

public class ToStatement<I> extends PipelineStatement<I> {

    public PipelineStatement connectedEndpoint;

    private String endpoint;

    public ToStatement(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void run(Object in) {
        if (this.connectedEndpoint == null)
            this.connectedEndpoint = PipelineRegistry.getInstance().getPipeline(endpoint);

        this.connectedEndpoint.run(in);

        if (this.next != null)
            this.next.run(in);
    }
}
