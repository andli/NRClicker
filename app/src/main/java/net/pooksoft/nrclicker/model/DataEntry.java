package net.pooksoft.nrclicker.model;

public class DataEntry {

    private int resourceId;
    private int value;
    private String label;

    public DataEntry(int resourceId, int value, String label) {
        this.resourceId = resourceId;
        this.value = value;
        this.label = label;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{label: ");
        sb.append(getLabel());
        sb.append(", value: ");
        sb.append(getValue());
        sb.append(", id: ");
        sb.append(getResourceId());
        sb.append("}");
        return sb.toString();
    }
}
