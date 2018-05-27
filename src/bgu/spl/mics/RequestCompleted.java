package bgu.spl.mics;
/**
 * 
 * @the message to send when the a request is completed.
 *
 * @param <T>
 */
public class RequestCompleted<T> implements Message {

    private Request<T> completed;
    private T result;
/**
 * 
 * @param completed
 * @param result
 */
    public RequestCompleted(Request<T> completed, T result) {
        this.completed = completed;
        this.result = result;
    }
/**
 * returning the completed request
 * @return
 */
    public Request getCompletedRequest() {
        return completed;
    }
/**
 * returning the result.
 * @return
 */
    public T getResult() {
        return result;
    }

}
