package com.skilldistillery.filmquery.entities;

public class InventoryItem {
	
	private int id;
	private int filmId;
	private int storeId;
	private String mediaCondition;
	private String lastUpdate;
	//TODO add film name for readability purposes?
	
	public InventoryItem() {
	}
	
	public InventoryItem(int id, int filmId, int storeId, String mediaCondition, String lastUpdate) {
		super();
		this.id = id;
		this.filmId = filmId;
		this.storeId = storeId;
		this.mediaCondition = mediaCondition;
		this.lastUpdate = lastUpdate;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFilmId() {
		return filmId;
	}
	public void setFilmId(int filmId) {
		this.filmId = filmId;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public String getMediaCondition() {
		return mediaCondition;
	}
	public void setMediaCondition(String mediaCondition) {
		this.mediaCondition = mediaCondition;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "Film ID: " + filmId + ", Inventory ID:" + id + ", Store ID:" + storeId + ", Condition:"
				+ mediaCondition + ", Last Update:" + lastUpdate + "]";
	}

}
