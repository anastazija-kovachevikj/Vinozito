package finki.nichk.logic;

public class StringValuePair {
    private String string;
    private int value;

    public StringValuePair(String string, int value) {
        this.string = string;
        this.value = value;
    }

    public String getString() {
        return string;
    }

    public int getValue() {
        return value;
    }
}