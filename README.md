# selenium-drivers-download-plugin
A maven plugin which generates drivers (chromedriver and geckodriver) at run time and sets the system property for the browser 

# Quickstart
Add the following to your POM file.

            <plugin>
                <groupId>com.adsingh.plugin</groupId>
                <artifactId>selenium-drivers-download-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <configuration>
                <!-- Path to save selenium drivers -->
                    <driverPath>D:/Driver</driverPath>
                    <drivers>
                        <driver>
                         <!-- Driver Name -->
                            <name>chromedriver</name>
                         <!-- Driver version -->
                            <version>2.39</version>
                         <!-- Driver for OS -->
                            <os>win32</os>
                        </driver>
                        <driver>
                            <name>chromedriver</name>
                            <version>2.39</version>
                            <os>linux64</os>
                        </driver>
                        <driver>
                            <name>geckodriver</name>
                            <version>0.20.1</version>
                            <os>linux64</os>
                        </driver>
                        <driver>
                            <name>geckodriver</name>
                            <version>0.20.1</version>
                            <os>macos</os>
                        </driver>
                        <driver>
                            <name>chromedriver</name>
                            <version>2.39</version>
                            <os>mac64</os>
                        </driver>
                    </drivers>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>generateDrivers</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
