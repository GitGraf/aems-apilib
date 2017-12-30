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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class AbstractAemsAction {
	
	private int userId;
	private String authStr;
	private String action;
	
	protected transient GsonBuilder builder;
	
	public AbstractAemsAction(int user, String auth, String action) {
		this.userId = user;
		this.authStr = auth;
		this.action = action;
		builder = new GsonBuilder().setPrettyPrinting()
				.serializeNulls()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
	}
	
	public String toJson() {
		JsonObject object = builder.create().toJsonTree(this).getAsJsonObject();
		JsonElement data = serializeData();
		object.add("data", data);
		return builder.create().toJson(object);
	}
	
	public abstract JsonElement serializeData();
}
