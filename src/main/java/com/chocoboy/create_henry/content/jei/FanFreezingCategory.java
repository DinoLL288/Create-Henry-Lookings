package com.chocoboy.create_henry.content.jei;

import com.chocoboy.create_henry.content.recipes.FreezingRecipe;
import net.minecraft.world.level.block.Blocks;

public class FanFreezingCategory extends HenryFanProcessingCategory.SimpleBlock<FreezingRecipe> {

    public FanFreezingCategory(Info<FreezingRecipe> info) {
        super(info, Blocks.POWDER_SNOW);
    }

}
