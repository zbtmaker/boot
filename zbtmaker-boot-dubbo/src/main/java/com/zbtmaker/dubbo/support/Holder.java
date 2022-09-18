package com.zbtmaker.dubbo.support;

/**
 * @author zoubaitao
 * date 2022/09/18
 */
public class Holder<T> {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
