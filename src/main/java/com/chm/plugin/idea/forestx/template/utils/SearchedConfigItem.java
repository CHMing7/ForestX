package com.chm.plugin.idea.forestx.template.utils;

public abstract class SearchedConfigItem<T> {
    protected final String insertion;

    protected final T element;

    protected final boolean isEL;

    protected SearchedConfigItem(String insertion, T element, boolean isEL) {
        this.insertion = insertion;
        this.element = element;
        this.isEL = isEL;
    }

    public String getInsertion() {
        return insertion;
    }

    public T getElement() {
        return element;
    }

    public boolean isEL() {
        return isEL;
    }

    @Override
    public String toString() {
        return insertion;
    }

    public abstract String getItemText();

}
