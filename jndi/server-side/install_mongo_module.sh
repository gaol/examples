#!/bin/bash

JBOSS_HOME="${JBOSS_HOME:-$1}"
MONGODB_VERSION="3.6.3"
echo -e "Mongodb vesion: $MONGODB_VERSION"
[ "$JBOSS_HOME" == "" -o ! -d "$JBOSS_HOME" ] && echo -e "Please specify JBOSS / WildFly server home" && exit 1
[ ! -e ${JBOSS_HOME}/jboss-modules.jar ] && echo -e "'$JBOSS_HOME' is not a valid JBoss/WildFly Server" && exit 1

echo -e "Creating module directory..."
mkdir -p $JBOSS_HOME/modules/system/layers/base/org/mongodb/main

echo -e "Downloading mongodb java driver..."
wget -O $JBOSS_HOME/modules/system/layers/base/org/mongodb/main/mongo-java-driver-${MONGODB_VERSION}.jar https://maven.aliyun.com/nexus/content/groups/public//org/mongodb/mongo-java-driver/${MONGODB_VERSION}/mongo-java-driver-${MONGODB_VERSION}.jar

echo -e "Creating $JBOSS_HOME/modules/system/layers/base/org/mongodb/main/module.xml for module: org.mongodb"

cat << EOF > $JBOSS_HOME/modules/system/layers/base/org/mongodb/main/module.xml
<module xmlns="urn:jboss:module:1.1" name="org.mongodb">
   <resources>
       <resource-root path="mongo-java-driver-${MONGODB_VERSION}.jar"/>
   </resources>
   <dependencies>
       <module name="javax.api"/>
       <module name="javax.transaction.api"/>
       <module name="javax.servlet.api" optional="true"/>
   </dependencies>
</module>
EOF

echo -e "Done!"

echo -e "Module XML file:"
cat $JBOSS_HOME/modules/system/layers/base/org/mongodb/main/module.xml


