# Changelog

## 0.4.0

- Tool assets can now live in `config/tinkersschema/resources/`, so a custom tool no longer needs Resource Loader to supply its model, textures or name. Resource Loader still works if you already use it.
- Per-part repair modifiers in a tool config now take effect. Before, the game ignored those values and always used the default repair rate.

## 0.3.2

- An empty chapter no longer appears in the Tinkers' book when the mod runs without any tool config, and a setup without configs adds nothing to the game.

## 0.3.1

- Repairing a tool no longer crashes the game when its config points at a part the tool does not have. Invalid entries are dropped at load time, and a tool left with none falls back to its first part.

