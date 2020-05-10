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
            "0.5|2-7*CoreAssets:Dirt",
            "0.1|20*CoreAssets:Stone"
        ],
        "itemDrops": [
            "1-2*CoreAdvancedAssets:Door",
            "CoreAssets:Pickaxe"
        ]
    }
}
```

## Lootables

```
{
  "Lootable": {
    "lootEntries": [
      {
        "item": "CoreAdvancedAssets:Door"
      },
      {
        "item": "CoreAssets:Shovel",
        "frequency": 50
      }
    ]
  }
}
```

## Commands 
  
mark a bunch of prefabs using "Lootable"  
use `giveRandomItem` ingame lot of times  
