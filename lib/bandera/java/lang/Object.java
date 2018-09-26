package java.lang;

public class Object {

    public final void wait() throws InterruptedException {}
    public final void wait(long l) throws InterruptedException {}
    public final void wait(long l, int i) throws InterruptedException {}
    public final void notify() {}
    public final void notifyAll() {}
    public final Class getClass() { return(null); }
    public boolean equals(Object o) { return(false); }
    public String toString() { return(""); }
    public int hashCode() { return(0); }

}
