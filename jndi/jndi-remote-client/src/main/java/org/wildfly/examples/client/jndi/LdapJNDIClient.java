package org.wildfly.examples.client.jndi;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

public class LdapJNDIClient {

    public static void main(String[] args) throws Exception {
        String filterName = args.length == 0 ? "uid=lgao" : args[0];
        String attrName = args.length <= 1 ? "mail" : args[1];
        invokeLDAPLookup(filterName, attrName);
    }

    private static void invokeLDAPLookup(String filter, String attrName) throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        jndiProperties.put(Context.PROVIDER_URL, "ldap://ldap.corp.redhat.com");
        System.out.println("LDAP JNDI Properties: \n");
        jndiProperties.forEach((n, v) -> System.out.println("\t" + n + " = " + v));
        System.out.println("\n");
        final DirContext context = new InitialDirContext(jndiProperties);
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> enumeration = context.search("ou=users,dc=redhat,dc=com", filter, searchControls);
        while (enumeration.hasMoreElements()) {
            SearchResult result = enumeration.next();
            System.out.println(attrName + " of " + filter + " = " + result.getAttributes().get(attrName));
        }
        context.close();
    }

}
