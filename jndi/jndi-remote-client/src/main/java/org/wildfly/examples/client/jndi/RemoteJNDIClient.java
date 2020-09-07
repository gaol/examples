package org.wildfly.examples.client.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Hashtable;
import java.util.Map;

public class RemoteJNDIClient {

    public static void main(String[] args) throws Exception {
        String jndiName = args.length == 0 ? "a" : args[0];
        invokeRemoteJNDI(jndiName);
    }

    private static void invokeRemoteJNDI(String jndiName) throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(Context.SECURITY_PRINCIPAL, "test");
        jndiProperties.put(Context.SECURITY_CREDENTIALS, "Test12#4");
        String url = "remote+http://localhost:8080";
        if (Boolean.getBoolean("remote")) {
            url = "remote://localhost:4447";
        }
        jndiProperties.put(Context.PROVIDER_URL, url);
        final Context context = new InitialContext(jndiProperties);

        System.out.println("JNDI Properties: \n");
        for (Map.Entry<String, String> entry: jndiProperties.entrySet()) {
            System.out.println("\t" + entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("\n");
        System.out.println("\n\tJNDI of name: " + jndiName + " = " + context.lookup(jndiName).toString() + "\n");
        context.close();
    }

}
