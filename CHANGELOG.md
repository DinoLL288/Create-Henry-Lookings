# Create: Henry Changelog

## 1.0.6

### Fixes
- **Golden Mixer now processes a full stack continuously instead of stalling between crafts.**
  - Fixed the processing-time calculation. The 2.5× speed multiplier was being fed *into* the `log2` of the mixer's processing formula, which collapsed the craft time to a single tick at higher RPMs.
  - Because the mixer was running effectively instantly, it outran the basin's output buffers. Create then refused to chain the next craft, so the Golden Mixer visibly retracted and stopped every few recipes before restarting.
  - The multiplier is now applied to the final duration, so the Golden Mixer is a genuine 2.5× faster Mechanical Mixer and keeps running until it can no longer match a recipe.
