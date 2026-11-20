# Changelog

## [0.3.0] - 2026-11-20

### Added
- `thrown` tool type: throwing weapons built on `ProjectileCore` that consume their own durability as ammo, mirroring Tinkers' Construct shuriken behaviour.
- `ammo` tool type: standalone ammo built on `ProjectileCore` that never launches itself, mirroring Tinkers' Construct arrow and bolt behaviour.
- `projectile` field for `thrown` and `ammo` tools, selecting between shuriken, arrow and bolt projectile entities.
- `throwSpeed`, `cooldown` and `durabilityPerAmmo` fields for ranged tools, with sane defaults and validation at load time.
- Custom bows now resolve ammo from vanilla arrows plus every `thrown` and `ammo` tool registered by this mod, without extra configuration.
- Modifier overlay support through `<tool_name>.mod.json` beside the tool model, documented with exact resource paths.
- Default tool configs for a throwing knife and a broadhead arrow.

### Changed
- Bow crosshair type is resolved once when the tool is constructed instead of being parsed on every rendered frame.
- Config files are sorted before loading, so registration and tool station ordering are deterministic.
- Duplicate detection uses the full resource location, so `foo` and `tinkersschema:foo` are recognised as the same tool.

### Fixed
- A tool whose item id was already taken is now skipped before construction, preventing an itemless tool from lingering in the Tinkers' Construct tool registry.

### Removed
- Unused `ModifierRegister` class, unused `ToolConfigLoader.parseJson`, and the redundant `TraitApplier` readiness flag.

### Documentation
- Corrected the tool type table (`sword`, `harvest`, `bow`, `thrown`, `ammo`), completed the field reference, and added the asset path and modifier overlay guides.

## [0.2.5]

- Initial public build: data-driven `sword`, `harvest` and `bow` tool registration.
