package test.lomboktest.utils.exception.post;

public class NotFoundPostException extends IllegalArgumentException {
    public NotFoundPostException(String message) {
        super(message);
    }
}

