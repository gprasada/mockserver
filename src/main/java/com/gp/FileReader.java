package com.gp;

import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileReader {

    public String readFile(String basepath, String fileName){
        String fileToRead = basepath+File.separator+fileName;
        System.out.println("file to read "+fileName);
        try {
            return IOUtils.toString(new FileInputStream(fileToRead), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
