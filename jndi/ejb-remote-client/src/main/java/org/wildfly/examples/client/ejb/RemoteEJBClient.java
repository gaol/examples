package org.wildfly.examples.client.ejb;

import org.jboss.ejb.client.EJBClient;
import org.jboss.ejb.client.StatelessEJBLocator;
import org.wildfly.examples.server.ejb.ICalculator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Hashtable;
import java.util.Map;

public class RemoteEJBClient {

    public static void main(String[] args) throws Exception {
        if (Boolean.getBoolean("ejb.client.api")) {
            System.out.println("\nCalling remote EJB using EJBClient API");
            invokeStatelessBeanUsingEJBCLientAPI();
        } else if (Boolean.getBoolean("jndi")){
            System.out.println("\nCalling remote EJB using exported JNDI lookup");
            invokeBeanUsingJNDILookup();
        } else {
            System.out.println("\nCalling remote EJB using jboss-ejb-client.properties and ejb:/ lookup");
            invokeStatelessBeanUsingEJBLookup();
        }
    }

    private static void invokeStatelessBeanUsingEJBCLientAPI() {
        System.out.println("Creating StatelessEJBLocator");
        StatelessEJBLocator<ICalculator> locator = new StatelessEJBLocator<ICalculator>(ICalculator.class,"", "server-side", "Calculator", "");
        System.out.println("Creating EJB Proxy");
        ICalculator calculator = EJBClient.createProxy(locator);
        System.out.println("EJB Proxy created!");
        int a = 204, b = 340;
        int sum = calculator.add(a, b);
        System.out.println("Remote calculator(Using EJBCLient API) of " + a + " + " + b + " = " + sum);
    }

    private static void invokeStatelessBeanUsingEJBLookup() throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);

        final String name = "ejb:/server-side/Calculator!";
        final String jndiName = name + ICalculator.class.getName();
        System.out.println("JNDI Properties: \n");
        for (Map.Entry<String, String> entry: jndiProperties.entrySet()) {
            System.out.println("\t" + entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("\tJNDI Name: " + jndiName);
        System.out.println("\n");
        final ICalculator statelessRemoteCalculator = (ICalculator)context.lookup(jndiName);
        System.out.println("Obtained a remote stateless calculator for invocation");
        int a = 204, b = 340;
        int sum = statelessRemoteCalculator.add(a, b);
        System.out.println("Remote calculator(Using JNDI lookup) of " + a + " + " + b + " = " + sum);
    }

    private static void invokeBeanUsingJNDILookup() throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        jndiProperties.put(Context.SECURITY_PRINCIPAL, "test");
        jndiProperties.put(Context.SECURITY_CREDENTIALS, "Test12#4");
        final String hostName = System.getProperty("eap.hostname", "localhost");
        jndiProperties.put(Context.PROVIDER_URL, "remote://" + hostName + ":4447");
        final Context context = new InitialContext(jndiProperties);

        final String name = "server-side/Calculator!";
        final String jndiName = name + ICalculator.class.getName();
        System.out.println("JNDI Properties: \n");
        for (Map.Entry<String, String> entry: jndiProperties.entrySet()) {
            System.out.println("\t" + entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("\tJNDI Name: " + jndiName);
        System.out.println("\n");
        final ICalculator statelessRemoteCalculator = (ICalculator)context.lookup(jndiName);
        System.out.println("Obtained a remote stateless calculator for invocation");
        int a = 204, b = 340;
        int sum = statelessRemoteCalculator.add(a, b);
        System.out.println("Remote calculator(Using Scoped EJB lookup) of " + a + " + " + b + " = " + sum);
    }

}
