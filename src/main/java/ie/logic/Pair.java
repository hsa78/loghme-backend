package ie.logic;

public class Pair<A , B > {
    public final A a;
    public final B b;

    public Pair(A a, B b){
        this.a = a;
        this.b = b;
    }

    public A getFirst(){
        return a;
    }

    public B getSecond(){
        return b;
    }
}
