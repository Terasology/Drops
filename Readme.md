# Drops

## Drop Grammar

```
 DROP_DEFINITION   := [CHANCE '|'] [COUNT '*'] DROP
 CHANCE            := FLOAT in [0..1]
 COUNT             := INT | RANGE
 RANGE             := INT '-' INT
 DROP              := STRING
```

```json5
{
    "DropGrammar": {
        "blockDrops": [
            "0.5|2-7*CoreBlocks:Dirt",
            "0.1|20*CoreBlocks:Stone"
        ],
        "itemDrops": [
            "1-2*CoreBlocks:Door",
            "CoreItems:Pickaxe"
        ]
    }
}
```
 
## Commands 
  
mark a bunch of prefabs using "Lootable"  
use `giveRandomItem` ingame lot of times  
