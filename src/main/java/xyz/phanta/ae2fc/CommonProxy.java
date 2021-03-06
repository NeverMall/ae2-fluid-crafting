package xyz.phanta.ae2fc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import xyz.phanta.ae2fc.constant.NameConst;
import xyz.phanta.ae2fc.handler.RegistryHandler;
import xyz.phanta.ae2fc.init.FcBlocks;
import xyz.phanta.ae2fc.init.FcItems;
import xyz.phanta.ae2fc.inventory.InventoryHandler;
import xyz.phanta.ae2fc.network.CPacketDumpTank;
import xyz.phanta.ae2fc.network.CPacketEncodePattern;
import xyz.phanta.ae2fc.network.CPacketLoadPattern;
import xyz.phanta.ae2fc.tile.TileFluidDiscretizer;
import xyz.phanta.ae2fc.tile.TileFluidPacketDecoder;
import xyz.phanta.ae2fc.tile.TileFluidPatternEncoder;
import xyz.phanta.ae2fc.tile.TileIngredientBuffer;

public class CommonProxy {

    private final RegistryHandler regHandler = createRegistryHandler();
    private final SimpleNetworkWrapper netHandler = NetworkRegistry.INSTANCE.newSimpleChannel(Ae2FluidCrafting.MOD_ID);

    protected RegistryHandler createRegistryHandler() {
        return new RegistryHandler();
    }

    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(regHandler);
        FcBlocks.init(regHandler);
        FcItems.init(regHandler);
        GameRegistry.registerTileEntity(TileFluidDiscretizer.class, Ae2FluidCrafting.resource(NameConst.BLOCK_FLUID_DISCRETIZER));
        GameRegistry.registerTileEntity(TileFluidPatternEncoder.class, Ae2FluidCrafting.resource(NameConst.BLOCK_FLUID_PATTERN_ENCODER));
        GameRegistry.registerTileEntity(TileFluidPacketDecoder.class, Ae2FluidCrafting.resource(NameConst.BLOCK_FLUID_PACKET_DECODER));
        GameRegistry.registerTileEntity(TileIngredientBuffer.class, Ae2FluidCrafting.resource(NameConst.BLOCK_INGREDIENT_BUFFER));
        netHandler.registerMessage(new CPacketEncodePattern.Handler(), CPacketEncodePattern.class, 0, Side.SERVER);
        netHandler.registerMessage(new CPacketLoadPattern.Handler(), CPacketLoadPattern.class, 1, Side.SERVER);
        netHandler.registerMessage(new CPacketDumpTank.Handler(), CPacketDumpTank.class, 2, Side.SERVER);
    }

    public void onInit(FMLInitializationEvent event) {
        // NO-OP
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Ae2FluidCrafting.INSTANCE, new InventoryHandler());
    }

    public SimpleNetworkWrapper getNetHandler() {
        return netHandler;
    }

}
