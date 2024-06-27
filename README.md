# Codex Naturalis

**Software Engineering** project - A.Y. 2023/2024

**Professor**: San Pietro Pierluigi

**Authors:** 
* Silvia Mosca
* Martina Missana
* Antonio Oliva
* Giorgio Micheli

## Implemented features


| Functionality            | Status |
|:-------------------------|:------------------------------------:|
| Basic rules              |✅|
| Complete rules           |✅|
| Socket                   |✅|
| RMI                      |✅|
| TUI                      |✅|
| GUI                      |✅|
| Chat                     |✅|
| Multiple Games           |✅|
| Persistence              | ⛔|
| Disconnection resilience | ⛔|


## How to run

Please note that this application is fully supported on Linux and Widows OS.

### Client

You can run the client version of the application by following these instructions:

1. Download the client JAR either for the GUI or the TUI
2. Open the terminal
3. Go to the directory containing the JAR file of the client 
4. Execute the client JAR using one of the following commands, adding as the first argument your (client) IP address:

   TUI:
    ```
    java -jar PSP001-1.0-SNAPSHOT-clientTUI.jar <client IP address>

    ```

   GUI:
    ```
    java -jar PSP001-1.0-SNAPSHOT-clientGUI.jar <client IP address>

    ```

You can choose between TCP and RMI once the program has started.



### Server
You can run the server version of the application by following these instructions:

1. Download the server JAR
2. Open the terminal
3. Go to the directory containing the JAR file of the server
4. Execute the server JAR using the following command, adding as the first argument the TCP port, as the second argument the RMI port and as the third argument the server IP address:
    ```
    java -jar PSP001-1.0-SNAPSHOT-server.jar <TCP port> <RMI port> <server IP address>
    
    ```



