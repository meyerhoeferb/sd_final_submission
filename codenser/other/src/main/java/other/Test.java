public class Test {
    public static void main(String[] args){

        System.out.println("starting main");
        callMethods();
        System.out.println("Back in main");
    }

    public static void callMethods() {
        A a = new A();
        B b = new B();
        a.methodA1();
        b.methodB1();
        b.callC();
        for(int i = 0; i < 3; i++) {
            a.methodA2();
        }

    }
}
