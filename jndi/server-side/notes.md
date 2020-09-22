
#  Start docker container for testing
#
#  docker run --rm -p 27017:27017  docker.io/mongo:3.6.3
#

# creating simple JNDI binding:
/subsystem=naming/binding=java\:jboss\/exported\/a:add(binding-type=simple,type=int, value=100)
{"outcome" => "success"}


# creating lookup / alias JNDI binding:
/subsystem=naming/binding=java\:jboss\/b:add(binding-type=lookup,lookup=java\:jboss\/exported\/a)
{"outcome" => "success"}

# creating object-factory JNDI binding:

/subsystem=naming/binding=java\:jboss\/mongoclient:add(binding-type=object-factory, \
module=org.mongodb, \
class=com.mongodb.client.jndi.MongoClientFactory, \
environment={\
   connectionString => "mongodb://localhost:27017"})

{"outcome" => "success"}


# creating external context JNDI binding:

/subsystem=naming/binding=java\:jboss\/external\/ldap:add(binding-type=external-context, \
cache=true, module=org.jboss.as.naming, \
class=javax.naming.directory.InitialDirContext,\
environment={\
   java.naming.factory.initial => com.sun.jndi.ldap.LdapCtxFactory, \
   java.naming.provider.url => "ldap://ldap.corp.redhat.com:389"})

{"outcome" => "success"}

