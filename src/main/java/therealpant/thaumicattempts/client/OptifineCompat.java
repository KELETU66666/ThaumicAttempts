package therealpant.thaumicattempts.client;

import net.minecraft.launchwrapper.Launch;
import therealpant.thaumicattempts.ThaumicAttempts;

/**
 * Defensive toggles for OptiFine. Some OptiFine options (Fast Render) break
 * GeckoLib/TER rendering and lead to crashes. We do not want to ship a hard
 * dependency, so everything is reflection-based and silently skipped when
 * OptiFine is absent.
 */
public final class OptifineCompat {

    private static final String OPTIFINE_CONFIG = "net.optifine.Config";

    private OptifineCompat() {}

    /**
     * If OptiFine is present, disable Fast Render and log what happened. The
     * methods are stable in G5 and earlier builds; reflection keeps us safe if
     * they change.
     */
    public static void ensureSafeSettings() {
        if (Boolean.TRUE.equals(Launch.blackboard.get("fml.deobfuscatedEnvironment"))) {
            return; // Running in dev (SRG) environment — OptiFine obf classes are unavailable.
        }
        try {
            Class<?> config = Class.forName(OPTIFINE_CONFIG);

            boolean fastRender = (boolean) config.getMethod("isFastRender").invoke(null);
            if (fastRender) {
                config.getMethod("setFastRender", boolean.class).invoke(null, false);
                ThaumicAttempts.LOGGER.warn("[OptiFine] Fast Render disabled for ThaumicAttempts rendering compatibility.");
            }
        } catch (ClassNotFoundException e) {
            // OptiFine is not installed — nothing to do.
        } catch (NoClassDefFoundError e) {
            ThaumicAttempts.LOGGER.warn("[OptiFine] OptiFine classes are not available in this environment; skipping compatibility toggles.");
        } catch (ReflectiveOperationException e) {
            ThaumicAttempts.LOGGER.warn("[OptiFine] Failed to adjust OptiFine settings for compatibility.", e);
        }
    }
}