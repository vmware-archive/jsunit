export CLASSPATH=$CLASSPATH:./lib/jsunit.jar:./lib/org.mortbay.jetty.jar:./lib/jdom.jar:./lib/javax.servlet.jar
echo $CLASSPATH
java net.jsunit.JsUnitResultAcceptor
