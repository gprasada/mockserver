package com.gp;

public class Expectation {

    private String requestPath;
    private String requestMethod;
    private String requestBody;
    private String responseStatus;
    private String responseBody;
    private String requestContentType;
    private String responseContentType;


    public String getRequestPath() {
        return trimSafeString(requestPath);
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return trimSafeString(requestMethod);
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestBody() {
        return trimSafeString(requestBody);
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public int getResponseStatus() {
        return Integer.parseInt(responseStatus.trim());
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseBody() {
        return trimSafeString(responseBody);
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    private String trimSafeString(String value) {
        if(value != null) {
            return value.trim();
        }
        return value;
    }

    public String getRequestContentType() {
        return trimSafeString(requestContentType);
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public String getResponseContentType() {
        return trimSafeString(responseContentType);
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }
}
