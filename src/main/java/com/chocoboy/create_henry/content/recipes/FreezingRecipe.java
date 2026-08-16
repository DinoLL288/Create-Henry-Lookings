package com.chocoboy.create_henry.content.recipes;

import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FreezingRecipe extends HenryFanProcessingRecipe {

    public FreezingRecipe(ProcessingRecipeParams params) {
        super(HenryRecipeTypes.FREEZING, params);
    }

}
