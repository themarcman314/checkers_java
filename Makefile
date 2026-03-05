JC = javac
FLAGS = -d build -sourcepath src
SOURCES = $(shell find src -name "*.java")

all:
	mkdir -p build
	$(JC) $(FLAGS) $(SOURCES)

run: all
	java -cp build client.client
clean:
	rm -r build/*
