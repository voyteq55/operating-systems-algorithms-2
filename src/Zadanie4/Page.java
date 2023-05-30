package Zadanie4;

public class Page {
    private int id;
    private int reference;

    public Page(int id, int reference) {
        this.id = id;
        this.reference = reference;
    }

    public int getReference() {
        return reference;
    }

    public int getIndex() {
        return id;
    }

    public Page copy() {
        return new Page(this.id, this.reference);
    }

}