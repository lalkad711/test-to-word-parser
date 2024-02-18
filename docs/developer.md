# Developer Documentation
This documentation aims to help developers own and operate this application.

# Build and run
It is encouraged to use a IDE that has good support for gradle when developing your application locally.

If you want to run the application from the command line please use the gradle wrapper that is bundled with the application:

* gradlew.bat (windows)
* gradlew (mac)

To build the applcation please run following comand:
> gradlew clean build

If you are actively working on the project, then following command will come in handy for formatting the code:
> gradlew spotlessApply

Or

>gradlew spotlessJava

To run this application simply use the following .zip once the application has been successfully build:
`/build/distributions/text-to-word-parser-SNAPSHOT.zip`

Within the above mentioned zip, there will a bat file, if working on windows:
`/build/distributions/text-to-word-parser-SNAPSHOT/bin/text-to-word-parser.bat`

If we execute the above file then we should see the app up and running.
NOTE: It will be nice to have JDK 21 for the pre-compiled application to run on your system without any errors.

The application will take an absolute or fully qualified path of a text file and try to create one or more xml and csv file/s.


# Test

Run the tests using
> gradlew test

The report is located at:
`/build/reports/tests/test/index.html`

##  Application Dependencies
* Lombok for boiler plate code generation.
* JAXB for XML Marshalling.
* OpenCSV for writing the java objects to CSV file format.