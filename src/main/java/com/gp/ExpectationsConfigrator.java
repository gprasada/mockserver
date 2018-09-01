package com.gp;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class ExpectationsConfigrator {
    public ExpectationsConfigrator() {
    }

    List<Expectation> readExpectations(String expectations) throws IOException {
        Reader in = new FileReader(expectations);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
        //request.path,request.method,request.contenttype,request.body,response.status,response.contentType,response.body
        List<Expectation> expectationList = new ArrayList<Expectation>();
        for (CSVRecord record : records) {
            Expectation expectation = new Expectation();
            expectation.setRequestPath(record.get("request.path"));
            expectation.setRequestMethod(record.get("request.method"));
            expectation.setRequestBody(record.get("request.body"));
            expectation.setRequestContentType(record.get("request.contenttype"));
            expectation.setResponseStatus(record.get("response.status"));
            expectation.setResponseBody(record.get("response.body"));
            expectation.setResponseContentType(record.get("response.contentType"));
            expectationList.add(expectation);
        }
        return expectationList;
    }
}