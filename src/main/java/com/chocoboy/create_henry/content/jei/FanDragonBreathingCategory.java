package com.chocoboy.create_henry.content.jei;

import com.chocoboy.create_henry.content.recipes.DragonBreathingRecipe;
import net.minecraft.world.level.block.Blocks;

public class FanDragonBreathingCategory extends HenryFanProcessingCategory.SimpleBlock<DragonBreathingRecipe> {

    public FanDragonBreathingCategory(Info<DragonBreathingRecipe> info) {
        super(info, Blocks.DRAGON_HEAD);
    }

}
