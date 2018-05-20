# Welcome to StackEdit!

Hi! I'm your first Markdown file in **StackEdit**. If you want to learn about StackEdit, you can read me. If you want to play with Markdown, you can edit me. If you have finished with me, you can just create new files by opening the **file explorer** on the le**Depends on [RPGPlayerLeveling](https://www.spigotmc.org/resources/rpg-player-leveling.11096/) and [Citizens](https://www.spigotmc.org/resources/citizens.13811/)**

# Table Of Contents
<ul>
  <li><a href="#known-bugs">Known Bugs</a></li>
  <li><a href="#dependencies">Dependencies</a></li>
  <li><a href="#commands">Commands and Permissions</a></li>
  <li>
    <a href="#getting-started-with-the-altar">How to use the altar</a>
    <ul>
      <li><a href="#getting-started-with-the-altar">Getting Started</a></li>
      <li><a href="#spell-components">Spell Components</a></li>
      <li><a href="#crafting-spells">Crafting Spells</a></li>
    </ul>
  </li>
  <li>
    <a href="#blackhole">Default Spells</a>
    <ul>
      <li><a href="#blackhole">Blackhole</a></li>
      <li><a href="#fling">Fling</a></li>
      <li><a href="#freeze">Freeze</a></li>
      <li><a href="#missile">Missile</a></li>
      <li><a href="#peek">Peek</a></li>
      <li><a href="#shield">Shield</a></li>
      <li><a href="#teleport">Teleport</a></li>
      <li><a href="#wallhack">Wallhack</a></li>
      <li><a href="#zeus">Zeus</a></li>
    </ul>
  </li>
  <li>
    <a href="#files">Files</a>
    <ul>
      <li><a href="#spell-folder">Spells Folder</a></li>
      <li><a href="#config.yml">config.yml</a></li>
      <li><a href="#inventories.yml">inventories.yml</a></li>
      <li><a href="#messages.yml">messages.yml</a></li>
      <li><a href="#papers.yml">papers.yml</a></li>
      <li><a href="#shops.yml">shops.yml</a></li>
    </ul>
  </li>
  <li>
    <a href="#spell-functions">Creating spells</a>
    <ul>
      <li><a href="#special-actions">Special Actions</a></li>
      <li><a href="#available-actions">Actions</a></li>
      <li><a href="#available-particles">Particles</a></li>
      <li><a href="#placeholders">Placeholders</a></li>
    </ul>
  </li>
</ul>


## Known Bugs:
Force spells move NPCs


## Dependencies
 * [RPGPlayerLeveling](https://www.spigotmc.org/resources/rpg-player-leveling.11096/)
 * [Citizens](https://www.spigotmc.org/resources/citizens.13811/)
 * [EffectLib](https://dev.bukkit.org/projects/effectlib)


## Commands
- /magic bazar  -  Spawn in the shop structure
- /magic structurebazar  -  Spawns the NPC for spells
- /magic paperbazar  -  Spawns the NPC for papers
- /magic wandbazar  -  Spawns the NPC for wands
- /magic wanditembazar  -  Spawns in the NPC for wand items
- /magic altaritembazar  -  Spawns in the NPC for altar items
- /magic create paper (level:power:area) (levelPowerDistance) [Shape]  -  Creates a paper with given values
- /magic create spell (spellName)  -  Gives the named spell
- /magic create wand (spellName) (level) (power) (shape) (distance)  -  Creates a wand with the given values
- /magic reload  -  Reloads the plugin
- /magic chest  -  Opens the wizards chest
- /magic menu  -  Opens the main menu
- /magic adminmenu  -  Opens the admin menu

## Permissions
- rpgmagic.npcsetup  -  Allows player to edit normal NPCs
- rpgmagic.wandsetup  -  Allows player to edit wand NPC
- rpgmagic.bazar  -  Allows player to use /magic bazar
- rpgmagic.bazarwand  -  Allows player to spawn the wand item NPC
- rpgmagic.bazaraltar  -  Allows player to spawn the altar item NPC
- rpgmagic.npc  -  Allows player to spawn in NPCs
- rpgmagic.create  -  Allows player to spawn in spells, papers and wands
- rpgmagic.reload  -  Allows player to reload the plugin
- rpgmagic.chest  -  Allows player to open the wizards chest
- rpgmagic.menu  -  Allows player to open the main menu
- rpgmagic.adminmenu  -  Allows player to open the admin menu


## Getting started with the altar

The alter is used for crafting all of the different spells. Spells are made up of two components which are explained later
Materials Required:

* 8 Lapis Blocks
* 4 Restone Torches
* 4 Redstone Dust
* 1 Beacon
* 1 Enchantment table


Step one<br>
![Step one](https://i.imgur.com/WgrtxJp.png)

Step two<br>
![Step two](https://i.imgur.com/o1wq0h4.png)

## Spell Components
All components can be obtained from one of the NPC shops
Spell
The spell you want to learn must be obtained from a shop or other source. This is just used to hold the spell name and effects.
Papers
There are three types of papers that set out different restrictions for the spell and all three are required in the crafting of a wand:
* Level
* Power
* Area

**Level** is used to set the minimum level requirement from RPG Player Leveling
<br>**Power** is used for things such as damage or strength depending on the spell it is used with
<br>**Area** is used to set the shape that the spell effects. The available shapes are cone, circle and line

## Crafting spells
Crafting a magic wand requires the following materials:
* 1 End rod
* 1 Level Paper
* 1 Power Paper
* 1 Area Paper
* 1 Spell Item


Crafting Recipe: The papers can be arranged in any order<br>
![Crafting recipe](https://i.imgur.com/ZawJe6j.png)


## Default Spells
### Blackhole

This spell creates a blackhole that sucks players and mobs into it. When you get sucked into it you get blindness and nausea aswell as getting damaged and teleported to another dimension
<br>![spell picture](http://plugins.bumblebee.gq/data/wTNY3lfmScw4yWziIsPJ/spellPictures/blackhole.png)

### Fling

This spell throws you up in the air towards where you are facing
<br>![spell picture](http://plugins.bumblebee.gq/data/wTNY3lfmScw4yWziIsPJ/spellPictures/fling.png)

### Freeze

This spell freezes the players in a radius around you
<br>![spell picture](http://plugins.bumblebee.gq/data/wTNY3lfmScw4yWziIsPJ/spellPictures/freeze.png)

### Missile

This spell shoots a tracking missile from your location towards your target
<br>![spell picture](http://plugins.bumblebee.gq/data/wTNY3lfmScw4yWziIsPJ/spellPictures/missile.png)

### Peek

This spell allows you to see through stone and dirt to quickly find ores
<br>![spell picture](http://plugins.bumblebee.gq/data/wTNY3lfmScw4yWziIsPJ/spellPictures/peek.png)

### Shield

This spell creates a shield around you that pushes other players and mobs away from you
<br>![spell picture](http://plugins.bumblebee.gq/data/wTNY3lfmScw4yWziIsPJ/spellPictures/shield.png)

### Teleport

This spell allows you to get to hard to reach locations quickly by teleporting you
<br>![spell picture](http://plugins.bumblebee.gq/data/wTNY3lfmScw4yWziIsPJ/spellPictures/teleport.png)

### Wallhack

This spell allows you to see other players through walls
<br>![spell picture](http://plugins.bumblebee.gq/data/wTNY3lfmScw4yWziIsPJ/spellPictures/wallhack.png)

### Zeus

This spell summons lightning down from the skys
<br>![spell picture](http://plugins.bumblebee.gq/data/wTNY3lfmScw4yWziIsPJ/spellPictures/zeus.png)

## Files
### Spell Folder

The spells folder contains all the spells that are created

### Config.yml

Config.yml contains the basic configuration

```
#Name of the magic alter
alterName: "Altare Magico"

#/magic bazar schematic to paste
bazarSchematic: "test"

wandItem:
display: "&6Catalizzatore"
lore:
- "&8&k111111111111111111111"
- "&4MAGIA: &5%spell%"
- "&4Potenza Magica: &5%totalpower%"
- "&4Livello Minimo: &5%lvl%"
- "&4Area d'Effetto:"
- "&5%area% di %distance% Metri"
- "&4Descrizione:"
- "&5%description%"

spellItem:
item: STRUCTURE_VOID
data: 0
durability: 0
name: "&6&lMetamagia"
lore:
- '&6&lTipo: &3%type%'
- '&6&lNome: &3%name%'
- ' '
- '&6&lDescrizione: &e%desc%'
addLore:
- "&6Price: %price%"

#NPC Name and Skin
npcs:
paper:
name: "Paper Bazar"
skin: "BumbleBeee_"
addLore:
- "&6Price: %price%"
wand:
name: "Wand Bazar"
skin: "BumbleBeee_"
addLore:
- "&6Price: %price%"
structure:
name: "Structure Bazar"
skin: "BumbleBeee_"
addLore:
- "&6Price: %price%"
altar:
name: "Alter Bazar"
skin: "BumbleBeee_"
addLore:
- "&6Price: %price%"
wanditems:
name: "Wand Item Bazar"
skin: "BumbleBeee_"
addLore:
- "&6Price: %price%"
```


### Inventories.yml

This file contains the message translations for all the GUIs

```#2 or more titles cannot be the same
inventory:
	npcSetupTypeSelection:
		title: "Select a shop type"
	itemNames:
		wand: "&6Wand"
		paper: "&fPaper"
		structure: "&cStructure"
	npcPaperOptions:
		title: "Select an option"
	itemNames:
		addPaper: "&aAdd paper"
		removePaper: "&cDelete paper"
		deleteNPC: "&cDelete NPC"
	addPaper:
		title: "Insert papers to add"
	itemNames:
		save: "&aSave changes"
		type: "&5%type%"
		price: "&6Click to change price: %price%"
	removePaper:
		title: "Click to remove a paper"
	paperShop:
		title: "Click to buy a paper"
		save: "&cClose"
		next: "&aNext"
		previous: "&aPrevious"
	structureShop:
		title: "Click to buy a spell"
		close: "&cClose"
		next: "&aNext"
		previous: "&aPrevious"
	selectPaperType:
		title: "Select a paper type"
		level: "&6Level"
		effectArea: "&6Effect Area"
		power: "&6Power"
	globalChest:
		title: "Global Chest"
		playerPaperMenu:
		title: "Your papers"
		next: "&aNext"
		previous: "&aPrevious"
	playerPaperAdminMenu:
		title: "Users papers"
		next: "&aNext"
		previous: "&aPrevious"
	playerSpellMenu:
		title: "Your spells"
		next: "&aNext"
		previous: "&aPrevious"
	playerSpellAdminMenu:
		title: "Users spells"
		next: "&aNext"
		previous: "&aPrevious"
	playerWandMenu:
		title: "Your wands"
		next: "&aNext"
		previous: "&aPrevious"
	playerWandAdminMenu:
		title: "Users wands"
		next: "&aNext"
		previous: "&aPrevious"
	playerMenu:
		title: "Select a category"
		wand: "&6Wand"
		paper: "&fPaper"
		structure: "&cStructure"
		close: "&cClose"
	playerAdminMenu:
		title: "Admin: Select a category"
		wand: "&6Wand"
		paper: "&fPaper"
		structure: "&cStructure"
		close: "&cClose"
	playerPaperSelectMenu:
		title: "Select a paper category"
		level: "&6Level"
		effectArea: "&6Effect Area"
		power: "&6Power"
	playerPaperAdminSelectMenu:
		title: "Admin: Select a paper category"
		level: "&6Level"
		effectArea: "&6Effect Area"
		power: "&6Power"
	wandShop:
	title: "Wand Shop"
	altarItemsShop:
		title: "Alter Item Shop"
	wandItemsShop:
		title: "Wand Item Shop"
	npcWandOptions:
		title: "Select a wand operation"
		addWand: "&aAdd wand"
		removeWand: "&cDelete wand"
		deleteNPC: "&cDelete NPC"
	addWand:
		title: "Insert wands to add"
		save: "&aSave changes"
		price: "&6Click to change price: %price%"
	removeWand:
		title: "Click to remove a wand"
	playerAdminMainMenu:
		title: "Select an option"
		search: "Search for a player"
		all: "View all players"
	adminPlayerMenu:
		title: "Select a player"
		next: "&aNext"
		previous: "&aPrevious"
```

### Messages.yml

This file contains the message translations for all messages sent in-game

```
invalidArguments: '&cInvalid arguments! Correct usage: %usage%'
noPermissions: '&cYou do not have the required permissions!'
reloadSuccess: '&aSuccessfully reloaded config!'
npcRemoved: '&aSuccessfully removed NPC'
notEnoughMoney: '&cYou need &e%price% &cor more to buy this!'
notANumber: '&c%number% is not a number!'
addedPaper: '&aSuccessfully added paper!'
categoryNotFound: '&cFailed to find category called %type%'
typeNotFound: '&cFailed to find paper type called %type%'
shapeNotFound: '&cFailed to find a shape named %shape%'
itemGiven: '&aYou have been given an item!'
spellNotFound: '&cFailed to find a spell named %spell%'
playerNotFound: '&cFailed to find a a player named %name%'
inputLevel: '&aEnter the level'
inputPower: '&aEnter the power'
inputAreaInfo: '&aEnter the shape and area (e.g coni 4)'
invalidFormatting: '&cInvalid formatting!'
enterANumber: '&aEnter a number:'
spellOnCooldown: '&cYou must wait &e%time% &cmore seconds!'
notEnoughMana: '&cYou do not have enough mana!'
errorWithSchematic: '&cFailed to %operation% schematic%reason%'
pastedSchematic: '&aSuccessfully pasted schematic!'
notHoldingWand: '&aYou must be holding a wand!'
enterPlayerName: '&aEnter a player name to search:'
helpCommand: '&6%command%&f: %description%'
```

### Papers.yml

This file contains the settings for each type of paper

```papers:
level:
item: 339
data: 0
durability: 0
name: "&6&lMetamagia"
lore:
- "&5&lLivello: &6&l%lvl%"
- " "
- "&3Questa Metamagia assegna all''oggetto magico una"
- "&3restrizione del suo uso da parte del Mago,"
- "&3che deve avere minimo il livello indicato."
effectArea:
item: 339
data: 0
durability: 0
name: "&6&lMetamagia"
lore:
- "&5&lArea d'' Effetto: &6&l%area%"
- "&5&lDistanza: &6&l%distance%"
- " "
- "&3Questa Metamagia assegna all''oggetto magico"
- "&3un'area d'effetto specifica."
- "&3Per maggiori info, consultare la Guida Magica."
power:
item: 339
data: 0
durability: 0
name: "&6&lMetamagia"
lore:
- "&5&lPotenza: &6&l%power%"
- " "
- "&3Questa Metamagia assegna all''oggetto magico una"
- "&3potenza che andra'' a moltiplicarsi al livello del Mago"
- "&3e al tipo dell''oggetto magico."
```

### Papers.yml

This file contains the settings for each type of paper
```
papers:
	level:
		item: 339
		data: 0
		durability: 0
		name: "&6&lMetamagia"
		lore:
		- "&5&lLivello: &6&l%lvl%"
		- " "
		- "&3Questa Metamagia assegna all''oggetto magico una"
		- "&3restrizione del suo uso da parte del Mago,"
		- "&3che deve avere minimo il livello indicato."
	effectArea:
		item: 339
		data: 0
		durability: 0
		name: "&6&lMetamagia"
		lore:
		- "&5&lArea d'' Effetto: &6&l%area%"
		- "&5&lDistanza: &6&l%distance%"
		- " "
		- "&3Questa Metamagia assegna all''oggetto magico"
		- "&3un'area d'effetto specifica."
		- "&3Per maggiori info, consultare la Guida Magica."
	power:
		item: 339
		data: 0
		durability: 0
		name: "&6&lMetamagia"
		lore:
		- "&5&lPotenza: &6&l%power%"
		- " "
		- "&3Questa Metamagia assegna all''oggetto magico una"
		- "&3potenza che andra'' a moltiplicarsi al livello del Mago"
		- "&3e al tipo dell''oggetto magico."
```

### Shops.yml

This file contains the items that are contained in the altar and wand item shops
```
altarShop:
	items:
	'1':
		slot: 0
		price: 10
		item: STICK
		name: '&5Useless Magic Stick'
		lore:
		- 'Useless lore'
	'2':
		slot: 1
		price: 20
		item: BLAZE_ROD
wandShop:
	items:
	'1':
		slot: 0
		price: 10
		item: STICK
		name: '&5Another useless Magic Stick'
		lore:
		- 'Useless lore'
	'2':
		slot: 1
		price: 20
		item: BLAZE_ROD
```


### Spell Functions
**Commands:**
Syntax: CMD:sender:command
```
CMD:[CONSOLE:PLAYER]:command
CMD:CONSOLE:broadcast test
```

**Actions**
```
Syntax: ACT:actionName:argument1:argument2
```

**Particles**
```
Syntax: PAR:particleName:argument1:argument2
```

### Special Actions
**Loop**<br>
Creates a loop with the given action
```
loop:action:with:arguments::loopTime:iterations
```

loopTime: The amount of time in between each loop<br>
iterations: How many times the loop is ran<br>
Total Loop Time(in seconds) = (iterations*loopTime)/20

**Delay**<br>
Creates a delay before continuing
```
delay:timeInSeconds
```

**StoreLocation**<br>
Store the target location for a player
```
storeLocation
```

### Available Actions
**applyPotionShape**<br>
Applies a potion effect to the player
```
applyPotionRadius:POTION_EFFECT:amplifier:duration
```
All potion types are listed [here](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html)

**applyPotionAtLocation**<br>
Applies a potion effect to entities around a location
```
applyPotionAtLocation:location:radius:potion:duration:boost
applyPotionAtLocation:%storedLoc%:3:BLINDNESS:20:20
```
All potion types are listed [here](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html)

**playSoundAtLocation**<br>
Plays a sound at a location
```
playSoundAtLocation:location:SOUND:volume
playSoundAtLocation:%storedLoc%:ENTITY_ENDERDRAGON_AMBIENT:10
```
All sound types are listed [here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html)

**WallWalk**<br>
Allow the player to walk on walls
```
wallWalk:enable
wallWalk:disable
```

**WaterWalk**<br>
Allow the player to walk on water
```
waterWalk:enable
waterWalk:disable
```

**FreezePlayer**<br>
Freeze the player
```
freezePlayer
```

**UnfreezePlayer**<br>
Unfreeze the player
```
unfreezePlayer
```

**TeleportToStoredLocation**<br>
Teleport the player to a previously stored location
```
teleportToStoredLocation
```

**ShootMissileAtTarget**<br>
Shoots a trail of particles at a player and damages them
```
shootMissileAtTarget:range:particle
shootMissileAtTarget:100:FLAME
```

**pullNearbyEntityTowards**<br>
Pulls entities towards a location within a range
```
pullNearbyEntityTowards:location:range
pullNearbyEntityTowards:%storedLoc%:10
```

**DamageEntitiesAtLocation**<br>
Damages entities at a location
```
damageEntitiesAtLocation:location:amount
damageEntitiesAtLocation:%storedLoc%:1
```

**TeleportEntitiesToWorld**<br>
Teleports entities at a location to a world
```
teleportEntitiesToWorld:fromCoords:toWorld
teleportEntitiesToWorld:%storedLoc%:%world%
```

**ThrowPlayerInDirection**<br>
Force the player in the direction they are looking
```
throwPlayerInDirection:force
throwPlayerInDirection:2
```

**StrikeLightning**<br>
Strike a lightning cluster at a location
```
strikeLightning:percentToHit
strikeLightning:10
```

**AddWallHack**<br>
Give a player wall hacks
```
addWallHack
```

**RemoveWallHack**<br>
Give a player wall hacks
```
removeWallHack
```

**ProtectPlayer**<br>
Push entities away from a player
```
protectPlayer:radius:power
protectPlayer:5:0.5
```

**ChangeBlocksForPlayerInArea**<br>
Change an area of blocks just for a specific player
```
changeBlocksForPlayerInArea:MATERIAL:offset-up:offset-down
changeBlocksForPlayerInArea:BARRIER:3:3
```

**FreezePlayersInShape**<br>
Freeze all the players inside the spell effect area
```
freezePlayersInShape
```

**UnfreezePlayersInShape**<br>
unfreeze all the players inside the spell effect area
```
unfreezePlayersInShape
```

### Available Particles
**Outline Area**<br>
Outline the area of the wand shape
```
outlineArea:PARTICLE_EFFECT
```
All particle effects can be found here
outlineAreaFromCastLoc
Outline the area of the wand shape from where it was cast
```
outlineAreaFromCastLoc:particle
outlineAreaFromCastLoc:REDSTONE
```

**Helix**<br>
Creates helix particle effect
```
helix:[target:location]:duration:radius:particle:strands:curve:period
helix:location:2000:10:FLAME:8:10:10
```
target:location: Target will create the effect where the player is looking and location will create it at the players feet<br>
duration: Time in milliseconds the effect will last for<br>

**createBlackHole**<br>
Creates the particles for a black hole
```
createBlackHole
```

**TrailPlayer**<br>
Create a particle trail behind the player
```
trailPlayer:FLAME:iterations
trailPlayer:FLAME:50
```

**Shield**<br>
Create a particle trail behind the player
```
shield:PARTICLE:radius:iterations
shield:TOTEM:5:200
```

### Placeholders
- %player%
- %spell%
- %distance%
- %potential%
- %storedLoc%ft corner of the navigation bar.

