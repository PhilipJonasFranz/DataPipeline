package example;

import pipeline.statement.PipelineStatement;

import java.util.List;

public class Main {

    public static void main(String[]args) {
        PipelineStatement<Integer> pipeline = PipelineStatement.<Integer>builder()
                .Label("entry")
                .Comment("Start of the pipeline")
                .Map(Object::toString)
                .Map(String::length)
                .For().Repeat(2).Then()
                    .If().Any().Dynamic(x -> x == 3).Static(false).End().Then()
                        .Log(x -> "Hi: " + x)
                    .End(x -> (Integer) x)
                    .Log(Object::toString)
                .End()
                .To("processor").Build();

        PipelineStatement.<Integer>builder()
                .Label("processor")
                .<Integer>Custom(new VeryEmptyStatement<>())
                .Map(Integer::doubleValue)
                .Log(Object::toString)
                .Build();

        pipeline.runAsync(List.of(1, 10, 100, 1000));
    }

}
