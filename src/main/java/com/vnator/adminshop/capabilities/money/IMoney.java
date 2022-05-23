package com.vnator.adminshop.capabilities.money;

public interface IMoney {

	boolean deposit(float money);
	boolean withdraw(float money);
	boolean canPerformWithdraw(float money);
	float getMoney();
	String getFormattedMoney();
	void setMoney(float money);

}
