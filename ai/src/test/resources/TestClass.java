public class TestClass {
    public void methodOne() {
        System.out.println("Method One");
    }

    private int methodTwo() {
        int result = 42;
        return result;
    }

    public static String staticMethod(String input) {
        return "Hello " + input;
    }

    private void methodWithInnerClass() {
        class InnerClass {
            public void innerMethod() {
                System.out.println("Inner Method");
            }
        }
        InnerClass inner = new InnerClass();
        inner.innerMethod();
    }
}
