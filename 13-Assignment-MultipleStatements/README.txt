Generate java files from the grammar:

java org.antlr.v4.Tool Expr.g4 -o output


Compile Runner class:

javac Runner.java -cp "./output;%CLASSPATH%" -d output


Print parse tree via built-in Tool (run when inside the "/output" folder)

java org.antlr.v4.gui.TestRig Expr s -tree -input ../input.txt


Print parse tree using the custom implementation (run when inside the "output" folder)

cat ../input.txt | java Runner



Repeated Runs when inside the "output" folder:
--- 
rm *
cd ..
java org.antlr.v4.Tool Expr.g4 -o output
javac Runner.java -cp "./output;%CLASSPATH%" -d output
cd output
java org.antlr.v4.gui.TestRig Expr s -tree -input ../input.txt
cat ../input.txt | java Runner 




Repeated runs for Runner when inside the "output" folder:
javac ../Runner.java -d . & cat ../input.txt | java Runner 