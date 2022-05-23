package com.vnator.adminshop.capabilities.ledger;

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public interface ILedger {

	//Capabilities shared with IMoney
	boolean deposit(String username, float amount);
	boolean withdraw(String username, float amount);
	boolean canPerformWithdraw(String username, float money);
	float getMoney(String username);
	void setMoney(String username, float money);

	//New capabilities
	void addPlayer(String username);
	HashMap<String, Float> getMap();
	void loadFromNBT(NBTTagCompound tag);

}
