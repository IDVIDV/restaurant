package org.example.restaurant.servicelayer;

public class OperationResult<T> {
    private final T result;
    private final Boolean isSuccess;
    private final String failReason;

    public OperationResult(T result) {
        this.result = result;
        this.isSuccess = true;
        this.failReason = null;
    }

    public OperationResult(String failReason) {
        this.result = null;
        this.isSuccess = false;
        this.failReason = failReason;
    }

    public OperationResult(T result, Boolean isSuccess, String failReason) {
        this.result = result;
        this.isSuccess = isSuccess;
        this.failReason = failReason;
    }

    public T getResult() {
        return result;
    }

    public Boolean isSuccess() {
        return isSuccess;
    }

    public String getFailReason() {
        return failReason;
    }
}
