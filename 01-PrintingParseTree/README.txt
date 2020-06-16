Shows printing parse tree via the built-in antlr tool and a custom implementation.


Generate java files from the grammar:

    java org.antlr.v4.Tool Hello.g4 -o output


Compile Runner class:

    javac Runner.java -cp "./output;%CLASSPATH%" -d output


Print parse tree via built-in Tool

    cd output
    java org.antlr.v4.gui.TestRig Hello r -tree -input ../input.txt
    cd ..


Print parse tree using the custom implementation

    cd output
    cat ../input.txt | java Runner
    cd ..



---

java org.antlr.v4.Tool Hello.g4 -o output
javac Runner.java -cp "./output;%CLASSPATH%" -d output
cd output
java org.antlr.v4.gui.TestRig Hello r -tree -input ../input.txt
cd ..
cd output
cat ../input.txt | java Runner
cd ..