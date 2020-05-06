# Drops

## Drop Grammar

```
 DROP_DEFINITION   := [CHANCE '|'] [COUNT '*'] DROP
 CHANCE            := FLOAT in [0..1]
 COUNT             := INT | RANGE
 RANGE             := INT '-' INT
 DROP              := STRING
```
 
## Commands 
  
mark a bunch of prefabs using "Lootable"  
use `giveRandomItem` ingame lot of times  
