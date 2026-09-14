# Tinkers' Schema

A data-driven tool registration framework for **Tinkers' Construct 2** on Minecraft 1.12.2. Define custom melee weapons and harvest tools entirely through JSON — no Java code required.

## What It Does

- Registers new tools into the Tinkers' Construct tool system
- Each tool is defined by a single JSON file in `config/tinkersschema/tools/`
- Supports custom parts, stats, traits, GUI layouts, and crafting station routing (Tool Station / Tool Forge)
- All tools behave as first-class Tinkers' items: craftable in the Tool Station, repairable, modifiable, and compatible with existing materials
- Existing Tinkers' Construct content is never modified — the mod only adds new entries

## Recommended Companions

| Mod | Purpose |
|---|---|
| [Resource Loader](https://www.curseforge.com/minecraft/mc-mods/resource-loader) | Load custom models, textures, and language files without packaging a resource pack |

## Current Scope

Phase 1 supports two base tool types:

| `tooltype` | Base Class | Use Case |
|---|---|---|
| `sword` | `SwordCore` | Melee weapons |
| `aoe` | `AoeToolCore` | Harvest tools with area-of-effect mining |

## File Layout

```
.minecraft/
├── config/
│   └── tinkersschema/
│       └── tools/
│           └── <tool_name>.json
└── resources/
    └── tinkersschema/
        ├── lang/
        ├── models/item/tools/
        └── textures/items/tools/
```

## JSON Format

```json
{
  "id": "namespace:tool_name",
  "tooltype": "sword",
  "station": "table",
  "categories": ["WEAPON"],
  "traits": ["splintering"],
  "repairParts": [1, 2],
  "repairModifiers": { "1": 1.5, "2": 1.5 },
  "parts": [
    { "type": "handle", "item": "tconstruct:tool_rod" },
    { "type": "head",   "item": "tconstruct:bow_limb" },
    { "type": "head",   "item": "tconstruct:bow_limb" }
  ],
  "guiSlots": ["center", "topright", "bottomleft"],
  "stats": {
    "damagePotential": 0.9,
    "attackSpeed": 1.6,
    "knockback": 1.1,
    "bonusAttack": 0.5,
    "attackMultiplier": 1.1,
    "durabilityMultiplier": 1.2,
    "damageCutoff": 15.0,
    "repairModifier": 1.0
  }
}
```

### Key Fields

| Field | Required | Description |
|---|---|---|
| `id` | Yes | Registry name (`namespace:path`) |
| `tooltype` | Yes | `sword` or `aoe` |
| `parts` | Yes | Ordered list of tool parts |
| `station` | No | `table` (default) or `forge` |
| `categories` | No | `WEAPON`, `HARVEST` |
| `traits` | No | Intrinsic trait IDs |
| `guiSlots` | No | Slot positions (`topleft`, `center`, etc.) or `"x,y"` coordinates |
| `stats` | No | All numeric values default to sensible baselines |

## Assets

Models and textures follow the standard resource pack layout. Each tool requires:

- `models/item/tools/<tool_name>.tcon.json` — layered tool model
- `textures/items/tools/<tool_name>/<layer>.png` — grayscale layer textures (16×16)
- `lang/<locale>.lang` — tool name and description

Textures are grayscale; Tinkers' Construct applies material colors at render time.

## Roadmap

- **Phase 2**: Additional tool types (ranged weapons, thrown weapons, armor)
- **Phase 3**: Custom tool part registration via JSON — currently only vanilla Tinkers' parts can be used

## License

MIT (mod code). ForgeDevEnv template by CleanroomMC retains its own MIT license.