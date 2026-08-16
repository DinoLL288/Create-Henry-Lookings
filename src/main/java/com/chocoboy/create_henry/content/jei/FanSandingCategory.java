package com.chocoboy.create_henry.content.jei;

import com.chocoboy.create_henry.content.recipes.SandingRecipe;
import net.minecraft.world.level.block.Blocks;

public class FanSandingCategory extends HenryFanProcessingCategory.SimpleBlock<SandingRecipe> {

    public FanSandingCategory(Info<SandingRecipe> info) {
        super(info, Blocks.SAND);
    }

}
