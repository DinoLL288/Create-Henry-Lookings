package com.chocoboy.create_henry.content.jei;

import com.chocoboy.create_henry.content.recipes.WitheringRecipe;
import net.minecraft.world.level.block.Blocks;

public class FanWitheringCategory extends HenryFanProcessingCategory.SimpleBlock<WitheringRecipe> {

    public FanWitheringCategory(Info<WitheringRecipe> info) {
        super(info, Blocks.WITHER_ROSE);
    }

}
