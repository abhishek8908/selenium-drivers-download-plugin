# selenium-drivers-download-plugin
A maven plugin which generates drivers (chromedriver and geckodriver) at run time.

# Maven dependency
~~~
<dependencies>
    <dependency>
        <groupId>com.github.abhishek8908</groupId>
        <artifactId>selenium-drivers-download-plugin</artifactId>
        <version>1.2</version>
    </dependency>
</dependencies>
~~~


# Quickstart
Add the following plugin to your POM file.

            <plugin>
                <groupId>com.github.abhishek8908</groupId>
                <artifactId>selenium-drivers-download-plugin</artifactId>
                <version>1.2</version>
                <configuration>
                <!-- Path to save selenium drivers -->
                    <driverPath>${project.basedir}/drivers</driverPath>
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
                            <name>geckodriver</name
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


# Usage

Add system property with Surefire Plugin 

       <properties>
            <name>chromedriver</name>
            <version>2.40</version>
            <os>win32</os>
       </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.21.0</version>
                <configuration>
                    <suiteXmlFiles>
                        <suiteXmlFile>testng.xml</suiteXmlFile>
                    </suiteXmlFiles>
                    <systemProperties>
                        <property>
                            <name>webdriver.chrome.driver</name>
                            <value>${project.basedir}/drivers/${name}-${version}-${os}.exe</value>
                        </property>
                    </systemProperties>
                </configuration>
            </plugin>
        </plugins>
    </build>
