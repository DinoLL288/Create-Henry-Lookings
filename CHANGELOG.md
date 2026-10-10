# Create: Henry Changelog

## 1.0.7

### Fixes
- **Smart Hopper crafting recipe no longer uses an empty tag.**
  - The recipe previously required `forge:plates/brass`, which no mod defines on NeoForge 1.21.1, making the Smart Hopper uncraftable. It now uses the conventional `c:plates/brass` tag (Create's Brass Plates).
- **Golden Mixer recipes now have proper names in recipe viewers (JEI/EMI).**
  - The three Golden Mixer categories no longer display as a raw `create_henry` translation key. Added the missing English names: *Golden Mixer Mixing*, *Golden Mixer Automatic Shapeless Crafting*, and *Golden Mixer Automatic Brewing*.

### Changes
- **Migrated all tags from the legacy `forge:` namespace to the conventional `c:` namespace.**
  - Item tags: `stripped_logs`, `stripped_wood`, `nuggets`, `nuggets/coal`, `nuggets/lapis`, `rubbers`, `raw_rubbers`, `crude_rubbers`, `dusts`, `dusts/obsidian`, and `buckets/*`.
  - Fluid tags: `sap`, `chocolate`, `vanilla`, `strawberry`, `glowberry`, `pumpkin`.
  - The mod's own items/fluids now register under the standard conventional tags, improving interoperability with other mods in 1.21.1.

## 1.0.6

### Fixes
- **Golden Mixer now processes a full stack continuously instead of stalling between crafts.**
  - Fixed the processing-time calculation. The 2.5× speed multiplier was being fed *into* the `log2` of the mixer's processing formula, which collapsed the craft time to a single tick at higher RPMs.
  - Because the mixer was running effectively instantly, it outran the basin's output buffers. Create then refused to chain the next craft, so the Golden Mixer visibly retracted and stopped every few recipes before restarting.
  - The multiplier is now applied to the final duration, so the Golden Mixer is a genuine 2.5× faster Mechanical Mixer and keeps running until it can no longer match a recipe.
