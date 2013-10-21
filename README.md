chatty-skype
============
Skype connector for Chatty.

Building
========
Building is done using Maven. Build the `pom.xml` file using your favourite Java IDE or the command prompt: `mvn install`. 
The bots's jar file is located in the `target` directory. 

Installation
============
Drop the jar file into Chatty's `bots` folder, add the following after `bots:` 
in your Chatty config file changing values as needed and restart Chatty:

        - type: skype
          settings:
               username: my-bot #The Skype username the bot looks for
               name: MyBot #The full name the bot will use
             
For more info, see [Chatty Configuration](https://github.com/bogeymanEST/chatty/wiki/Getting-Started#configuration).
