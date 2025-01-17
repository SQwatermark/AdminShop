package com.vnator.adminshop.blocks.seller;

import com.vnator.adminshop.blocks.shop.ShopStock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.oredict.OreDictionary;

public class ContainerSeller extends Container {


	public ContainerSeller(InventoryPlayer playerInv, final TileEntitySeller seller){
		IItemHandler inventory = seller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		//Item Selling Slot
		addSlotToContainer(new SlotItemHandler(inventory, 0, 26, 35){
			@Override
			public void onSlotChanged(){
				seller.markDirty();
			}

			@Override
			public boolean isItemValid(ItemStack stack){
				//Check if the item itself is sellable
				String id = stack.getItem().getRegistryName() + ":" + stack.getMetadata();
				boolean itemIn = ShopStock.sellMap.containsKey(id);
//				System.out.println(id);
				if(stack.getTagCompound() != null) {
					id += " " + stack.getTagCompound().toString();
					itemIn = itemIn || ShopStock.sellMap.containsKey(id);
//					System.out.println(id);
				}
				if(itemIn)
					return true;

				//Check if oredict of the item is sellable
				boolean oreIn;
				int [] oreIDs = OreDictionary.getOreIDs(stack);
				for(int i : oreIDs) {
//					System.out.println(i);
					if(ShopStock.sellMap.containsKey(""+i)) {
//						System.out.println("Matches!");
						return true;
					}
					if(stack.hasTagCompound()){
						String checkKey = i+" "+stack.getTagCompound().toString();
//						System.out.println(checkKey);
						if(ShopStock.sellMap.containsKey(checkKey))
							return true;
					}
				}

				//Not a sellable item
				return false;
			}
		});

		//Fluid Selling Slot
		addSlotToContainer(new SlotItemHandler(inventory, 1, 80, 35){
			@Override
			public void onSlotChanged(){seller.markDirty();}

			@Override
			public boolean isItemValid(ItemStack stack){
				FluidStack fluid = FluidUtil.getFluidContained(stack);
				if(fluid != null){
					String name = fluid.getFluid().getName();
					if(ShopStock.sellMap.containsKey(name)) {
//						System.out.println("Matches!");
						return true;
					}
					if(fluid.tag != null)
						name += " "+fluid.tag.toString();
					if(ShopStock.sellMap.containsKey(name))
						return true;
				}
				return false;
			}
		});

		//Power Selling Slot
		addSlotToContainer(new SlotItemHandler(inventory, 2, 134, 35){
			@Override
			public void onSlotChanged(){seller.markDirty();}

			@Override
			public boolean isItemValid(ItemStack stack){
				IEnergyStorage battery = stack.getCapability(CapabilityEnergy.ENERGY, null);
				if(battery != null)
					return true;

				return false;
			}
		});

		/*
		//Item output slot
		addSlotToContainer(new SlotItemHandler(inventory, 3, 134, 15){
			@Override
			public void onSlotChanged(){seller.markDirty();}
			@Override
			public boolean isItemValid(ItemStack stack){return false;}
		});
		//Fluid tank output slot
		addSlotToContainer(new SlotItemHandler(inventory, 4, 134, 15){
			@Override
			public void onSlotChanged(){seller.markDirty();}
			@Override
			public boolean isItemValid(ItemStack stack){return false;}
		});
		//Power container output slot
		addSlotToContainer(new SlotItemHandler(inventory, 5, 134, 15){
			@Override
			public void onSlotChanged(){seller.markDirty();}
			@Override
			public boolean isItemValid(ItemStack stack){return false;}
		});
		*/

		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 9; j++){
				addSlotToContainer(new Slot(playerInv, j + i*9 + 9, 8+j*18, 84+i*18));
			}
		}

		for(int k = 0; k < 9; k++){
			addSlotToContainer(new Slot(playerInv, k, 8+k*18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack()){
			ItemStack itemStack1 = slot.getStack();
			itemstack = itemStack1.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if(index < containerSlots){
				if(!this.mergeItemStack(itemStack1, containerSlots, inventorySlots.size(), true)){
					return ItemStack.EMPTY;
				}
			}else if(!this.mergeItemStack(itemStack1, 0, containerSlots, false)){
				return ItemStack.EMPTY;
			}

			if(itemStack1.getCount() == 0){
				slot.putStack(ItemStack.EMPTY);
			}else{
				slot.onSlotChanged();
			}

			if(itemStack1.getCount() == itemstack.getCount()){
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemStack1);
		}
		return itemstack;
	}
}
