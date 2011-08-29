set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_21

cmd /c "C:\Program Files\NetBeans 6.9.1\java\ant\bin\ant" -verbose -buildfile build-test.xml test > report.txt

pause