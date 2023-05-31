package Zadanie4;

public class Page {
    private int id;
    private int reference;
    private boolean wasPageFault;

    public Page(int id, int reference) {
        this.id = id;
        this.reference = reference;
        this.wasPageFault = false;
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

    public void markPageFault() {
        wasPageFault = true;
    }

    public boolean wasPageFault() {
        return wasPageFault;
    }

}
