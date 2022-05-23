package com.vnator.adminshop;

import com.vnator.adminshop.items.CheckItem;
import com.vnator.adminshop.items.ItemBase;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

	public static final List<ItemBase> itemsList = new ArrayList<ItemBase>();

	//public static FirstItem firstItem = new FirstItem();
	//public static IngotCopper ingotCopper = new IngotCopper();
	public static CheckItem check = new CheckItem();

	public static void registerItems(RegistryEvent.Register<Item> event){
		event.getRegistry().registerAll(itemsList.toArray(new Item[0]));
	}

	public static void registerModels(){
		System.out.println("Number of Items: "+itemsList.size());
		for(ItemBase i : itemsList){
			i.registerItemModel();
		}
	}
}
