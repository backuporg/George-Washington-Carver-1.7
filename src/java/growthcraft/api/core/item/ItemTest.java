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
package growthcraft.api.core.item;

import growthcraft.api.core.definition.IMultiItemStacks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTest {
    private ItemTest() {
    }

    public static boolean isValid(@Nonnull ItemStack stack) {
        if (stack == null) return false;
        if (stack.getItem() == null) return false;
        return stack.stackSize > 0;
    }

    public static boolean itemMatches(@Nullable IMultiItemStacks expected, @Nullable ItemStack actual) {
        if (expected == null || expected.isEmpty()) {
            return actual == null;
        } else {
            if (actual != null) {
                return expected.containsItemStack(actual);
            }
        }
        return false;
    }

    public static boolean itemMatches(@Nullable ItemStack expected, @Nullable ItemStack actual) {
        if (expected == null) {
            return actual == null;
        } else {
            if (actual != null) {
                if (expected.getItemDamage() == ItemKey.WILDCARD_VALUE) {
                    return expected.getItem() == actual.getItem();
                } else {
                    return expected.isItemEqual(actual);
                }
            }
        }
        return false;
    }

    public static boolean areStacksEqual(@Nullable IMultiItemStacks expected, @Nullable ItemStack actual) {
        if (!itemMatches(expected, actual)) return false;
        return actual.stackSize == expected.getStackSize();
    }

    public static boolean areStacksEqual(@Nullable ItemStack expected, @Nullable ItemStack actual) {
        if (!itemMatches(expected, actual)) return false;
        return actual.stackSize == expected.stackSize;
    }

    public static boolean hasEnough(@Nullable IMultiItemStacks expected, @Nullable ItemStack actual) {
        if (!itemMatches(expected, actual)) return false;
        return actual.stackSize >= expected.getStackSize();
    }

    public static boolean hasEnough(@Nullable ItemStack expected, @Nullable ItemStack actual) {
        if (!itemMatches(expected, actual)) return false;
        return actual.stackSize >= expected.stackSize;
    }

    @SuppressWarnings({"rawtypes"})
    public static boolean isValidAndExpected(@Nonnull List expectedItems, @Nonnull List<ItemStack> givenItems) {
        if (expectedItems.size() != givenItems.size()) return false;
        for (int i = 0; i < expectedItems.size(); ++i) {
            final Object expected = expectedItems.get(i);
            final ItemStack actual = givenItems.get(i);
            if (expected != null) {
                if (!isValid(actual)) return false;
                if (expected instanceof ItemStack) {
                    if (!((ItemStack) expected).isItemEqual(actual)) return false;
                } else if (expected instanceof IMultiItemStacks) {
                    if (!((IMultiItemStacks) expected).containsItemStack(actual)) return false;
                } else {
                    return false;
                }
            } else {
                if (actual != null) return false;
            }
        }
        return true;
    }

    @SuppressWarnings({"rawtypes"})
    public static boolean containsExpectedItemsUnordered(@Nonnull List expectedItems, @Nonnull List<ItemStack> givenItems) {
        final boolean[] usedSlots = new boolean[givenItems.size()];
        for (Object expectedStack : expectedItems) {
            boolean found = false;
            for (int index = 0; index < usedSlots.length; ++index) {
                if (usedSlots[index]) continue;
                if (expectedStack instanceof IMultiItemStacks) {
                    found = itemMatches((IMultiItemStacks) expectedStack, givenItems.get(index));
                } else if (expectedStack instanceof ItemStack) {
                    found = itemMatches((ItemStack) expectedStack, givenItems.get(index));
                } else if (expectedStack == null) {
                    found = givenItems.get(index) == null;
                }

                if (found) {
                    usedSlots[index] = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }
}
