/**
  Copyright 2017 Niklas Graf
	
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package at.aems.apilib;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import at.aems.apilib.crypto.EncryptionType;

public class AemsUpdateAction extends AbstractAemsAction {

	private transient String tableName;
	private transient String idColumn;
	private transient Object idValue;
	private transient Map<String, Object> updateData;
	
	public AemsUpdateAction(AemsUser user, EncryptionType encryption) {
		super(user, "UPDATE", encryption);
		updateData = new HashMap<String, Object>();
	}
	
	public void setTable(String tableName) {
		this.tableName = tableName;
	}
	
	public void setIdColumn(String columnName, Object value) {
		this.idColumn = columnName;
		this.idValue = value;
	}
	
	public void write(String columnName, Object value) {
		updateData.put(columnName, value);
	}

	@Override
	public JsonElement serializeData() {
		
		if(idColumn == null || tableName == null) {
			throw new IllegalArgumentException("Please specify a table name and an identifier column name!");
		}
		
		JsonObject data = new JsonObject();
		if(idValue instanceof Number) {
			data.addProperty(idColumn, (Number) idValue);
		} else if(idValue instanceof Boolean) {
			data.addProperty(idColumn, (Boolean) idValue);
		} else {
			data.addProperty(idColumn, (String) idValue);
		}
		
		JsonArray tableData = new JsonArray();
		tableData.add(builder.create().toJsonTree(updateData));
		
		data.add(tableName, tableData);
		
		return data;
	}

	@Override
	public String getHttpVerb() {
		return "PUT";
	}

}
