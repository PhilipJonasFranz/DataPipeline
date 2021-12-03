package example;

import pipeline.statement.PipelineStatement;
import pipeline.statement.PipelineStatementBuilder;

import java.util.List;

public class Main {

    public static void main(String[]args) {
        List<Integer> input = List.of(1, 10, 100, 1000);

        PipelineStatement<Integer> pipeline = new PipelineStatementBuilder<Integer>()
                .Label("entry")
                .Comment("Start of the pipeline")
                .Map(Object::toString)
                .Map(String::length)
                .For().Repeat(2).Then()
                    .If().Dynamic(x -> x == 3).Then()
                        .Do(x -> System.out.println("Hi: " + x))
                    .End(x -> (Integer) x)
                    .Do(System.out::println)
                .End()
                .To("processor").Build();

        new PipelineStatementBuilder<Integer>()
                .Label("processor")
                .<Integer>Custom(new VeryEmptyStatement<>())
                .Map(Integer::doubleValue)
                .Do(System.out::println)
                .Build();

        pipeline.run(input);
    }

}
