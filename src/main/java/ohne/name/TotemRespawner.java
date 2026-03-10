package ohne.name;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class TotemRespawner extends Block {
    TotemRespawner(BlockBehaviour.Properties properties) {
        super(properties);
    }
    @Override
    protected void randomTick(@NonNull BlockState blockState, @NonNull ServerLevel level, @NonNull BlockPos blockPos, RandomSource randomSource) {
        if(randomSource.nextInt(5) == 0) {
            level.destroyBlock(blockPos, true);
        }
    }
}
