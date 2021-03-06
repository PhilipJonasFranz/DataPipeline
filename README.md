# DataPipeline [![version](https://img.shields.io/badge/0.0.1-grey.svg)](https://semver.org) [![status](https://img.shields.io/badge/development-red.svg)](https://semver.org)

*This little project is mainly used to experiment with Apache Camel-flavoured Builders and Pipelines, dont expect anything revolutionary.*

The data pipeline allows configuring modular blocks into complex workflows and run said workflows with input data from various input sources.

The pipeline consists out of statements, which can perform operations on the current data element or can branch the control flow in the pipeline. Statements can even trigger other pipelines with the current data element. Conditional statements also have conditions, which are configurable building blocks as well. While conditions can only be inserted at some locations, statements can be used everywhere.

## Contents

- [Example](#example)
- [Built in Functionality](#built-in-functionality)

## Example

The following example shows a pipeline that utilizes most of the included pipeline statements and conditions. For more information on the included elements take a look at the [Documentation](#built-in-functionality).

```java
public class Main {
    public static void main(String[] args) {
        PipelineElement<Integer> pipeline = new PipelineStatementBuilder<Integer>()
                .Label("entry")
                .Comment("Start of the pipeline")
                .Map(Object::toString)
                .Log(x -> x)
                .Map(String::length)
                .For().Repeat(2).Then()
                     .If().Any().Dynamic(x -> x == 3).Static(false).End().Then()
                         .Do(x -> System.out.println("Hi: " + x))
                     .End(x -> (Integer) x)
                .End()
                .To("processor").Build();
      
        new PipelineStatementBuilder<Integer>()
                .Label("processor")
                .<Integer>Custom(new VeryEmptyStatement<>())
                .Map(Integer::doubleValue)
                .Log(x -> x)
                .Build();
      
        pipeline.run(List.of(1, 10, 100, 1000));
    }
}
```

## Built in Functionality

The following section gives a brief overview over the built-in statements and conditions.

### Statements

 |    Create with   |        Functionality                                           |   Continued with  |
 | ---------------- | -------------------------------------------------------------- | ----------------- |
 | `Comment(String)`| Insert a comment at this location. Has no functionality        | Statement         |
 | `Do(FLambda)`    | Performs some operation with the current element               | Statement         |
 | `Map(FMapper)`   | Maps the current element to a different type                   | Statement         |
 | `If()`           | Opens a new if-statement scope. Requires closing with `End()`  | Condition         |
 | `For()`          | Opens a new for-statement scope. Requires closing with `End()` | Condition         |
 | `Label(String)`  | Defines this statement as a receiver of `To(String)` statements| Statement         |
 | `To(String)`     | Sends the current element to a `Label(String)` statement       | Statement         |
 | `Custom(PipelineStatement)`| Allows plugging in a custom pipeline statement       | Statement         |
 | `Log(String)`    | Logs the given string to `System.out`                          | Statement         |
 | `Log(FMapper)`   | Uses the mapper to map to String and log to `System.out`       | Statement         |
 | `Log(PrintStream, String)`| Logs the given string to given print stream           | Statement         |
 | `Log(PrintStream, FMapper)`| Uses the mapper to map to String and log to given print stream| Statement         |

### Conditions

 |    Create with   |        Functionality                                           |   Continued with  |
 | ---------------- | -------------------------------------------------------------- | ----------------- |
 | `Static(bool)`   | Create a static condition with a given value                   | Condition         |
 | `Dynamic(FMapper)`| Maps the current element to a bool value                      | Condition         |
 | `All()`          | Requires all conditions in the new scope to match. Close with `End()`| Condition   |
 | `Any()`          | Requires at least one condition in the new scope to match. Close with `End()`| Condition|
 | `End()`          | Closes a scope opened by `All()` or `Any()`                    | Condition         |
 | `Repeat(int)`    | This condition returns true `n` times for a given element      | Condition         |
 | `Then()`         | Ends the condition builder to resume with defining statements  | Statement         |
 
## Running the Pipeline

Using the Builder, after the pipeline is configured the `.Build()` method in the Builder will return the head statement of the pipeline. The pipeline can now be run using `pipeline.run(...)`. Multiple input formats are available. The pipeline can be run asynchronously on a new Thread using `pipeline.runAsync(...)`.
