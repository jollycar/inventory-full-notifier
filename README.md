# Inventory Full Notififier

## About

This mod notifies a player when their inventory is full and there is no more room to store items already in your inventory.

I had the need for this kind of mod when I was playing on a "prison" server and could not find a similar mod on curseforge.

### Features

- This is a client side mod
- The player is notified on attack a block (AttackBlockCallback)
- You get notified if there is no more room for any stackable item already in your inventory. 
Non-stackable items, like a pickaxe, do not count 
- The sound played is "ENCHANT_THORNS_HIT", see SoundEvents class for possible values (not configurable yet)

### Configuration options (planned, not yet implemented):

- Toggle the notifier on/off
- Make a visual notifier. Make possible to turn on/off
- Set the percentage of how full your inventory must be before you get notified
- Choose the sound. Make possible to turn on/off

## License

This software is available under the MIT license. See LICENSE file in this repository.
