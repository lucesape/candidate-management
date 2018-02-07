package ro.msg.cm.exception;

public class CandidateNotFound extends RuntimeException {

    public CandidateNotFound() {
        super();
    }

    public CandidateNotFound(String message) {
        super(message);
    }
}
