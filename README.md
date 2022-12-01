# ECM1410
This is our submission for the ECM1417 Continuous Assessment. It contains the source code for the backend of a cycling race system written in Java. Written in a pair with [@charliems](https://www.github.com/charliems).

## Compilation Instructions (cmd)

javac -d bin/ src/cycling/*.java

jar cvf cycling.jar -C bin .

jar uvf cycling.jar -C src .

javac -d bin -cp cycling.jar src/CyclingPortalInterfaceTestApp.java

java -ea -cp bin;cycling.jar CyclingPortalInterfaceTestApp
