package org.wildfly.examples.client.jndi;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Map;

public class DnsJNDIClient {

    public static void main(String[] args) throws Exception {
        String domainName = args.length == 0 ? "redhat.com" : args[0];
        invokeDNSLookup(domainName);
    }

    private static void invokeDNSLookup(String domainName) throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "dns://8.8.8.8");
        System.out.println("DNS JNDI Properties: \n");
        for (Map.Entry<String, String> entry: jndiProperties.entrySet()) {
            System.out.println("\t" + entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("\n");
        final DirContext context = new InitialDirContext(jndiProperties);
        Attributes attrs = context.getAttributes(domainName, new String[]{"A"});
        NamingEnumeration<?> enumeration = attrs.get("A").getAll();
        while (enumeration.hasMoreElements()) {
            System.out.println("IP of domain name: " + domainName + " = " + enumeration.nextElement().toString());
        }
        context.close();
    }

}
