# File Copier
## Description:
This script synchronizes directories from the WSL directory to the Windows file system. It skipped some directories like node_modules and dist.
## Purpose of the project:
This code has been used in a demo to try to make native scripts using Java. The topic is explained here -> https://medium.com/@rollerfdeveloper/creando-scripts-nativos-en-java-utilizando-graalvm-f11f621f9591
## Commands:
These commands are for use with GraalVM only.
```bash
javac FileCopier.java
native-image FileCopier
```
