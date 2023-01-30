package vn.unicloud.genericqueue.server.algorithm;

import java.util.AbstractQueue;

public abstract class GenericQueue<E> extends AbstractQueue<E> {
    public abstract void setId(String id);
    public abstract String getId();

}
