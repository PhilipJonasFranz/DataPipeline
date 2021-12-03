package pipeline.routing;

import pipeline.statement.FromStatement;

import java.util.HashMap;

public class PipelineRegistry {

    private static final PipelineRegistry registry = new PipelineRegistry();

    private HashMap<String, FromStatement> registeredPipelines = new HashMap<>();

    public static PipelineRegistry getInstance() {
        return registry;
    }

    private PipelineRegistry() {

    }

    public FromStatement getPipeline(String endpoint) {
        return this.registeredPipelines.get(endpoint);
    }

    public void registerPipeline(FromStatement from) {
        this.registeredPipelines.put(from.endpoint, from);
    }

}
