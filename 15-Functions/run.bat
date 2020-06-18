@echo off
echo Deleting files from /output folder
pause

rm -f output/*
echo Will generate grammar files
java org.antlr.v4.Tool Prog.g4 -o output
pause

echo Will compile Runner class
javac Runner.java -cp "./output;%CLASSPATH%" -d output
pause

cd output

cd ..
cd output
echo Will run Runner
cat ../input.txt | java Runner
cd ..


