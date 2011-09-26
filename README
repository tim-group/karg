_Karg_ is a library that helps you to write Java functions that take keyword arguments, e.g.:

```java
class Example {

    private static final Keyword<String> GREETING = Keyword.newKeyword();
    private static final Keyword<String> NAME = Keyword.newKeyword();

    public void greet(KeywordArgument...argArray) {
        KeywordArguments args = KeywordArguments.from(argArray);
        String greeting = GREETING.from(args, "Hello");
        String name = NAME.from(args, "World");
        System.out.println(String.format("%s, %s!", greeting, name));
    }

    public void sayHello() {
        greet();
    }

    public void sayGoodbye() {
        greet(GREETING.of("Goodbye");
    }

    public void campItUp() {
        greet(NAME.of("Sailor");
    }
}
```

It does a few other things as well - look in the tests for examples.

Nat Pryce's [MakeItEasy](http://code.google.com/p/make-it-easy/) does some similar things, and may be what you want instead.
