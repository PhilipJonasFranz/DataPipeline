# DataPipeline [![version](https://img.shields.io/badge/0.0.1-grey.svg)](https://semver.org) [![status](https://img.shields.io/badge/development-red.svg)](https://semver.org)

The data pipeline allows to configure modular blocks into complex workflows and run said workflows with input data from various input sources.

The pipeline consists out of statements, which can perform operations on the current data element or can branch the control flow in the pipeline. Statements can even trigger other pipelines with the current data element. Conditional statements also have conditions, which are configurable building blocks as well. While conditions can only be inserted at some locations, statements can be used everywhere.

## Contents

- [Built in Functionality](#built-in-functionality)

## Example

The following example shows a pipeline that utilizes most of the included pipeline statements and conditions. For more information on the included elements take a look at the [Documentation](#built-in-functionality).

```java
PipelineElement<Integer> pipeline = new PipelineStatementBuilder<Integer>()
    .Label("entry")
    .Comment("Start of the pipeline")
    .Map(Object::toString)
    .Map(String::length)
    .For().Repeat(2).Then()
        .If().Any().Dynamic(x -> x == 3).Static(false).End().Then()
            .Do(x -> System.out.println("Hi: " + x))
        .End(x -> (Integer) x)
        .Do(System.out::println)
    .End()
    .To("processor").Build();
  
new PipelineStatementBuilder<Integer>()
    .Label("processor")
    .Map(Integer::doubleValue)
    .Do(System.out::println)
    .Build();

pipeline.run(List.of(1, 10, 100, 1000));
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
 | `Label(String)`  | Defines this statement as a reciever of `To(String)` statements| Statement         |
 | `To(String)`     | Sends the current element to a `Label(String)` statement       | Statement         |
 
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
 
