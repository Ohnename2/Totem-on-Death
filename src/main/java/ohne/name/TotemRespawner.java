package ohne.name;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jspecify.annotations.NonNull;

public class TotemRespawner extends Block {
    public static final BooleanProperty DECAY = BooleanProperty.create("decay");

    TotemRespawner(BlockBehaviour.Properties properties, boolean shouldDecay) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DECAY, shouldDecay));
    }


    @Override
    protected void randomTick(@NonNull BlockState blockState, @NonNull ServerLevel level, @NonNull BlockPos blockPos, RandomSource randomSource) {
        if(randomSource.nextInt(5) == 0) {
            level.destroyBlock(blockPos, true);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DECAY);
    }

    public void decay(@NonNull BlockState blockState, @NonNull ServerLevel level, @NonNull BlockPos blockPos) {
        if(blockState.getValue(DECAY)) {
            level.destroyBlock(blockPos, true);
        }
    }
}
