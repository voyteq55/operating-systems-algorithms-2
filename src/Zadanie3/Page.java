package Zadanie3;

public class Page {
    private int id;
    private int reference;
    private int secondChanceBit;

    public Page(int id, int reference) {
        this.id = id;
        this.reference = reference;
        this.secondChanceBit = 1;
    }

    public int getReference() {
        return reference;
    }

    public int getIndex() {
        return id;
    }

    public void setSecondChanceBit(int secondChanceBit) {
        this.secondChanceBit = secondChanceBit;
    }

    public int getSecondChanceBit() {
        return secondChanceBit;
    }

    public Page copy() {
        return new Page(this.id, this.reference);
    }

}
