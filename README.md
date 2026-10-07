# Mutant Villager — Minecraft 1.20.1 Forge port

Port target: Minecraft 1.20.1 / Forge 47.4.26.

Original source analyzed: `mutantvillager-0.1.jar` (original metadata identifies Minecraft Forge 1.16.5).

Included:
- Mutant Villager entity, 1.4 x 2.5 hitbox
- 300 HP, 0.33 movement speed, 3 attack damage, 1 knockback
- Original goal/target behavior and projectile/fall/trident immunity
- Raw and cooked mutant villager meat
- Spawn egg and creative tab
- Original texture carried over at 128x128
- Smelting and smoking recipes
- Broad overworld monster spawning matching the original mod's wide biome coverage
- Right-click trade-display menu with the original six-item trade pool
- Taming/healing behavior using pumpkin pie

## Build

This is a ForgeGradle source project. Install Java 17 and use the official Forge 1.20.1 MDK, or place the project files into an existing Forge 1.20.1 MDK project. Then run:

`gradlew build`

The compiled jar will be in `build/libs/`.

The build cannot be executed in this environment because the Forge/Minecraft Gradle artifacts are not locally cached and this execution environment has no outbound Maven/DNS access.
