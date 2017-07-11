package com.rytong.emp.unit;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class MGsonBuilder {

	public  Gson createGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeHierarchyAdapter(Integer.class, new IntegerDeserializer());
		gsonBuilder.registerTypeHierarchyAdapter(Double.class,new DoubleDeserializer());
		gsonBuilder.registerTypeHierarchyAdapter(Float.class,new FloatDeserializer());
		return gsonBuilder.create();
	}
	private class IntegerDeserializer implements JsonDeserializer<Integer> {
		@Override
		public Integer deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {

			String string = arg0.getAsString();
			if (string.length() > 0) {
				return Integer.parseInt(string);
			} else {
				return 0;
			}
		}
	}

	private class DoubleDeserializer implements JsonDeserializer<Double> {

		@Override
		public Double deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
			String string = arg0.getAsString();
			if (string.length() > 0) {
				return Double.parseDouble(string);
			} else {
				return 0.0;
			}
		}
	}
	private class FloatDeserializer implements JsonDeserializer<Float> {
		@Override
		public Float deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
			String string = arg0.getAsString();
			if (string.length() > 0) {
				return Float.parseFloat(string);
			} else {
				return 0f;
			}
		}
	}

}
