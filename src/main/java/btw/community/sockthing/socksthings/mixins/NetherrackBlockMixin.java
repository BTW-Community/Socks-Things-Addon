package btw.community.sockthing.socksthings.mixins;

import btw.block.BTWBlocks;
import btw.block.blocks.FullBlock;
import btw.block.blocks.NetherrackBlock;
import btw.client.fx.BTWEffectManager;
import btw.community.sockthing.socksthings.SocksThingsAddon;
import btw.item.BTWItems;
import btw.item.items.ChiselItem;
import btw.item.items.PickaxeItem;
import btw.item.items.ToolItem;
import btw.item.util.ItemUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.Random;

@Mixin(NetherrackBlock.class)
public abstract class NetherrackBlockMixin extends FullBlock {

	public NetherrackBlockMixin(int iBlockID) {
		super( iBlockID, BTWBlocks.netherRockMaterial);
	}

	@Override
	public void onBlockAdded( World world, int x, int y, int z )
	{
		if (!world.provider.isHellWorld)
		{
			// "2" in last param to not trigger another neighbor block notify
			if (world.getBlockMetadata(x, y, z) == 1) {
				world.setBlock(x, y, z, SocksThingsAddon.fallingNetherrackGold.blockID, 0, 2);
			}
			else world.setBlock(x, y, z, BTWBlocks.fallingNetherrack.blockID, 0, 2);
		}
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		if (world.getBlockMetadata(x, y, z) == 1) return 2.5F;
		return super.getBlockHardness(world, x, y, z);
	}

	@Override
	public void getSubBlocks(int id, CreativeTabs creativeTabs, List list)
	{
		list.add(new ItemStack(id, 1, 0));
		list.add(new ItemStack(id, 1, 1));
	}

	@Override
	public int idDropped(int meta, Random random, int par3)
	{
		if (meta == 1) {
			return BTWItems.goldOreChunk.itemID;
		}
		else return super.idDropped(meta, random, par3);
	}

	@Override
	public int getEfficientToolLevel(IBlockAccess blockAccess, int x, int y, int z)
	{
		if (blockAccess.getBlockMetadata(x, y, z) == 1)	{
			return getRequiredToolLevelForOre(blockAccess, x, y, z);
		}
		else return super.getEfficientToolLevel(blockAccess, x, y, z);
	}

	public int getRequiredToolLevelForOre(IBlockAccess blockAccess, int x, int y, int z)
	{
		return 2;
	}

	@Override
	public boolean canConvertBlock(ItemStack stack, World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) == 1;
	}

	@Override
	public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide)
	{
		int iOldMetadata = world.getBlockMetadata( i, j, k );

		world.setBlockAndMetadataWithNotify(i, j, k, Block.netherrack.blockID, 0);

		if ( !world.isRemote )
		{
			int iLevel = getConversionLevelForTool(stack, world, i, j, k);

			if ( iLevel > 0 )
			{
				world.playAuxSFX( BTWEffectManager.STONE_RIPPED_OFF_EFFECT_ID, i, j, k, 0 );

				if ( iLevel >= 3 )
				{
					ejectItemsOnGoodPickConversion(stack, world, i, j, k, iOldMetadata, iFromSide);
				}
				else if ( iLevel == 2 )
				{
					ejectItemsOnStonePickConversion(stack, world, i, j, k, iOldMetadata, iFromSide);
				}
				else
				{
					ejectItemsOnChiselConversion(stack, world, i, j, k, iOldMetadata, iFromSide);
				}
			}
		}

		return true;
	}

	/**
	 * Returns 1, 2, or 3 depending on the level of the conversion tool.  0 if it can't convert
	 */
	private int getConversionLevelForTool(ItemStack stack, World world, int i, int j, int k)
	{
		if ( stack != null )
		{
			if ( stack.getItem() instanceof PickaxeItem)
			{
				int iToolLevel = ((ToolItem)stack.getItem()).toolMaterial.getHarvestLevel();

				if (iToolLevel >= getEfficientToolLevel(world, i, j, k) )
				{
					if ( iToolLevel > 1 )
					{
						return 3;
					}

					return 2;
				}
			}
			else if ( stack.getItem() instanceof ChiselItem)
			{
				int iToolLevel = ((ToolItem)stack.getItem()).toolMaterial.getHarvestLevel();

				if (iToolLevel >= getEfficientToolLevel(world, i, j, k) )
				{
					return 1;
				}
			}
		}

		return 0;
	}

	protected void ejectItemsOnGoodPickConversion(ItemStack stack, World world, int i, int j, int k, int iOldMetadata, int iFromSide)
	{
		ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k,
				new ItemStack( idDropped( iOldMetadata, world.rand, 0 ),
						quantityDropped( world.rand ),
						damageDropped( iOldMetadata ) ), iFromSide);
	}

	protected void ejectItemsOnStonePickConversion(ItemStack stack, World world, int i, int j, int k, int iOldMetadata, int iFromSide)
	{
//		ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k,
//				new ItemStack(idDroppedOnStonePickConversion(iOldMetadata, world.rand, 0),
//						quantityDroppedOnStonePickConversion(world.rand),
//						damageDroppedOnStonePickConversion(iOldMetadata) ), iFromSide);
	}

	protected void ejectItemsOnChiselConversion(ItemStack stack, World world, int i, int j, int k, int iOldMetadata, int iFromSide)
	{
		ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k,
				new ItemStack(idDroppedOnConversion(iOldMetadata),
						quantityDroppedOnConversion(world.rand),
						damageDroppedOnConversion(iOldMetadata) ), iFromSide);
	}

	public int idDroppedOnConversion(int iMetadata)
	{
		return BTWItems.goldOrePile.itemID;
	}

	public int quantityDroppedOnConversion(Random rand)
	{
		return 1;
	}

	public int damageDroppedOnConversion(int iMetadata)
	{
		return 0;
	}

	@Environment(EnvType.CLIENT)
	private Icon gold_ore;

	@Override
	@Environment(EnvType.CLIENT)
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);
		gold_ore = register.registerIcon("STABlockNetherGoldOre");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Icon getIcon(int side, int meta) {
		if (meta == 1) {
			return gold_ore;
		}
		else return super.getIcon(side, meta);
	}
}
