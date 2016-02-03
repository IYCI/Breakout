
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Ball.java \
	Bricks.java \
	Paddle.java \
	GameBoard.java \
	Splashscreen.java \
	GameFrame.java \
	BrickItem.java \
	Main.java

MAIN = Main

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	$(JVM) $(MAIN)

clean:
	$(RM) *.class
