# aems-apilib
This library is used to generate the JSON-Body that will be sent to the AEMS-API. Note that this library does not (yet)
support the actual API communication. 

# Usage
This library provides different classes for each operation: `QUERY`, `INSERT` and `UPDATE`. To use one of them, you need to instantiate the corresponding class and set certain fields. When you are done, you can generate the JSON string using `myInstance.toJson()`. 

## Query
```java
AemsQueryAction query = new AemsQueryAction(userId, authString);
query.setQuery("INSERT GRAPH-QL QUERY HERE");
String json = query.toJson();
```
This code example would produce the following JSON output:
```json
{
  "user_id": 10,
  "auth_str": "auth",
  "action": "QUERY",
  "data": "INSERT GRAPH-QL QUERY HERE"
}
```

## Insert
```java
AemsInsertAction insert = new AemsInsertAction(userId, authString);
insert.setTable("Notifications"); // In which table are we inserting?
// Insert multiple records
for(NotificationData data : notifications) {
	insert.beginWrite(); // Let's write a new record
	insert.write("name", data.getName()); // write(columnName, value)
	insert.write("derivation", data.getDerivation());
	insert.write("annotation", data.getAnnotation());
	insert.endWrite(); // End of current record
}
String json = insert.toJson();
```
This code example could produce the following JSON output:
```json
{
  "user_id": 10,
  "auth_str": "auth",
  "action": "INSERT",
  "data": {
    "Notifications": [
      {
        "annotation": "Stromverbrauch weicht 20% ab",
        "name": "Stromwarnung",
        "derivation": 20.0
      },
      {
        "annotation": "Anomalien beim Wasserzähler!",
        "name": "Wasserzähler",
        "derivation": 50.0
      }
    ]
  }
}
```

## Update
```java
AemsUpdateAction update = new AemsUpdateAction(userId, authString);
update.setTable("Meters"); 		// Which table are we updating?
update.setIdColumn("id", "AT000001"); 	// Which column and value should be used for the WHERE statement?
		
update.write("type", 3); 		// UPDATE the meter type column, set it to 3 (whatever that may be)
		
String json = update.toJson();
```
This code example would produce the following JSON output:
```json
{
  "user_id": 10,
  "auth_str": "auth",
  "action": "UPDATE",
  "data": {
    "id": "AT000001",
    "Meters": [
      {
        "type": 3
      }
    ]
  }
}
```
