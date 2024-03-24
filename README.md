# ProfessionalPunishments
![](https://img.shields.io/github/license/ProOmnes/ProfessionalPunishments) ![](https://img.shields.io/github/last-commit/ProOmnes/ProfessionalPunishments)

ProfessionalPunishments is a professional punishment system with bans, mutes and warnings for PaperMC server software
(Latest plugin version: **1.20.4-R0.1-1.0.0-SNAPSHOT**).

## Features

* Temporarily ban players for bad behavior
* Temporarily mute players from the chat
* Temporarily warn players for bad behavior
* Get all logs about bans, mutes and warnings
* Remove active punishments
* Clear all log data of a player
* Edit all commands and permissions
* Edit all chat messages in this plugin
* Use databases like MySQL or MongoDB or use local files to save data

## How to install

1. Download the latest .jar file.
2. Put the .jar file in the ‘plugins‘ folder. *(If you are not using ‘paper‘ as server software, please install the
   plugin ‘MiniMessage‘.)*
3. Stop and restart the server.
4. Edit configuration files in config.yml.
5. Restart the server and have fun using ProfessionalPunishments.

## Commands and permissions

| Command          | Description                                           | Parameters                                                                                         | Permission                           |
|------------------|-------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------------------------|
| ban              | Ban a player temporarily for a certain reason.        | /ban <`string`: player> <`string`: id>                                                             | punishments.command.ban              |
| banlog           | Get a history of all bans of this player.             | /banlog <`string`: player>                                                                         | punishments.command.banlog           |
| checkban         | Check if a player is banned and get more information. | /checkban <`string`:player>                                                                        | punishments.command.checkban         |
| editban          | Change the duration or the reason for a punishment.   | /editban <`string`:player> \<`string`:[reason\|duration]> <`string`:[newReason\|newTimeFormat]     | punishments.command.editban          |
| tempban          | Ban a player temporarily for a certain reason.        | /tempban \<`string`:player> \<`string`:timeFormat> \<`string`:reason>                              | punishments.command.tempban          |
| unban            | Cancel a punishment.                                  | /unban \<`string`:player> \<`string`:reason>                                                       | punishments.command.unban            |
| mute             | Mute a player temporarily for a certain reason.       | /mute <`string`: player> <`string`: id>                                                            | punishments.command.mute             |
| mutelog          | Get a history of all mutes of this player.            | /mutelog <`string`: player>                                                                        | punishments.command.mutelog          |
| checkmute        | Check if a player is muted and get more information.  | /checkmute <`string`:player>                                                                       | punishments.command.checkmute        |
| editmute         | Change the duration or the reason for a punishment.   | /editmute <`string`:player> \<`string`:[reason\|duration]> <`string`:[newReason\|newTimeFormat]    | punishments.command.editmute         |
| tempmute         | Mute a player temporarily for a certain reason.       | /tempmute \<`string`:player> \<`string`:timeFormat> \<`string`:reason>                             | punishments.command.tempmute         |
| unmute           | Cancel a punishment.                                  | /unmute \<`string`:player> \<`string`:reason>                                                      | punishments.command.unmute           |
| warn             | Warn a player temporarily for a certain reason.       | /warn <`string`:player> <`string`: id>                                                             | punishments.command.warn             |
| warnlog          | Get a history of all warnings of this player.         | /warnlog <`string`:player>                                                                         | punishments.command.warnlog          |
| warnings         | Get all active warnings of this player.               | /warnings \<`string`:player>                                                                       | punishments.command.warnings         |
| editwarning      | Change the duration or the reason for a warning.      | /editwarning <`string`:player> \<`string`:[reason\|duration]> <`string`:[newReason\|newTimeFormat] | punishments.command.editwarning      |
| tempwarn         | Warn a player temporarily for a certain reason.       | /tempmute \<`string`:player> \<`string`:timeFormat> \<`string`:reason>                             | punishments.command.tempwarn         |
| unwarn           | Cancel a warning.                                     | /unwarn \<`string`:player> \<`string`:warnID> \<`string`:reason>                                   | punishments.command.unwarn           |
| checkpunishment  | Check if a punishment id exists and get data.         | /checkpunishment \<`string`:punishmentID>                                                          | punishments.command.checkpunishment  |
| clearlog         | Clear a certain log type of a player.                 | /clearlog \<`string`:player> \<`string`:punishmentType>                                            | punishments.command.clearlog         |
| deletepunishment | Delete a certain log entry of a player by the log id. | /deletepunishment \<`string`:punishmentID>                                                         | punishments.command.deletepunishment |

## Showcase


## Maven Dependency

### Repository
```xml
<repository>
   <id>proomnes-repository-snapshots</id>
   <name>ProOmnes Repository</name>
   <url>https://repo.proomnes.net/snapshots</url>
</repository>
```

### Dependency
```xml
<dependency>
   <groupId>net.proomnes.professionalpunishments-sp</groupId>
   <artifactId>ProfessionalPunishments-paper-1.20.4-R0.1</artifactId>
   <version>1.0.0-SNAPSHOT</version>
</dependency>
```
## Contact & reporting issues
![](https://img.shields.io/github/stars/ProOmnes/ProfessionalPunishments) ![](https://img.shields.io/github/forks/ProOmnes/ProfessionalPunishments) ![](https://img.shields.io/github/watchers/ProOmnes/ProfessionalPunishments)

I am very happy to receive any constructive feedback. You can simply leave a message in
the [GitHub Discussions](https://github.com/orgs/ProOmnes/discussions/categories/feedback).

Please report issues on the [GitHub Page](https://github.com/ProOmnes/ProfessionalPunishments/issues) of this plugin.
Every issue is investigated immediately and validated and solved as quickly as possible.

If you are a developer and want to contribute to this plugin, you can
simply [create a pull request](https://github.com/ProOmnes/ProfessionalPunishments/pulls). Please read
the [contribution rules](https://docs.proomnes.net/contribution-rules) before contributing.

![](https://img.shields.io/github/issues-closed/ProOmnes/ProfessionalPunishments) ![](https://img.shields.io/github/issues-pr/ProOmnes/ProfessionalPunishments)
