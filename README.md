
NoCheatPlus
---------
[![Build Status](https://ci.codemc.io/buildStatus/icon?job=Updated-NoCheatPlus%2FUpdated-NoCheatPlus)](https://ci.codemc.io/job/Updated-NoCheatPlus/job/Updated-NoCheatPlus/)
[![Discord](https://img.shields.io/discord/598285007496151098?label=discord&logo=discord)](https://discord.gg/NASKHYc)

Updated-NoCheatPlus is a continuation of the famous anti-cheat plugin NoCheatPlus, introduced by [NeatMonster](https://github.com/NeatMonster) and [Asofold](https://github.com/asofold) building on the code base of [NoCheat](https://github.com/md-5/NoCheat), created by [Evenprime](https://github.com/Evenprime).

NoCheatPlus attempts to enforce "vanilla Minecraft" mechanics, as well as preventing players from abusing weaknesses in Minecraft or its protocol, making your server more safe. Organized in different sections, various checks are performed to test players doing, covering a wide range including flying and speeding, fighting hacks, fast block breaking and nukers, inventory hacks, chat spam and other types of malicious behaviour.

Hints
---------
* Be sure that your Spigot/CraftBukkit and NoCheatPlus versions match together. The latest version of NCP is compatible with a wide range of CraftBukkit/Spigot versions.
* Don't use tabs in the config.yml file.
* Use [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) for full efficiency of the fight checks and other. Using a version of ProtocolLib that is supported by NCP is essential, as otherwise some checks will be disabled.
* For compatibility with other plugins such as mcMMO, citizens and more check out [CompatNoCheatPlus](https://github.com/Updated-NoCheatPlus/CompatNoCheatPlus).

Links
---------

###### Download
* [Jenkins (current)](https://ci.codemc.io/job/Updated-NoCheatPlus/job/Updated-NoCheatPlus/)
* [BukkitDev (legacy)](https://dev.bukkit.org/projects/nocheatplus/files/)
* [SpigotMC (legacy)](https://www.spigotmc.org/resources/nocheatplus2015-07-25.26/updates)
* [Jenkins (legacy)](https://ci.md-5.net/job/NoCheatPlus/)

###### Support and Documentation
* [Issues/Tickets](https://github.com/Updated-NoCheatPlus/NoCheatPlus/issues)
* [Wiki](https://github.com/Updated-NoCheatPlus/Docs)
* [Configuration](https://github.com/Updated-NoCheatPlus/Docs#configuration)
* [Permissions](https://github.com/Updated-NoCheatPlus/Docs/blob/master/Settings/Permissions.md)
* [Commands](https://github.com/Updated-NoCheatPlus/Docs/blob/master/Settings/Commands.md)

###### Developers
* [License (GPLv3)](https://github.com/Updated-NoCheatPlus/NoCheatPlus/blob/master/LICENSE.txt)
* [API](https://github.com/Updated-NoCheatPlus/Docs/blob/master/Development/API.md)
* [Contribute](https://github.com/Updated-NoCheatPlus/NoCheatPlus/blob/master/CONTRIBUTING.md)

###### Related Plugins
* [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)
* [CompatNoCheatPlus](https://dev.bukkit.org/projects/compatnocheatplus-cncp/)

###### Obtain a build of Spigot
* [Get the latest BuildTools.jar](https://hub.spigotmc.org/jenkins/job/BuildTools/)
* [Run according to instructions](https://www.spigotmc.org/wiki/buildtools/)
* ([Server installation instructions](https://www.spigotmc.org/wiki/spigot-installation/))

Compiling NoCheatPlus
---------
```./gradlew clean build```


Jar will be in build/libs/
