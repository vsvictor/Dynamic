package com.education.dynamic.model

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.reflect.Type

data class DynamicItem(val name : String, var function : String, var param : String){}
class DynamicList : ArrayList<DynamicItem>()

class ItemParser : JsonDeserializer<DynamicItem>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DynamicItem {
        var name = "Undefined"
        try{
            name = json?.asJsonObject?.get("name")?.asString?:"Undefined"
        }catch (ex : Exception){
            name = "Undefined"
        }
        var function = "Undefined"
        try{
            function= json?.asJsonObject?.get("function")?.asString?:"Undefined"
        }catch (ex : Exception){
            function = "Undefined"
        }
        var param = "Undefined"
        try{
            param= json?.asJsonObject?.get("param")?.asString?:"Undefined"
        }catch (ex : Exception){
            param = "Undefined"
        }
        return DynamicItem(name, function, param)
    }
}

class ListParser : JsonDeserializer<DynamicList>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DynamicList {
        val result = DynamicList()
        val list = json?.asJsonObject?.get("menu")?.asJsonArray?: JsonArray()
        for(item in list){
            val i = ItemParser()
                .deserialize(item, ItemParser::class.java, context)
            result.add(i)
        }
        return result
    }

}