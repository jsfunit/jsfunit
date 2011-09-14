The Getting Started applicaiton is completely independent.  This project's pom does not inherit from any parent pom and it does not rely on a settings.xml file.  This way, it can show everything that one needs to set up a JSFUnit/Arquillian project.

The Getting Started applicaiton can be run with JBossAS 6.x or JBossAS 7.x.  Both are set to run in remote mode.  To modify the build for other containers or other modes, see the Arquillian documentation.  You can also see configurations for other containers and modes at https://github.com/jsfunit/jsfunit/tree/master/examples-arquillian.

To run Getting Started with JBoss AS6:
1) Start the server process for JBoss AS6
2) mvn -Pjbossas-remote-6 test

To run Getting Started with JBoss AS7:
1) Start the server process for JBoss AS7
2) mvn -Pjbossas-remote-7 test