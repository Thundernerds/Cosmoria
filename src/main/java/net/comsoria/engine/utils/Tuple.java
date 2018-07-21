package net.comsoria.engine.utils;

public class Tuple<A,B> {
    public A a;
    public B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public Tuple() {
        this.a = null;
        this.b = null;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public void setA(A a) {
        this.a = a;
    }
}
