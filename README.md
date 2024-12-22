# An Example CLI with Smithy4s + Scala Native

You can read my blog post about this CLI [here](https://blog.lewisjkl.com/smithy4s-cli-scala-native).

- An example CLI that calls out to the [NumbersApi](http://numbersapi.com/#42)
- Built using Smithy4s, Scala Native, Http4s, Ember, Decline
- Requires llvm and s2n installations on your computer

## Running

In SBT Shell:

```console
> run get-number-fact 10000 trivia
```