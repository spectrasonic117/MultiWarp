# MultiWarp

MultiWarp is a Minecraft plugin that allows players to create, manage, and teleport between multiple warp points. This plugin is designed to enhance the gameplay experience by providing easy-to-use commands for warp management.

## Features

-   Create and manage warp groups.
-   Add and delete warp points within groups.
-   Teleport players to warp points.
-   Reload plugin configurations without restarting the server.
-   View plugin version and author information.

## Commands

-   `/multiwarp create <group>`: Create a new warp group.
-   `/multiwarp add <group>`: Add a warp point to an existing group.
-   `/multiwarp delete <group> <index>`: Delete a warp point from a group.
-   `/multiwarp deletegroup <group>`: Delete an entire warp group.
-   `/multiwarp tp <group>`: Teleport players to the next warp in the group.
-   `/multiwarp version`: Display the plugin version and author information.
-   `/multiwarp reload`: Reload the plugin configurations.

## Compilation

To compile the MultiWarp plugin using Gradle, follow these steps:

1. Ensure you have Gradle installed on your system. You can download it from [Gradle's official website](https://gradle.org/install/).

2 compile the plugin using the following command:

```bash
./gradlew build
```

3. Once the compilation is complete, you can install the plugin by copying the compiled JAR file to the `plugins` directory of your Minecraft server.
