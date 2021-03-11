# Example simple servlet to list smallrye default configs

## Build

> mvn clean install

## Deploy it to WildFly/EAP

In JBoss CLI:

> deploy target/smallrye-config-patch-reproducer.war


## Check the output using URI:

> curl  http://localhost:8080/patches/hello

You should see the default configs from server side.

