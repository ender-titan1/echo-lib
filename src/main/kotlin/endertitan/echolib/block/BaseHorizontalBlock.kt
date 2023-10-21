package endertitan.echolib.block

import endertitan.echolib.blockentity.IDrop
import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty

open class BaseHorizontalBlock(props: Properties) : BaseEntityBlock(props) {

    companion object {
        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
    }

    // FACING

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        return defaultBlockState().setValue(FACING, ctx.horizontalDirection.opposite)
    }

    override fun rotate(state: BlockState, level: LevelAccessor, pos: BlockPos, direction: Rotation): BlockState {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)))
    }

    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.rotate(mirror.getRotation(state.getValue(FACING)))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(FACING)
    }

    // ENTITY

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return null
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        if (state.block != newState.block) {
            val be: BlockEntity? = level.getBlockEntity(pos)

            if (be is IDrop) {
                be.drop()
            }
        }

        super.onRemove(state, level, pos, newState, isMoving)
    }
}