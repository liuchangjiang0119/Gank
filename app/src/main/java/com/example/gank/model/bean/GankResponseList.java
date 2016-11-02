package com.example.gank.model.bean;

import java.util.List;

/**
 * Created by dell on 2016/9/16.
 */
public class GankResponseList<T> {

    private boolean error;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
