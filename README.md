This is made for execrcise of
https://blog.newrelic.com/2014/09/29/diving-bytecode-manipulation-creating-audit-log-asm-javassist/

commands ran

>javac ImportantLogClassTransformer.java

> javac -cp home/chetan/Downloads/jonathon/RuntimeManipulationWithJavaAgent/src/com/company/agent ImportantLogClassTransformer.java Agent.java

> jar cvfm Agent.jar manifest.txt Agent.class ImportantLogClassTransformer.class

Now you need to add in VM argument of another project under javaAgent

-javaagent:/home/chetan/Downloads/jonathon/RuntimeManipulationWithJavaAgent/src/com/company/agent/Agent.jar

see attached pic
