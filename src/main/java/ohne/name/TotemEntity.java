package ohne.name;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class TotemEntity extends ItemEntity {
    boolean isImmuneToExplosion = true;

    public TotemEntity(EntityType<? extends ItemEntity> entityType, Level world) {
        super(entityType, world);
    }

    public TotemEntity(Level world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
    }

    public TotemEntity(Level world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, stack, velocityX, velocityY, velocityZ);
    }

    @Override
    public boolean ignoreExplosion(@NonNull Explosion explosion) {
        return this.isImmuneToExplosion;
    }
}
