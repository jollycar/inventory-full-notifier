# Inventory Full Notifier

## About

This mod notifies a player when their inventory is x percentage full or if there is no more room to store items already in your inventory.

I had the need for this kind of mod when I was playing on a "prison" server and could not find a similar mod on curseforge.

### Features

- This is a client side mod
- The player is notified on attack a block (AttackBlockCallback)
- You can get notified if your inventory is x percentage full (configurable)
- You can get notified if there is no more room for any stackable item already in your inventory (configurable)
- You can get notified if there is no more room for any single stackable item, like a pickaxe, already in your inventory (configurable)
- The sound played is "ENCHANT_THORNS_HIT", see SoundEvents class for possible values (not configurable yet)

### Configuration options:

- Mod enabled
- Notify for single stack items
- Notify if no more room
- Notify on inventory percentage full
- Notifier sound volume

## License

This software is available under the MIT license. See LICENSE file in this repository.
