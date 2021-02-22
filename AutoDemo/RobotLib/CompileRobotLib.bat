@echo off

rem this script actually does not work for some reason. Use the VSCode export option instead!

rem compile the .java files into .class files
"C:\Program Files\Java\jdk-11.0.4\bin\javac.exe" -d .\bin *.java


rem compile the .class files into a jar
"C:\Program Files\Java\jdk-11.0.4\bin\jar.exe" cvf .\bin\RobotLib.jar .\bin\RobotLib\*.class

rem copy jar to RobotLibExample as well
copy .\bin\RobotLib.jar .\..\RobotLibExamples\

echo done
pause