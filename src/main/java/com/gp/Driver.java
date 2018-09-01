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
import static org.mockserver.model.XmlBody.xml;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
    }

    private void setExpectation(Expectation expectation) {
        mockServer.when(getRequest(expectation)).respond(getResponse(expectation));
    }

    private HttpResponse getResponse(Expectation expectation) {
        HttpResponse httpResponse =  HttpResponse.response();
        httpResponse = httpResponse.withStatusCode(expectation.getResponseStatus());
        if(!isBlank(expectation.getResponseBody())) {
            String resFile = expectation.getResponseBody();
            String resBody = fr.readFile(sampleFilesBasePath, resFile);
            String contentType = expectation.getResponseContentType();

            httpResponse = setResponseBody(contentType, resBody, httpResponse);
        }
        return httpResponse;
    }

    private HttpResponse setResponseBody(String contentType, String resBody, HttpResponse httpResponse) {
        HttpResponse response = httpResponse.withHeader("Content-Type", contentType);
        if(contentType.equals("application/json")) {
            response = response.withBody(json(resBody));
        } else if(contentType.equals("application/xml")) {
            response = response.withBody(xml(resBody));
        } else {
            response = response.withBody(resBody);
        }
        return response;
    }

    private HttpRequest getRequest(Expectation expectation) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest = httpRequest.withPath(expectation.getRequestPath());
        httpRequest = httpRequest.withMethod(expectation.getRequestMethod());
        if(!isBlank(expectation.getRequestBody())) {
            String reqFile = expectation.getRequestBody();
            String reqBody = fr.readFile(sampleFilesBasePath, reqFile);
            String contentType = expectation.getRequestContentType();
            httpRequest = setBody(contentType, reqBody, httpRequest);
        }
        return httpRequest;
    }

    private HttpRequest setBody(String contentType, String reqBody, HttpRequest httpRequest) {
        HttpRequest request = httpRequest.withHeader("Content-Type", contentType);
        if(contentType.equals("application/json")) {
            request = request.withBody(json(reqBody));
        } else if(contentType.equals("application/xml")) {
            request = request.withBody(xml(reqBody));
        } else {
            request = request.withBody(reqBody);
        }
        return request;
    }

}
