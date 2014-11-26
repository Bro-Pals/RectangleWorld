@echo off
javac -d class -cp lib/SimpleDrawing.jar src/bropals/rectangleworld/*.java src/bropals/rectangleworld/event/*.java
PAUSE
jar cfm RectangleWorldClient.jar manifest/client/MANIFEST.mf -C class .
jar cfm RectangleWorldServer.jar manifest/server/MANIFEST.mf -C class .
PAUSE
