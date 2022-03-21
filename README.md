# ECM1410-2


## Compilation Instructions (cmd)

javac -d bin/ src/cycling/*.java

jar cvf cycling.jar -C bin .

jar uvf cycling.jar -C src .

javac -d bin -cp cycling.jar src/CyclingPortalInterfaceTestApp.java

java -ea -cp bin;cycling.jar CyclingPortalInterfaceTestApp
