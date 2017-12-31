package at.aems.apilib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AemsInsertAction extends AbstractAemsAction {

	private transient String tableName;
	private transient List<Map<String, Object>> insertData;
	private transient Map<String, Object> currentMap;
	
	public AemsInsertAction(AemsUser user) {
		super(user, "INSERT");
		insertData = new ArrayList<Map<String,Object>>();
	}
	
	public void setTable(String tableName) {
		this.tableName = tableName;
	}
	
	public void beginWrite() {
		currentMap = new HashMap<String, Object>();
	}
	
	public void write(String columnName, Object value) {
		if(currentMap == null) {
			beginWrite();
		}
		currentMap.put(columnName, value);
	}
	
	public void endWrite() {
		insertData.add(currentMap);
		currentMap = null;
	}

	@Override
	public JsonElement serializeData() {
		
		if(tableName == null) {
			throw new IllegalArgumentException("Table name cannot be null! Set it using #setTable(String name)");
		}
		
		JsonObject data = new JsonObject();
		JsonArray dataContainer = builder.create().toJsonTree(insertData).getAsJsonArray();
		data.add(tableName, dataContainer);
		
		return data;
	}


}
