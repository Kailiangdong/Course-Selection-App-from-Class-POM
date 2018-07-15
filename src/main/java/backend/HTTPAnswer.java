package backend;

public class HTTPAnswer {

    private boolean httpRequestSuccessful;
    private String answer;

    public HTTPAnswer(boolean httpRequestSuccessful, String answer) {
        this.httpRequestSuccessful = httpRequestSuccessful;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isHttpRequestSuccessful() {
        return httpRequestSuccessful;
    }
}