package org.frameworks.nao;

import com.aldebaran.qi.QiService;

public class HelloService extends QiService {

    public String greet(String name) {
        return "Hello " + name;
    }

}
