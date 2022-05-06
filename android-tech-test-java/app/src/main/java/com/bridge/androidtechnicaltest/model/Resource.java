package com.bridge.androidtechnicaltest.model;

public class Resource<T> {

    private final Status status;
    private final T data;
    private final Throwable error;

    Resource(Status status, T data, Throwable errorCode) {
        this.status = status;
        this.data = data;
        this.error = errorCode;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return error;
    }

    public static <T> Resource<T> loading() {
        return new Resource<T>(Status.LOADING, null, null);
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<T>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Throwable error) {
        return new Resource<T>(Status.ERROR, null, error);
    }
}
