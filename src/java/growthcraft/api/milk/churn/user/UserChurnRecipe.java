/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.api.milk.churn.user;

import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ICommentable;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.milk.churn.ChurnRecipe;
import growthcraft.api.milk.churn.IChurnRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class UserChurnRecipe implements ICommentable
{
	public String comment = "";
	public FluidStackSchema input_fluid;
	public FluidStackSchema output_fluid;
	public ItemKeySchema output_item;
	public int churns;

	public UserChurnRecipe(FluidStackSchema pInputFluid, FluidStackSchema pOutputFluid, ItemKeySchema pOutputItem, int pChurns)
	{
		this.output_item = pOutputItem;
		this.input_fluid = pInputFluid;
		this.output_fluid = pOutputFluid;
		this.churns = pChurns;
	}

	public UserChurnRecipe() {}

	@Override
	public String toString()
	{
		return String.format("UserChurnRecipe(`%s` / %d = `%s` & `%s`)", input_fluid, churns, output_fluid, output_item);
	}

	@Override
	public void setComment(String comm)
	{
		this.comment = comm;
	}

	@Override
	public String getComment()
	{
		return comment;
	}

	public List<IChurnRecipe> toChurnRecipes()
	{
		final List<IChurnRecipe> recipes = new ArrayList<IChurnRecipe>();
		final FluidStack inputFluidStack = input_fluid.asFluidStack();
		final FluidStack outputFluidStack = output_fluid != null ? output_fluid.asFluidStack() : null;

		if (output_item != null && output_item.isValid())
		{
			for (ItemStack stack : output_item.getItemStacks())
			{
				recipes.add(new ChurnRecipe(inputFluidStack, outputFluidStack, stack, churns));
				// only the first item is used, everything is dropped.
				break;
			}
		}
		else
		{
			recipes.add(new ChurnRecipe(inputFluidStack, outputFluidStack, null, churns));
		}
		return recipes;
	}
}
