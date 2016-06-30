# halva-scalaz-examples

One of my favorite (intermediate/advanced) walk throughs of some of the core Monads in Functional Programming is [Learning Scalaz](http://eed3si9n.com/learning-scalaz/), which goes through the equally excellent (intermediate) [Learn You a Haskell For Great Good](http://learnyouahaskell.com/) reimplementing the Haskell examples in Scalaz.

The [Functional Java library](http://www.functionaljava.org/) provides good implementations of most of these core Scalaz constructs in Java, but without much in the way of documentation or example code.

The [Halva](https://github.com/Randgalt/halva) library provides a good implementation of many of the standard core Scala language features (in particular for this purpose, the for comprehension) and enables quite pleasant boiler-plate free implementations of the (Haskell ->) Scala/Scalaz examples in pure Java8, with minimal source-level annotations.

_(Actually in some cases the Java version is a fair bit easier to follow - in return for being slightly more verbose, which is often a price worth paying)._

The actual examples (with links to the original source material) are in the [test package](https://github.com/Alex-At-Home/halva-scalaz-examples/tree/master/src/test/java/person/alexp/halva/examples).

Currently there's:
* [ReaderExample](https://github.com/Alex-At-Home/halva-scalaz-examples/blob/master/src/test/java/person/alexp/halva/examples/ReaderExample.java) - [Reader monad](), including reproducing [Monad Transformers]() (chaining Readers and Optionals), which worked surprisingly nicely thanks to [Halva](https://github.com/Randgalt/halva)
* [WriterExample](https://github.com/Alex-At-Home/halva-scalaz-examples/blob/master/src/test/java/person/alexp/halva/examples/WriterExample.java) - [Writer monad](http://eed3si9n.com/learning-scalaz/Writer.html)
* [StateExample](https://github.com/Alex-At-Home/halva-scalaz-examples/blob/master/src/test/java/person/alexp/halva/examples/StateExample.java) - [State monad](http://eed3si9n.com/learning-scalaz/State.html) (one of my favorites!)
* [LensExample](https://github.com/Alex-At-Home/halva-scalaz-examples/blob/master/src/test/java/person/alexp/halva/examples/LensExample.java)  - Lens class, chained using the [State monad](http://eed3si9n.com/learning-scalaz/Lens.html)
* [ZipperExample](https://github.com/Alex-At-Home/halva-scalaz-examples/blob/master/src/test/java/person/alexp/halva/examples/ZipperExample.java) - [TreeZipper](http://eed3si9n.com/learning-scalaz/Tree.html) and [Zipper](http://eed3si9n.com/learning-scalaz/Zipper.html) classes, chained using Optionals

I'll add others as I get the time.

Depending on the IDE used, it may be necessary to call `mvn clean generate-sources` before (re)building the project.

_(The code in the [source package](https://github.com/Alex-At-Home/halva-scalaz-examples/tree/master/src/main/java/person/alexp/halva/examples) simply uses the `@MonadicFor` annotation to build for comprehension classes specific to each monad, and `@TypeAlias` to build some simple aliases for generic container classes)._
