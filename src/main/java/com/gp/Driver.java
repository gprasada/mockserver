package com.gp;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.IOException;
import java.util.Properties;

import static org.mockserver.model.JsonBody.json;

public class Driver {

    private final ConfigLoader configLoader = new ConfigLoader();
    private ClientAndServer mockServer;

    public static void main(String[] args) throws IOException {
        new Driver().start();
    }

    private void start() throws IOException {
        Properties properties = configLoader.getProperties();
        System.out.println(properties.getProperty("server.port"));
        int serverPort = Integer.parseInt(properties.getProperty("server.port"));
        System.out.println("serverPort "+serverPort);

        mockServer = ClientAndServer.startClientAndServer(serverPort);
        setExpectations();
    }

    private void setExpectations() {
        mockServer.when(getRequest()).respond(getResponse());
        mockServer.when(getRequest2()).respond(getResponse2());
    }

    private HttpResponse getResponse2() {
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
    }


}
