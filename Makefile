ANTLR4_JAR=antlr-4.9.1-complete.jar
LOMBOK_JAR=lombok.jar
JUNIT_JAR=junit-4.13.2.jar
HAMCREST_JAR=hamcrest-all-1.3.jar
ANTLR4_URL=https://www.antlr.org/download/$(ANTLR4_JAR)
LOMBOK_URL=https://projectlombok.org/downloads/$(LOMBOK_JAR)
JUNIT_URL=https://repo1.maven.org/maven2/junit/junit/4.13.2/$(JUNIT_JAR)
HAMCREST_URL=https://repo1.maven.org/maven2/org/hamcrest/hamcrest-all/1.3/$(HAMCREST_JAR)
ANTLR = java -jar lib/$(ANTLR4_JAR) -Dlanguage=Java -visitor -no-listener
G4 = src/main/g4
PFILE = $(G4)/FOkParser.g4
LFILE = $(G4)/FOkLexer.g4
MAIN=src/main/java
TEST=src/test/java
OUT = out/class
CP = .:$(OUT):lib/$(ANTLR4_JAR):lib/$(JUNIT_JAR):lib/$(HAMCREST_JAR):lib/$(LOMBOK_JAR)

all: lib
	make antlr

lib: 
	mkdir -p lib
	curl -o lib/$(ANTLR4_JAR) $(ANTLR4_URL)
	curl -o lib/$(LOMBOK_JAR) $(LOMBOK_URL)
	curl -o lib/$(JUNIT_JAR) $(JUNIT_URL)
	curl -o lib/$(HAMCREST_JAR) $(HAMCREST_URL)

clean:
	rm -rf lib $(OUT)

antlr: lib/$(ANTLR4_JAR) $(PFILE) $(LFILE)
	$(ANTLR) $(LFILE) -package antlr
	$(ANTLR) $(PFILE) -package antlr
	rm -rf $(G4)/*.tokens
	rm -rf $(G4)/*.interp
	mv $(G4)/*.java $(MAIN)/antlr

compile: antlr
	mkdir -p $(OUT)
	javac -cp $(CP):$(MAIN) -d $(OUT) $(MAIN)/Main.java

run: compile
	@echo ""
	@echo "Running..."
	java -cp $(OUT):lib/$(ANTLR4_JAR) Main
	@echo "Done."

test: compile
	@echo ""
	@echo "Running tests..."
	@mkdir -p $(OUT)
	@javac -cp $(CP) -d $(OUT) $(TEST)/SimpleTest.java 
	@java -cp $(CP) org.junit.runner.JUnitCore SimpleTest
	@echo "Done."

wc: 
	@wc -l `find . -name '[^FOk]*.java' -or -name '*.g4' -or -name 'Makefile' -or -name 'FOkVisitor.java'`

.PHONY: all clean antlr run compile test wc