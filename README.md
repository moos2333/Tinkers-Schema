# Tinkers' Schema

Tinkers' Schema is a data-driven tool registration framework for **Tinkers' Construct 2** on Minecraft 1.12.2. It registers custom melee weapons, harvest tools, bows, throwing weapons and ammo from JSON files, so no Java code is involved.

## What It Does

- Every tool the mod registers becomes a full member of the Tinkers' Construct tool system
- A tool is defined by a single JSON file in `config/tinkersschema/tools/`
- The JSON declares the tool's parts, stats, traits, GUI layout and crafting station (Tool Station or Tool Forge)
- Each tool behaves as a first-class Tinkers' item: it is craftable in the Tool Station, repairable, modifiable, and compatible with existing materials
- Existing Tinkers' Construct content is never modified; the mod only adds new entries

## Recommended Companions

| Mod | Purpose |
|---|---|
| [Resource Loader](https://www.curseforge.com/minecraft/mc-mods/resource-loader) | Load custom models, textures, and language files without packaging a resource pack |

## Current Scope

| `tooltype` | Base Class | Use Case |
|---|---|---|
| `sword` | `SwordCore` | Melee weapons |
| `harvest` | `AoeToolCore` | Harvest tools with area-of-effect mining |
| `bow` | `BowCore` | Bows |
| `thrown` | `ProjectileCore` | Throwing weapons that spend their own durability as ammo |
| `ammo` | `ProjectileCore` | Standalone ammo items consumed by launchers; cannot be thrown by hand |

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
| `tooltype` | Yes | `sword`, `harvest`, `bow`, `thrown`, or `ammo` |
| `parts` | Yes | Ordered list of tool parts |
| `station` | No | `table` (default) or `forge` |
| `categories` | No | Any of `TOOL`, `WEAPON`, `HARVEST`, `AOE`, `LAUNCHER`, `PROJECTILE`, `NO_MELEE` |
| `traits` | No | Intrinsic trait IDs |
| `guiSlots` | No | Slot positions (`topleft`, `center`, etc.) or `"x,y"` coordinates |
| `stats` | No | All numeric values default to sensible baselines |
| `effectiveMaterials` | `harvest` | Block materials the tool can mine; without it the tool mines nothing |
| `harvestToolClass` | No | `pickaxe`, `axe`, `shovel`, … |
| `aoeRange` | No | `[width, height, depth]`, defaults to `[1, 1, 1]` |
| `ammo` | No | Ammo items for `bow`; defaults to vanilla arrows plus every `ammo`/`thrown` tool |
| `crosshair` | No | `X`, `INVERSE`, `PLUS`, `T`, `SQUARE` (default) |
| `projectile` | No | Projectile entity for `thrown` and `ammo`: `shuriken` (default), `arrow`, `bolt` |
| `throwSpeed` | No | Launch speed for `thrown`, defaults to `2.1` |
| `cooldown` | No | Ticks between throws for `thrown`, defaults to `4` |
| `durabilityPerAmmo` | No | Durability per shot for `thrown` and `ammo`, defaults to `10` |

## Assets

Paths below are relative to a namespace root: `assets/<namespace>/` inside a resource pack, or `resources/<namespace>/` when using Resource Loader. The namespace is the one in the tool's `id`, so `tinkersschema:pickaxe_axe` reads from `assets/tinkersschema/`.

| Path | Purpose |
|---|---|
| `models/item/tools/<tool_name>.tcon.json` | Layered tool model |
| `models/item/tools/<tool_name>.mod.json` | Modifier overlay map (optional) |
| `textures/items/tools/<tool_name>/<layer>.png` | Grayscale layer textures (16×16) |
| `lang/<locale>.lang` | Tool name and description |

Textures are grayscale; Tinkers' Construct applies material colors at render time.

### Modifier Overlays

Tinkers' Construct indexes its own modifier textures by tool name, and those tables only list the built-in tools, so a custom tool name never matches them. A `.mod.json` beside the tool model supplies the mapping for that tool — for `tinkersschema:pickaxe_axe` that is `assets/tinkersschema/models/item/tools/pickaxe_axe.mod.json`:

```json
{
  "textures": {
    "sharpness": "tconstruct:items/pickaxe/mod_sharpness",
    "diamond": "tconstruct:items/pickaxe/mod_diamond"
  }
}
```

Keys are modifier identifiers; values are the overlay textures drawn on top of the tool model, so reuse the textures of the vanilla tool the model is based on to keep the UV layout aligned. Modifiers absent from the map render without an overlay, and the file is only read while the tool model bakes.

## Roadmap

- **Next**: Custom tool parts will be registerable from JSON; currently only vanilla Tinkers' parts can be used
- Armor is not planned: Tinkers' Construct has no armor system on 1.12.2

## License

MIT (mod code). ForgeDevEnv template by CleanroomMC retains its own MIT license.