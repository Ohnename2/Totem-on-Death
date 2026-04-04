package ohne.name;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.EnumSet;

public class TotemEntity extends ItemEntity {

    public TotemEntity(EntityType<? extends ItemEntity> entityType, Level world, Component displayname) {
        super(entityType, world);
        this.setAttributes(displayname);
    }

    public TotemEntity(Level world, double x, double y, double z, ItemStack stack, Component displayname) {
        super(world, x, y, z, stack);
        this.setAttributes(displayname);
    }

    public TotemEntity(Level world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ, Component displayname) {
        super(world, x, y, z, stack, velocityX, velocityY, velocityZ);
        this.setAttributes(displayname);
    }

    private void setAttributes(Component displayname) {
        this.setNeverPickUp();
        this.setNoGravity(true);
        this.setUnlimitedLifetime();
        this.setGlowingTag(true);
        this.setInvulnerable(true);
        this.canUsePortal(false);
        this.setCustomName(displayname);
        this.setCustomNameVisible(true);
        this.setPortalCooldown(20000);
    }

    @Override
    public boolean ignoreExplosion(@NonNull Explosion explosion) {
        return true;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public void saveWithoutId(final ValueOutput output) {
    }

    public void totemTick(ServerLevel level, Vec3 targetPos, float YRot, float XRot) {
        this.setPortalCooldown(20000);
        this.teleportTo(level, targetPos.x, targetPos.y, targetPos.z,  EnumSet.noneOf(Relative.class) , YRot, XRot, false);
        this.setDeltaMovement(Vec3.ZERO);
        this.needsSync = true;
    }
}
