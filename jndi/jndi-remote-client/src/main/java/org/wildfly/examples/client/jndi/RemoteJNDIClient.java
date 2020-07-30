package org.wildfly.examples.client.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Hashtable;

public class RemoteJNDIClient {

    private static final String HTTP = "http";

    public static void main(String[] args) throws Exception {
        String jndiName = args.length == 0 ? "default-jndi-name" : args[0];
        invokeRemoteJNDI(jndiName);
    }

    private static void invokeRemoteJNDI(String jndiName) throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
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
        }
        jndiProperties.put(Context.PROVIDER_URL, providerURL);

        System.out.println("JNDI Properties: \n");
        jndiProperties.forEach((n, v) -> System.out.println("\t" + n + " = " + v));
        System.out.println("\n");
        final Context context = new InitialContext(jndiProperties);
        System.out.println("\n\tJNDI of name: " + jndiName + " = " + context.lookup(jndiName).toString() + "\n");
        context.close();
    }

}
