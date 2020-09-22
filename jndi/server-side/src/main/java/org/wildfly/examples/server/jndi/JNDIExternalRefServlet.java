package org.wildfly.examples.server.jndi;

import javax.annotation.Resource;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "JNDIExternalRefServlet", urlPatterns = {"/jndiRef"})
public class JNDIExternalRefServlet extends HttpServlet {

    @Resource(lookup = "java:/jboss/external/ldap")
    private InitialDirContext ldapCtx;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null) {
            name = "lgao";
        }
        String filter = "uid=" + name;
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        StringBuilder sb = new StringBuilder();
        String[] attIDs = {"mail", "cn", "manager"};
        try {
            NamingEnumeration<SearchResult> enumeration = ldapCtx.search("ou=users,dc=redhat,dc=com", filter, searchControls);
            while (enumeration.hasMoreElements()) {
                Attributes attrs = enumeration.nextElement().getAttributes();
                for (String id: attIDs) {
                    sb.append(id).append(": ").append(attrs.get(id).get()).append("\n");
                }
            }
        } catch (NamingException e) {
            throw new IOException("Cannot search LDAP items.", e);
        }
        PrintWriter out = resp.getWriter();
        out.println(sb.toString());
        out.flush();
    }
}