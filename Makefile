
all: build clean run

build:
	@echo "contruire le fichier JAR: "
	@mvn package
	@mv target/fastClavier-2.6.0-jar-with-dependencies.jar ./fastClavier.jar
clean:
	@echo "nettoyer les fichiers temporaires: "
	@rm -r ./target
run:	
	@echo "execution:"
	@java -jar ./fastClavier.jar



