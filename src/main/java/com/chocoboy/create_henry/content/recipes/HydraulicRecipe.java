package com.chocoboy.create_henry.content.recipes;

import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HydraulicRecipe extends BasinRecipe {
    public HydraulicRecipe(ProcessingRecipeParams params) {
        super(HenryRecipeTypes.HYDRAULIC_COMPACTING, params);
    }
}
