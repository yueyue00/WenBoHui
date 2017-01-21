package com.gheng.exhibit.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 *  json转换工具
 * @author lileixing
 */
public class JsonUtils {
	
	private static Gson gson;
	private static List<String> exclusions;
	
	private JsonUtils() {
	}
	
	static{
		GsonBuilder builder = new GsonBuilder();
		 builder.registerTypeAdapter(Timestamp.class, new TimestampAdapter());
		 builder.registerTypeAdapter(java.sql.Date.class, new SqlDateAdapter());
		 builder.registerTypeAdapter(java.util.Date.class, new DateAdapter());
		 gson = builder.create();
		 exclusions = new ArrayList<String>();
	}
	
	private static void initExclusions(){
		exclusions.clear();
	}
	
	public static String toJson(Object obj,String...excluFields){
		if(obj == null)
			return "{}";
		if(excluFields.length > 0){
			for (String s : excluFields) {
				exclusions.add(s);
			}
		}
		String result =  gson.toJson(obj);
		initExclusions();
		return result;
	}
	/**
	 * @param obj
	 * @param excuFields
	 * 	剔除的字段
	 * @return
	 */
	public static String toJson(Object obj,List<String> excluFields){
		if(obj == null)
			return "{}";
		if(excluFields != null)
			exclusions.addAll(excluFields);
		String result =  gson.toJson(obj);
		initExclusions();
		return result;
	}
	
	public static <T> T fromJson(String json, Class<T> clazz){
		return gson.fromJson(json, clazz);
	}
	
	
	
	static class JsonExclusionStrategy implements ExclusionStrategy{
		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
		@Override
		public boolean shouldSkipField(FieldAttributes field) {
			return exclusions.contains(field.getName());
		}
		
	}
	static class DateAdapter extends TypeAdapter<java.util.Date> {
		private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		@Override
		public void write(JsonWriter out, java.util.Date value)
				throws IOException {
			out.value(value == null ? null : format.format(value));
		}
		@Override
		public Date read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String s = in.nextString();
			try {
				return format.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	static class TimestampAdapter extends TypeAdapter<Timestamp> {
		private final DateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		@Override
		public void write(JsonWriter out, Timestamp value) throws IOException {
			out.value(value == null ? null : format.format(value));
		}
		@Override
		public Timestamp read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String s = in.nextString();
			try {
				return new java.sql.Timestamp(format.parse(s).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	static class SqlDateAdapter extends TypeAdapter<java.sql.Date> {
		private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		@Override
		public void write(JsonWriter out, java.sql.Date value)
				throws IOException {
			out.value(value == null ? null : format.format(value));
		}
		@Override
		public java.sql.Date read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String s = in.nextString();
			try {
				return new java.sql.Date(format.parse(s).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

	}

}
