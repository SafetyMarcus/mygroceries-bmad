# Coding Standards

Taken from: https://developer.android.com/kotlin/style-guide

## Source file organization

### File naming

Source file names are PascalCase and match the name of the class, interface, or object defined in the file.

### File structure

Files should be organized as follows:

1.  License or copyright header (if applicable)
2.  File-level annotations
3.  Package statement
4.  Import statements
5.  Top-level declarations

### Class layout

The contents of a class should be organized in the following order:

1.  Property declarations
2.  Initializer blocks
3.  Constructors
4.  Methods
5.  Companion object

### Interface layout

The contents of an interface should be organized in the following order:

1.  Property declarations
2.  Methods
3.  Companion object

## Formatting

### Indentation

Use 4 spaces for indentation. Do not use tabs.

### Line length

Limit lines to 100 characters.

### Wrapping

When a line exceeds the character limit, break it at a higher syntactic level.

### Blank lines

Use blank lines to separate logical blocks of code.

### Spacing

Use spaces around operators, keywords, and after commas.

### Parentheses

Use parentheses for clarity, even when not strictly required.

## Naming

### Packages

Package names are all lowercase, with words separated by dots.

### Classes

Class names are PascalCase.

### Functions

Function names are camelCase.

### Properties

Property names are camelCase.

### Constants

Constant names are SCREAMING_SNAKE_CASE.

## Documentation

### KDoc

Use KDoc for all public classes, functions, and properties.

### Comments

Use comments to explain complex or non-obvious code.

## Language features

### Nullability

Use nullability features to express nullability explicitly.

### Immutability

Prefer immutable data structures and variables.

### Lambdas

Use lambdas for concise and readable code.

### Coroutines

Use coroutines for asynchronous programming.

## Interoperability

### Java

Follow Java coding conventions when interacting with Java code.

### C/C++

Follow C/C++ coding conventions when interacting with C/C++ code.

## Tools

### Lint

Use Lint to check for common coding errors.

### ktlint

Use ktlint to enforce coding style.

## Best practices

### Error handling

Handle errors gracefully.

### Logging

Use logging for debugging and monitoring.

### Testing

Write unit tests for all code.

### Performance

Optimize for performance.

### Security

Consider security implications.

### Accessibility

Design for accessibility.

### Internationalization

Support internationalization.

### Localization

Support localization.

### Version control

Use version control for all code.

### Code reviews

Conduct code reviews.

### Continuous integration

Use continuous integration.

### Continuous delivery

Use continuous delivery.

### Open source

Contribute to open source.

### Community

Engage with the community.

### Learning

Keep learning.

### Mentoring

Mentor others.

### Giving back

Give back to the community.

### Having fun

Have fun!