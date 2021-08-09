# ArcaneClientAPI
The purpose of this API is to communicate with users using Arcane Client. This is achieved by
using plugin messaging channels on the Bukkit and Bungeecord platforms. This API can be easily expanded
onto so it can support other server platforms like for instance the Velocity proxy.

## Features
- Arcane Only **-** Enabling this makes it so only users on the client can join the server.
- Version Selection **-** (TODO) Select which versions of Minecraft users can use to connect to your server.
- Disable Mods **-** (TODO) This is extremely useful if you want to base your server around a specific
 gamemode and you want to prevent users from using specific mods in the client.

## Example
Here is a simple example of how to get the API so you can use it in the future.
See the example module for more information.
```java
ArcaneClientAPI api = ArcaneClientAPI.builder()
        .withAllowedVersions(ClientVersion.values()) // An array of client versions that are allowed to join
        .withProperties(new ArcaneClientAPI.ClientProperty[] {
                ArcaneClientAPI.ClientProperty.VOICE_CHAT // Tells the client that voice chat is supported on this server
        }).build();
api.withAdapter(new BukkitAdapter(api, this)); // Set the adapter for the api to use
```


## License
[MIT](https://choosealicense.com/licenses/mit/)