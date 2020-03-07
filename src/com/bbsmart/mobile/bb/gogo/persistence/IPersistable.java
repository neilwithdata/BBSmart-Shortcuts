package com.bbsmart.mobile.bb.gogo.persistence;

public interface IPersistable {

	void loadUsing(IPersistenceProvider provider) throws PersistenceException;
	void storeUsing(IPersistenceProvider provider) throws PersistenceException;

}
