package org.wildfly.examples.server.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(ICalculator.class)
public class Calculator implements ICalculator {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

}
