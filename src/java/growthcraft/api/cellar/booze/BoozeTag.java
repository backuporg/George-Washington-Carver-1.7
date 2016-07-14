/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.api.cellar.booze;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.fluids.FluidTag;

public class BoozeTag
{
	// the booze is young and probably has no effects
	public static final FluidTag YOUNG = CoreRegistry.instance().fluidTags().createTag("young");
	// the booze is fermented
	public static final FluidTag FERMENTED = CoreRegistry.instance().fluidTags().createTag("fermented");
	// the booze has its effect time extended
	public static final FluidTag EXTENDED = CoreRegistry.instance().fluidTags().createTag("extended");
	// the booze has a stronger effect, but limited time
	public static final FluidTag POTENT = CoreRegistry.instance().fluidTags().createTag("potent");
	// the booze has the extended + potent effect
	public static final FluidTag HYPER_EXTENDED = CoreRegistry.instance().fluidTags().createTag("hyper_extended");
	// the booze WILL kill the player on overdose
	public static final FluidTag DEADLY = CoreRegistry.instance().fluidTags().createTag("deadly");
	// the booze is poisoned, most likely by using netherrash
	public static final FluidTag POISONED = CoreRegistry.instance().fluidTags().createTag("poisoned");
	// the booze was fermented using Lager yeast
	public static final FluidTag CHILLED = CoreRegistry.instance().fluidTags().createTag("chilled");
	// the booze is heavily intoxicating
	public static final FluidTag INTOXICATED = CoreRegistry.instance().fluidTags().createTag("intoxicated");
	// the booze has a base in magic
	public static final FluidTag MAGICAL = CoreRegistry.instance().fluidTags().createTag("magical");
	// the booze has been reinforced, (a stronger version of potent)
	public static final FluidTag FORTIFIED = CoreRegistry.instance().fluidTags().createTag("fortified");
	// the booze has been brewed with hops
	public static final FluidTag HOPPED = CoreRegistry.instance().fluidTags().createTag("hopped");
	// the booze is some form of wine
	public static final FluidTag WINE = CoreRegistry.instance().fluidTags().createTag("wine");
	// the booze is some form of cider
	public static final FluidTag CIDER = CoreRegistry.instance().fluidTags().createTag("cider");
	//This beverage was distilled
	public static final FluidTag DISTILLED = CoreRegistry.instance().fluidTags().createTag("distilled");
	//This beverage has been filtered
	public static final FluidTag FILTERED = CoreRegistry.instance().fluidTags().createTag("filtered");
	//This beverage has been flavored with spices
	public static final FluidTag SPICED = CoreRegistry.instance().fluidTags().createTag("spiced");
	//This beverage has been flavored with fruit
	public static final FluidTag FRUITY = CoreRegistry.instance().fluidTags().createTag("fruity");
	//This beverage is ancient, and of questionable safety
	public static final FluidTag ANCIENT = CoreRegistry.instance().fluidTags().createTag("ancient");
	//This beverage is a cocktail
	public static final FluidTag COCKTAIL = CoreRegistry.instance().fluidTags().createTag("cocktail");
	//This beverage is brewed from nether-based ingredients
	public static final FluidTag INFERNAL = CoreRegistry.instance().fluidTags().createTag("infernal");
	//This beverage is brewed from end-based ingredients
	public static final FluidTag ELDRITCH = CoreRegistry.instance().fluidTags().createTag("eldritch");
	//This beverage is believed to have medicinal properties
	public static final FluidTag MEDICINAL = CoreRegistry.instance().fluidTags().createTag("medicinal");
	//This beverage is made from malted grains
	public static final FluidTag MALTED = CoreRegistry.instance().fluidTags().createTag("malted");
	//This beverage is made from fermented tea
	public static final FluidTag KOMBUCHA = CoreRegistry.instance().fluidTags().createTag("kombucha");
	//This beverage is made from distilling coconuts
	public static final FluidTag ARRACK = CoreRegistry.instance().fluidTags().createTag("arrack");
	//This beverage is made from molasses
	public static final FluidTag RUM = CoreRegistry.instance().fluidTags().createTag("rum");
	//This beverage is made from fermenting palm nectar
	public static final FluidTag NECTAR = CoreRegistry.instance().fluidTags().createTag("nectar");
	//This beverage is made from fermenting agave
	public static final FluidTag PULQUE = CoreRegistry.instance().fluidTags().createTag("pulque");
	//This beverage is whiskey
	public static final FluidTag WHISKEY = CoreRegistry.instance().fluidTags().createTag("whiskey");
	//This beverage is vodka
	public static final FluidTag VODKA = CoreRegistry.instance().fluidTags().createTag("vodka");
	//This beverage is disgusting
	public static final FluidTag VILE = CoreRegistry.instance().fluidTags().createTag("vile");
	//This beverage is gin
	public static final FluidTag GIN = CoreRegistry.instance().fluidTags().createTag("gin");
	//This beverage is beer
	public static final FluidTag BEER = CoreRegistry.instance().fluidTags().createTag("beer");
	//This beverage is bourbon
	public static final FluidTag BOURBON = CoreRegistry.instance().fluidTags().createTag("bourbon");
	//This beverage is sherry
	public static final FluidTag SHERRY = CoreRegistry.instance().fluidTags().createTag("sherry");
	//This beverage is port
	public static final FluidTag PORT = CoreRegistry.instance().fluidTags().createTag("port");

	private BoozeTag() {}
}
