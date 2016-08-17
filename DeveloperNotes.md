Developer notes
===============

These are notes to help a developer working on jlibsedml to compile and distribute 
the code.

Running the build
=================

You can run the build by running  

    ./gradlew clean build
    
or on Windows

    gradlew.bat clean build
    
    
which will install Gradle automatically (if needed),  run tests and generate a jar file in build/libs.

or, using Maven

    mvn clean package

Logging
=======

Slf4j API is used in source code, and logging is enabled when running tests using
slf4j simple, using 'info' level logging by default.

To alter the logging level when running tests, add a system property

    org.slf4j.simpleLogger.defaultLogLevel=[debug, warn ,error]
    
E.g. 

   -Dorg.slf4j.simpleLogger.defaultLogLevel=debug 
   
Committing
==========

Please **don't** commit the _build_, _target_ or _bin_ folders that might get created by your IDE or a Maven or Gradle build. These are generated files and shouldn't be in the VCS.

Deployment
==========

* Ensure all unit tests pass using `./gradlew clean test`.
* Ensure all new public API methods are documented with Javadoc and a @since tag.
* Update the jlibsedmlhowto.textile if need be. You'll at least need to update the first few sentences which contain the version number.
* Update the README.txt file with information about new features in this release.
* Rebuild the documentation using docs/build.xml (for this to generate a PDF, you'll need _fop_ installed).
* In SEDMLDocument class, update the jlibsedml version number constant.
* In `build.gradle`, update the `jar.version` property to the same value.
* Now run `gradle clean distZip`. This will generate documentation, and include src code and Javadoc into a zip in build/distributions
* Upload the zip, tutorial and README.txt to Sourceforge.

