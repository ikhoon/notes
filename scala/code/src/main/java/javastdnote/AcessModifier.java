package javastdnote;

abstract class AccessModifier {
    abstract void foo();
}

abstract class Extended extends AccessModifier {
    void bar() {
        foo();
    }
}
