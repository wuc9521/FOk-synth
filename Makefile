ANTLR4_JAR=antlr-4.9.1-complete.jar
ANTLR4_URL=https://www.antlr.org/download/$(ANTLR4_JAR)
ANTLR = java -jar lib/$(ANTLR4_JAR) -Dlanguage=Java -visitor -no-listener
AG=src/main/ag
PFILE = $(AG)/FOkParser.g4
LFILE = $(AG)/FOkLexer.g4
MAIN=src/main/java
OUT = out

all: lib/$(ANTLR4_JAR)
	make antlr

lib/$(ANTLR4_JAR): 
	mkdir -p lib
	curl -o lib/$(ANTLR4_JAR) $(ANTLR4_URL)

clean:
	rm -rf lib $(OUT)

antlr: lib/$(ANTLR4_JAR) $(PFILE) $(LFILE)
	$(ANTLR) $(LFILE) -package antlr
	$(ANTLR) $(PFILE) -package antlr
	rm -rf $(AG)/*.tokens
	rm -rf $(AG)/*.interp
	mv $(AG)/*.java $(MAIN)/antlr

compile: antlr
	mkdir -p $(OUT)/class/$(MAIN)
	javac -cp lib/$(ANTLR4_JAR):$(MAIN) -d $(OUT)/class/$(MAIN) $(MAIN)/Main.java

run: compile
	@echo ""
	@echo "Running..."
	java -cp $(OUT)/class:lib/$(ANTLR4_JAR) Main
	@echo "Done."

.PHONY: all clean antlr run compile