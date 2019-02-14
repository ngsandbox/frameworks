package org.ngsandbox.streams;

import org.junit.Assert;

import java.net.URL;

public class FileUtils {
    public static String getFilePath(String innerPath) {
        URL resource = FileUtils.class.getResource(innerPath);
        Assert.assertNotNull("The file was not found " + innerPath, resource);
        return resource.getFile();
    }

}
