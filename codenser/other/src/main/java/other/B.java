class B {
    public static void methodB1() {
        System.out.println("methodB1");
    }

    public static void callC() {
        C c = new C();
        int i = 3;
        if (i == 1) {
            c.methodC();
        }
        c.methodC();
    }
}
