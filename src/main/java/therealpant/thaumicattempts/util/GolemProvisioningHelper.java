package therealpant.thaumicattempts.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.GolemHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Bridges Thaumcraft's {@code GolemHelper#requestProvisioning} API between the
 * 1.12.2 releases that ship either the 4-parameter or the 5-parameter overload.
 */
public final class GolemProvisioningHelper {

    private static final MethodHandle REQUEST_FIVE_INT_ARGS;
    private static final MethodHandle REQUEST_FIVE_BOOL_ARGS;
    private static final MethodHandle REQUEST_FOUR_ARGS;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle fiveInt = null;
        MethodHandle fiveBool = null;
        MethodHandle four = null;
        try {
            fiveInt = lookup.findStatic(
                    GolemHelper.class,
                    "requestProvisioning",
                    MethodType.methodType(boolean.class, World.class, BlockPos.class, EnumFacing.class, ItemStack.class, int.class)
            );
        } catch (NoSuchMethodException | IllegalAccessException ignored) {
        }

        if (fiveInt == null) {
            try {
                fiveBool = lookup.findStatic(
                        GolemHelper.class,
                        "requestProvisioning",
                        MethodType.methodType(boolean.class, World.class, BlockPos.class, EnumFacing.class, ItemStack.class, boolean.class)
                );
            } catch (NoSuchMethodException | IllegalAccessException ignored) {
            }
        }

        if (fiveInt == null && fiveBool == null) {
            try {
                four = lookup.findStatic(
                        GolemHelper.class,
                        "requestProvisioning",
                        MethodType.methodType(boolean.class, World.class, BlockPos.class, EnumFacing.class, ItemStack.class)
                );
            } catch (NoSuchMethodException | IllegalAccessException ignored) {
            }
        }

        if (fiveInt == null && fiveBool == null && four == null) {
            throw new IllegalStateException("Unable to resolve thaumcraft.api.golems.GolemHelper#requestProvisioning");
        }

        REQUEST_FIVE_INT_ARGS = fiveInt;
        REQUEST_FIVE_BOOL_ARGS = fiveBool;
        REQUEST_FOUR_ARGS = four;
    }

    private GolemProvisioningHelper() {
    }

    public static boolean requestProvisioning(World world, BlockPos pos, EnumFacing side, ItemStack stack) {
        return requestProvisioning(world, pos, side, stack, 0);
    }

    public static boolean requestProvisioning(World world, BlockPos pos, EnumFacing side, ItemStack stack, int priority) {
        try {
            if (REQUEST_FIVE_INT_ARGS != null) {
                return (boolean) REQUEST_FIVE_INT_ARGS.invokeExact(world, pos, side, stack, priority);
            }
            if (REQUEST_FIVE_BOOL_ARGS != null) {
                return (boolean) REQUEST_FIVE_BOOL_ARGS.invokeExact(world, pos, side, stack, priority > 0);
            }
            return (boolean) REQUEST_FOUR_ARGS.invokeExact(world, pos, side, stack);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to call Thaumcraft golem provisioning", e);
        }
    }
}