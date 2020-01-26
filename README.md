# spring-cloud-stream-message-conversion-bug
Sample application that attempts to demonstrate a bug found in spring cloud stream

The bug manifests when trying to use function composition to compose a function that serves as a message handler.
Specifically when the functions that compose the handler function are class implementations of the Function interface instead of inlined lambdas.

In short the framework is not able to correctly pass a Message<List<Map<String,Object>>> from one function to the other.
The payload List of the passed message will contain all null elements instead of the expected maps.

Class DemoApplicationTests uses integration tests to demonstrate when the function composition fails and where in the downstream code is the actual failure.

