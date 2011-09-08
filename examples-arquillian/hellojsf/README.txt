To make authentication work in AS7 follow the steps:

1) on standalone/configuration/standalone.xml file
add the class name org.jboss.security.auth.spi.UsersRolesLoginModule
as a parameter for the code attribute at login module from security-domain named "other"

<subsystem xmlns="urn:jboss:domain:security:1.0">
            <security-domains>
                <security-domain name="other" cache-type="default">
                    <authentication>
                        <login-module code="org.jboss.security.auth.spi.UsersRolesLoginModule" flag="required"/>
                    </authentication>
                </security-domain>
            </security-domains>
        </subsystem>

2) on standalone/configuration/mgmt-users.properties
add this new line to the file:
admin=password

To make it work in AS6 use default server and you shall have no problem with it.


