package org.wildfly.examples.client.ejb;

import org.jboss.ejb.client.EJBClient;
import org.jboss.ejb.client.EJBIdentifier;
import org.jboss.ejb.client.StatelessEJBLocator;
import org.wildfly.examples.server.ejb.ICalculator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Hashtable;

public class RemoteEJBClient {

    private static final String HTTP = "http";

    public static void main(String[] args) throws Exception {
        if (Boolean.getBoolean("ejb.client.api")) {
            invokeStatelessBeanUsingEJBCLientAPI();
        } else {
            invokeStatelessBeanUsingJNDIAPI();
        }
    }

    private static void invokeStatelessBeanUsingEJBCLientAPI() {
        StatelessEJBLocator<ICalculator> locator = new StatelessEJBLocator<ICalculator>(ICalculator.class,
                new EJBIdentifier("", "server-side", "Calculator", ""));
        ICalculator calculator = EJBClient.createProxy(locator);
        int a = 204, b = 340;
        int sum = calculator.add(a, b);
        System.out.println("Remote calculator(Using EJBCLient API) of " + a + " + " + b + " = " + sum);
    }

    private static void invokeStatelessBeanUsingJNDIAPI() throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        // the old org.jboss.naming.remote.client.InitialContextFactory delegates to the new one
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        String username = System.getProperty("username");
        if (username != null) {
            jndiProperties.put(Context.SECURITY_PRINCIPAL, username);
            jndiProperties.put(Context.SECURITY_CREDENTIALS, System.getProperty("password"));
        }
        final String hostName = System.getProperty("wildfly.hostname", "localhost");
        final String providerURL;
        if (Boolean.getBoolean(HTTP)) {
            providerURL = "http://" + hostName + ":8080/wildfly-services";
        } else {
            providerURL = "remote+http://" + hostName + ":8080";
//            providerURL = "http://" + hostName + ":8080/wildfly-services";
        }
        jndiProperties.put(Context.PROVIDER_URL, providerURL);

        final Context context = new InitialContext(jndiProperties);

        final String name = Boolean.getBoolean("ejb") ? "ejb:/server-side/Calculator!" : "server-side/Calculator!";
        final String jndiName = name + ICalculator.class.getName();
        System.out.println("JNDI Properties: \n");
        jndiProperties.forEach((n, v) -> System.out.println("\t" + n + " = " + v));
        System.out.println("\tJNDI Name: " + jndiName);
        System.out.println("\n");
        final ICalculator statelessRemoteCalculator = (ICalculator)context.lookup(jndiName);
        System.out.println("Obtained a remote stateless calculator for invocation");
        int a = 204, b = 340;
        int sum = statelessRemoteCalculator.add(a, b);
        System.out.println("Remote calculator(Using JNDI lookup) of " + a + " + " + b + " = " + sum);
    }

}
