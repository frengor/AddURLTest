# A test for remapping when using `URLClassLoader.addURL(URL)`

A little test for remapping libraries loaded using `URLClassLoader.addURL(URL)`.

This is a (super) stripped down version of a library like [Libby](https://github.com/AlessioDP/libby), which allows to load libraries dynamically
in the plugin class loader. It works by calling the `addURL` method on the plugin class loader (the method is called via reflection after having
obtained access to the `java.net` module, for more info see [URLClassLoaderHelper](https://github.com/AlessioDP/libby/blob/5386c74ef8d11f397b57a07cea97bca183302328/core/src/main/java/net/byteflux/libby/classloader/URLClassLoaderHelper.java)).

## How to run

An already built binary is provided in this repo ([addurl-test-plugin-1.0.jar](https://github.com/frengor/AddURLTest/raw/main/addurl-test-plugin-1.0.jar)), otherwise just clone the repo and use `mvn package` to build from source.
The final plugin is located inside `AddURLTestPlugin/target`.

Copy the plugin inside the `plugins` folder and start your server **with the following parameter**: `--add-opens java.base/java.net=ALL-UNNAMED`.

Enter your server and break a block. If everything works, a (client-side only) zombie should have spawned at (0, 0, 0), otherwise an exception should have been thrown.

<details><summary>Example exception</summary>

```txt
java.lang.NoClassDefFoundError: net/minecraft/world/level/World
	at addurl-test-plugin-1.0.jar/com.fren_gor.addURLTestPlugin.BlockBreakListener.onBlockBreak(BlockBreakListener.java:12) ~[addurl-test-plugin-1.0.jar:?]
	at com.destroystokyo.paper.event.executor.asm.generated.GeneratedEventExecutor1.execute(Unknown Source) ~[?:?]
	at org.bukkit.plugin.EventExecutor$2.execute(EventExecutor.java:77) ~[paper-api-1.20.6-R0.1-SNAPSHOT.jar:?]
	at co.aikar.timings.TimedEventExecutor.execute(TimedEventExecutor.java:81) ~[paper-api-1.20.6-R0.1-SNAPSHOT.jar:git-Paper-71]
	at org.bukkit.plugin.RegisteredListener.callEvent(RegisteredListener.java:70) ~[paper-api-1.20.6-R0.1-SNAPSHOT.jar:?]
	at io.papermc.paper.plugin.manager.PaperEventManager.callEvent(PaperEventManager.java:54) ~[paper-1.20.6.jar:git-Paper-71]
	at io.papermc.paper.plugin.manager.PaperPluginManagerImpl.callEvent(PaperPluginManagerImpl.java:131) ~[paper-1.20.6.jar:git-Paper-71]
	at org.bukkit.plugin.SimplePluginManager.callEvent(SimplePluginManager.java:627) ~[paper-api-1.20.6-R0.1-SNAPSHOT.jar:?]
	at net.minecraft.server.level.ServerPlayerGameMode.destroyBlock(ServerPlayerGameMode.java:378) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.level.ServerPlayerGameMode.destroyAndAck(ServerPlayerGameMode.java:337) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.level.ServerPlayerGameMode.handleBlockBreakAction(ServerPlayerGameMode.java:213) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.network.ServerGamePacketListenerImpl.handlePlayerAction(ServerGamePacketListenerImpl.java:1845) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.handle(ServerboundPlayerActionPacket.java:51) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.handle(ServerboundPlayerActionPacket.java:20) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.network.protocol.PacketUtils.lambda$ensureRunningOnSameThread$0(PacketUtils.java:55) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.TickTask.run(TickTask.java:18) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.util.thread.BlockableEventLoop.doRunTask(BlockableEventLoop.java:151) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.util.thread.ReentrantBlockableEventLoop.doRunTask(ReentrantBlockableEventLoop.java:24) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.MinecraftServer.doRunTask(MinecraftServer.java:1517) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.MinecraftServer.doRunTask(MinecraftServer.java:198) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.util.thread.BlockableEventLoop.pollTask(BlockableEventLoop.java:125) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.MinecraftServer.pollTaskInternal(MinecraftServer.java:1494) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.MinecraftServer.pollTask(MinecraftServer.java:1417) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.util.thread.BlockableEventLoop.managedBlock(BlockableEventLoop.java:135) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.MinecraftServer.waitUntilNextTick(MinecraftServer.java:1383) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.MinecraftServer.runServer(MinecraftServer.java:1244) ~[paper-1.20.6.jar:git-Paper-71]
	at net.minecraft.server.MinecraftServer.lambda$spin$0(MinecraftServer.java:326) ~[paper-1.20.6.jar:git-Paper-71]
	at java.base/java.lang.Thread.run(Thread.java:1583) ~[?:?]
Caused by: java.lang.ClassNotFoundException: net.minecraft.world.level.World
	at org.bukkit.plugin.java.PluginClassLoader.loadClass0(PluginClassLoader.java:197) ~[paper-api-1.20.6-R0.1-SNAPSHOT.jar:?]
	at org.bukkit.plugin.java.PluginClassLoader.loadClass(PluginClassLoader.java:164) ~[paper-api-1.20.6-R0.1-SNAPSHOT.jar:?]
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:526) ~[?:?]
	... 28 more
```

</details>

A full log of a failing execution is available [here](https://raw.githubusercontent.com/frengor/AddURLTest/main/log.txt).

## How it works

The plugin contains the jar to load (built by the `AddURLTest` module), which is extracted into the plugin data folder before being loaded using `addURL`.

`AddURLTest` just contains a class with the `test` method, which spawns the client side zombie. The `BlockBreakEvent` listener just calls such method.

Since the `test` method uses spigot-mapped NMS code, the zombie will be successfully spawned only if the loaded jar is remapped when loaded.
