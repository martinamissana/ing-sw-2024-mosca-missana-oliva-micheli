package model.commonItem;

public enum Resource implements ItemBox {
    QUILL, INKWELL, MANUSCRIPT;

    @Override
    public String getType() {
        return this.name();
    }
}
