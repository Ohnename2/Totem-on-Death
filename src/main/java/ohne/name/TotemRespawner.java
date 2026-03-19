package ohne.name;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jspecify.annotations.NonNull;

public class TotemRespawner extends Block {
    public static final BooleanProperty DECAY = BooleanProperty.create("decay");
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 5);

    TotemRespawner(BlockBehaviour.Properties properties, boolean shouldDecay) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DECAY, shouldDecay).setValue(AGE, 0));
    }

    @Override
    protected void randomTick(@NonNull BlockState blockState, @NonNull ServerLevel level, @NonNull BlockPos blockPos, RandomSource randomSource) {
        int age = blockState.getValue(AGE);
        if(randomSource.nextInt(3) == 0) {
            if(age < 5) {
                level.setBlock(blockPos, blockState.setValue(AGE,  age + 1), Block.UPDATE_ALL);
                return;
            }
            level.destroyBlock(blockPos, true);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DECAY);
        builder.add(AGE);
    }

    public void decay(@NonNull BlockState blockState, @NonNull ServerLevel level, @NonNull BlockPos blockPos) {
        if(blockState.getValue(DECAY)) {
            level.destroyBlock(blockPos, true);
        }
    }
}
