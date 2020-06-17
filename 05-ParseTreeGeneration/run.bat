@echo off
echo Deleting files from /output folder
pause

rm -f output/*
echo Will generate grammar files
java org.antlr.v4.Tool Expr.g4 -o output
pause

echo Will compile Runner class
javac Runner.java -cp "./output;%CLASSPATH%" -d output
pause

cd output
echo Will run TestRig
java org.antlr.v4.gui.TestRig Expr s -tree -input ../input.txt
echo Ran TestRig
pause

cd ..
cd output
echo Will run Runner
cat ../input.txt | java Runner
cd ..

echo done

