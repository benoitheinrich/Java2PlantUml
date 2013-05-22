# Introduction

The Java2PlantUml project is intended to convert a list of Java files into a class diagram documentation that could be used by [PlantUml](http://plantuml.sourceforge.net/classes.html)

Right now the initial version only allows the conversion of simple java classes using the list of private fields defined at the class level.

# Building Java2PlantUml

Just checkout the repository and run from the command line:

    mvn install

# Running Java2PlantUml

Once you've built Java2PlantUml you can run it using the following command line:

   java -jar target/java2PlantUml-1.0.0-SNAPSHOT.one-jar.jar -b <PATH_TO_YOUR_JAVA_FILES> -o <PATH_OF_THE_GENERATED_FILE>

Once the file is generated you can use the PlantUML jar to convert it into an image:

   java -jar plantuml.jar <PATH_OF_THE_GENERATED_FILE>

That's it, you can now look at your nice diagram :)

# Contributing

This program has been hacked in a few hours, so for sure it might not work for everyone, so if you want to contribute your welcome.

Just do a Pull Request and I'll see how to re-integrate :)
