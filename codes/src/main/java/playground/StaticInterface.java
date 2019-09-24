package playground;



interface StaticInterface {
    static String foo() {
        return "foo" + Hidden.bar();
    }

}

class Hidden {
    static String bar() {
        return "bar";
    }
}

