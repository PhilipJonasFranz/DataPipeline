package pipeline.statement;

import pipeline.routing.PipelineRegistry;
import pipeline.exception.UnknownEndpointException;

public class ToStatement<I> extends PipelineStatement<I> {

    public PipelineStatement connectedEndpoint;

    private String endpoint;

    public ToStatement(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void run(Object in) {
        if (this.connectedEndpoint == null) {
            this.connectedEndpoint = PipelineRegistry.getInstance().getPipeline(endpoint);
            if (this.connectedEndpoint == null)
                throw new UnknownEndpointException(this.endpoint);
        }

        this.connectedEndpoint.run(in);

        if (this.next != null)
            this.next.run(in);
    }
}
