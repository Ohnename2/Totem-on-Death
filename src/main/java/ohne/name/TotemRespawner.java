package ohne.name;


import net.minecraft.core.BlockPos;
import net.minecraft.server.dedicated.Settings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;

import java.util.Random;
import java.util.random.RandomGenerator;

public class TotemRespawner extends Block {
    TotemRespawner(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        if(randomSource.nextInt(5) == 0) {}
        level.destroyBlock(blockPos, true);
    }
}
