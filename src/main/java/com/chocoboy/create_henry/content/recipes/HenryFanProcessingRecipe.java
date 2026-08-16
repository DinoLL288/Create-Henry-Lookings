package com.chocoboy.create_henry.content.recipes;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class HenryFanProcessingRecipe extends StandardProcessingRecipe<HenryFanProcessingRecipe.Wrapper> {

    protected HenryFanProcessingRecipe(IRecipeTypeInfo recipeType, ProcessingRecipeParams params) {
        super(recipeType, params);
    }

    @Override
    public boolean matches(Wrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0).test(inv.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 12;
    }

    public static class Wrapper extends RecipeWrapper {
        private final ItemStackHandler handler;

        public Wrapper() {
            super(new ItemStackHandler(1));
            this.handler = (ItemStackHandler) inv;
        }

        public boolean isEmpty() {
            return handler.getStackInSlot(0).isEmpty();
        }

        public void setItem(int slot, ItemStack stack) {
            handler.setStackInSlot(slot, stack);
        }
    }
}
