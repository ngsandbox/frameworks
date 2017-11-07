package org.ngsandbox.nlp;

import opennlp.tools.doccat.DoccatModel;

import java.io.InputStream;

public class Program {
    public static void main(String... args) throws Exception {
        InputStream is = null;
        DoccatModel m = new DoccatModel(is);
    }
}
