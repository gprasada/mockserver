package com.gp;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static org.mockserver.model.JsonBody.json;

public class Driver {

    private final ConfigLoader configLoader = new ConfigLoader();
    private final ExpectationsConfigrator expectationsConfigrator = new ExpectationsConfigrator();
    private ClientAndServer mockServer;
    private String sampleFilesBasePath;
    private FileReader fr = new FileReader();

    public static void main(String[] args) throws IOException {
        new Driver().start();
    }

    private void start() throws IOException {
        Properties properties = configLoader.getProperties();
        System.out.println(properties.getProperty("server.port"));
        int serverPort = Integer.parseInt(properties.getProperty("server.port"));
        sampleFilesBasePath = properties.getProperty("samplefiles.basepath");
        System.out.println("serverPort "+serverPort);

        mockServer = ClientAndServer.startClientAndServer(serverPort);
        List<Expectation> expectationList = expectationsConfigrator.readExpectations(properties.getProperty("expectations"));
        setExpectations(expectationList);
    }


    private void setExpectations(List<Expectation> expectationList) {
        for (Iterator<Expectation> iterator = expectationList.iterator(); iterator.hasNext(); ) {
            Expectation expectation = iterator.next();
            setExpectation(expectation);
        }
        /*mockServer.when(getRequest()).respond(getResponse());
        mockServer.when(getRequest2()).respond(getResponse2());*/
    }

    private void setExpectation(Expectation expectation) {
        mockServer.when(getRequest(expectation)).respond(getResponse(expectation));

    }

    private HttpResponse getResponse(Expectation expectation) {
        HttpResponse httpResponse =  HttpResponse.response();
        httpResponse = httpResponse.withStatusCode(expectation.getResponseStatus());
        if(expectation.getResponseBody() != null && expectation.getResponseBody().trim().length() > 0) {
            String resFile = expectation.getResponseBody();
            String reqBody = fr.readFile(sampleFilesBasePath, resFile);

            httpResponse = httpResponse.withHeader("Content-Type", "application/json").withBody(json(reqBody));
        }
        return httpResponse;
    }

    private HttpRequest getRequest(Expectation expectation) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest = httpRequest.withPath(expectation.getRequestPath());
        httpRequest = httpRequest.withMethod(expectation.getRequestMethod());
        if(expectation.getRequestBody() != null && expectation.getRequestBody().trim().length() > 0) {
            String reqFile = expectation.getRequestBody();
            String reqBody = fr.readFile(sampleFilesBasePath, reqFile);

            httpRequest = httpRequest.withHeader("Content-Type", "application/json").withBody(json(reqBody));
        }
        return httpRequest;
    }

 /*   private HttpResponse getResponse2() {
        return HttpResponse.response().withHeader("Content-Type", "application/json").withBody("{\"greetz\":\"hello worldz\"}").withStatusCode(201);
    }

    private HttpRequest getRequest2() {
        return new HttpRequest().withPath("/some/path2").withMethod("POST").withBody(json(getJSON2(), MatchType.STRICT));
    }

    private HttpResponse getResponse() {
        return HttpResponse.response().withHeader("Content-Type", "application/json").withBody("{\"greet\":\"hello world\"}").withStatusCode(200);
    }

    private HttpRequest getRequest() {
        return new HttpRequest().withPath("/some/path").withMethod("POST").withBody(json(getJSON(), MatchType.STRICT));
    }

    private String getJSON() {
        return "{\"id\":1245,\"address\":\"NSW\"}";
    }

    private String getJSON2() {
        return "{\"id\":124,\"address\":\"Paramatta\"}";
    }*/


}
