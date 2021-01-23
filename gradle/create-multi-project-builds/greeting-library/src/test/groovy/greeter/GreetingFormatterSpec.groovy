package greeter

import spock.lang.Specification

def 'Create a greeting'() {
    expect: 'Greeting to be correctly capitalized'
    GreetingFormatter.greeting('gradlephant') == 'Hello, Gradlephant'
}
