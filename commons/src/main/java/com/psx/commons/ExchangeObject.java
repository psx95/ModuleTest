package com.psx.commons;

/**
 * Exchange object is used to exchange data between two modules.
 */
public class ExchangeObject {
    public Object[] data;
    public String type;
    public Modules to;
    public Modules from;

    public ExchangeObject(Object[] data, String type, Modules to, Modules from) {
        this.data = data;
        this.type = type;
        this.to = to;
        this.from = from;
    }
}
