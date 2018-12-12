run: 
	java -jar executable.jar
	jar -cMf food.zip .


nosrc: application/Main.java
	javac -d . application/*.java
	jar -cfm executable.jar manifest.txt application


srcbin: src/application/Main.java
	javac -d bin src/application/*.java
	jar -cfm executable.jar manifest_srcbin.txt -C bin application

clean:
	\rm application/*.class	


cleanbin:
	\rm bin/application/*.class	