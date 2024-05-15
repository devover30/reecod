package info.devram.reecod.libs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import info.devram.reecod.exceptions.AppException;


/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    public enum Status {
        LOGIN_SUCCESS,
        LOGIN_VERIFY_SUCCESS,
        TOKEN_VERIFY_SUCCESS,
        NOTES_GET_SUCCESS,
        NOTES_TAGS_GET_SUCCESS,
        ERROR
    }

    @Nullable
    private final T data;
    @NonNull
    private final Status status;
    @Nullable
    private final AppException exception;

    public Result(@NonNull Status status, @Nullable T data, @Nullable AppException error) {
        this.status = status;
        this.data = data;
        this.exception = error;
    }

    public static <T> Result<T> loginSuccess(T data) {
        return new Result<>(Status.LOGIN_SUCCESS, data, null);
    }

    public static <T> Result<T> loginVerifySuccess(T data) {
        return new Result<>(Status.LOGIN_VERIFY_SUCCESS, data, null);
    }


    public static <T> Result<T> error(AppException e) {
        return new Result<>(Status.ERROR, null, e);
    }

    public static <T> Result<T> verifyTokenSuccess(T data) {
        return new Result<>(Status.TOKEN_VERIFY_SUCCESS, data, null);
    }

    public static <T> Result<T> notesListFetchSuccess(T data) {
        return new Result<>(Status.NOTES_GET_SUCCESS, data, null);
    }

    public static <T> Result<T> notesTagsFetchSuccess(T data) {
        return new Result<>(Status.NOTES_TAGS_GET_SUCCESS, data, null);
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return this.data;
    }

    @Nullable
    public AppException getException() {
        return this.exception;
    }
}