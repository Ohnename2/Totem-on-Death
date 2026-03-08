package ohne.name.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Block.class)
public class ForceToRespawn {
    /**
     * @author Totem-on-Death
     * @reason Allow respawn everywhere
     */
    @Overwrite
    public boolean isPossibleToRespawnInThis(BlockState state) {
        return true;
    }
}
