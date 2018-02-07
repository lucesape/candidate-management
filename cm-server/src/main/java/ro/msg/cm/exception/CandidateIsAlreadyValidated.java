package ro.msg.cm.exception;

public class CandidateIsAlreadyValidated extends RuntimeException {

    public CandidateIsAlreadyValidated() {
        super();
    }

    public CandidateIsAlreadyValidated(String message) {
        super(message);
    }
}
