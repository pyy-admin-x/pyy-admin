package com.thtf.generate.server.utils;

/**
 * 数据类型工具类
 * @author pyy
 */
public class DataTypeUtils {

	public static final java.lang.String String = "String";
	public static final java.lang.String Byte = "Byte";
	public static final java.lang.String ByteArray = "byte[]";
	public static final java.lang.String Short = "Short";
	public static final java.lang.String Integer = "Integer";
	public static final java.lang.String Long = "Long";
	public static final java.lang.String Float = "Float";
	public static final java.lang.String Double = "Double";
	public static final java.lang.String Character = "Character";
	public static final java.lang.String Boolean = "Boolean";
	public static final java.lang.String Date = "java.util.Date";
	public static final java.lang.String Time = "java.sql.Time";
	public static final java.lang.String Timestamp = "java.sql.Timestamp";
	public static final java.lang.String Object = "java.lang.Object";

	public static final java.lang.String CHAR = "CHAR";
	public static final java.lang.String TEXT = "TEXT";
	public static final java.lang.String BLOB = "BLOB";
	public static final java.lang.String CLOB = "CLOB";
	public static final java.lang.String BIT = "BIT";
	public static final java.lang.String TINYINT = "TINYINT";
	public static final java.lang.String SMALLINT = "SMALLINT";
	public static final java.lang.String INT = "INT";
	public static final java.lang.String INT8 = "INT8";
	public static final java.lang.String BIGINT = "BIGINT";
	public static final java.lang.String LONG = "LONG";
	public static final java.lang.String FLOAT = "FLOAT";
	public static final java.lang.String NUMBER = "NUMBER";
	public static final java.lang.String NUMERIC = "NUMERIC";
	public static final java.lang.String DECIMAL = "DECIMAL";
	public static final java.lang.String DOUBLE = "DOUBLE";
	public static final java.lang.String BOOL = "BOOL";
	public static final java.lang.String BOOLEAN = "BOOLEAN";
	public static final java.lang.String DATE = "DATE";
	public static final java.lang.String TIME = "TIME";
	public static final java.lang.String BINARY = "BINARY";
	public static final java.lang.String VARBINARY = "VARBINARY";
	public static final java.lang.String CHAR_FOR_BIT_DATA = "CHAR FOR BIT DATA";
	public static final java.lang.String RAW = "RAW";
	public static final java.lang.String IMAGE = "IMAGE";
	public static final java.lang.String BYTE = "BYTE";
	public static final java.lang.String DK_CM_BLOB = "DK_CM_BLOB";
	public static final java.lang.String DK_CM_SMALLINT = "DK_CM_SMALLINT";
	public static final java.lang.String OBJECT = "OBJECT";
	public static final java.lang.String LONGVARCHAR = "LONGVARCHAR";
	public static final java.lang.String INTEGER = "INTEGER";
	public static final java.lang.String TIMESTAMP = "TIMESTAMP";
	public static final java.lang.String VARCHAR = "VARCHAR";
	  
	/**
	 * 根据数据库类型获取对应的JAVA类型
	 * @param dataType
	 * @return
	 */
	public static java.lang.String getJavaType(java.lang.String dataType) {
		java.lang.String javaType = String;
		if (dataType != null) {
			dataType = dataType.toUpperCase();
			if (dataType.contains(BINARY) || dataType.contains(CHAR_FOR_BIT_DATA) || dataType.contains(RAW)
					|| dataType.contains(IMAGE) || dataType.contains(BYTE) || dataType.contains(DK_CM_BLOB)
					|| dataType.contains(BLOB)) {
				javaType = ByteArray;
			} else if (dataType.contains(BOOLEAN) || dataType.contains(BIT) || dataType.contains(DK_CM_SMALLINT)) {
				javaType = Boolean;
			} else if (dataType.contains(CHAR) || dataType.contains(TEXT) || dataType.contains(CLOB)) {
				javaType = String;
			} else if (dataType.contains(LONG) || dataType.contains(BIGINT) || dataType.contains(INT8)) {
				javaType = Long;
			} else if (dataType.contains(INT)) {
				javaType = Integer;
			}  else if (dataType.contains(FLOAT)) {
				javaType = Float;
			} else if (dataType.contains(NUMBER) || dataType.contains(DECIMAL) || dataType.contains(DOUBLE)) {
				javaType = Double;
			} else if (dataType.contains(DATE) || dataType.contains(TIME)) {
				javaType = Date;
			} else if (dataType.contains(OBJECT)) {
				javaType = Object;
			}
		}
		return javaType;
	}
	
	/**
	 * 根据数据库类型获取对应JdbcType
	 * @param dataType
	 * @return
	 */
	public static java.lang.String getJdbcType(java.lang.String dataType) {
		java.lang.String jdbcType = dataType.toUpperCase();
		if (dataType.contains(BINARY) || dataType.contains(CHAR_FOR_BIT_DATA) || dataType.contains(RAW)
				|| dataType.contains(IMAGE) || dataType.contains(BYTE) || dataType.contains(DK_CM_BLOB)) {
			jdbcType = VARBINARY;
		} else if (dataType.contains(BLOB)) {
			jdbcType = BLOB;
		} else if (dataType.contains(CLOB)) {
			jdbcType = CLOB;
		} else if (dataType.contains(BOOLEAN)) {
			jdbcType = BOOLEAN;
		} else if (dataType.contains(BIT) || dataType.contains(DK_CM_SMALLINT)) {
			jdbcType = BIT;
		} else if (jdbcType.equalsIgnoreCase(TINYINT)) {
			jdbcType = TINYINT;
		} else if (jdbcType.equalsIgnoreCase(SMALLINT)) {
			jdbcType = SMALLINT;
		} else if (jdbcType.equalsIgnoreCase(INT) || jdbcType.contains(INTEGER)) {
			jdbcType = INTEGER;
		} else if (jdbcType.equalsIgnoreCase(DATE)) {
			jdbcType = DATE;
		} else if (jdbcType.equalsIgnoreCase(TIME)) {
			jdbcType = TIME;
		} else if(jdbcType.contains(DATE) || jdbcType.contains(TIME)) {
			jdbcType = TIMESTAMP;
		} else if(jdbcType.contains(FLOAT)) {
			jdbcType = FLOAT;
		} else if(jdbcType.contains(DOUBLE)) {
			jdbcType = DOUBLE;
		} else if(jdbcType.contains(DECIMAL)) {
			jdbcType = DECIMAL;
		} else if(jdbcType.contains(NUMBER) || jdbcType.contains(NUMERIC)) {
			jdbcType = NUMERIC;
		} else if(jdbcType.contains(TEXT) || jdbcType.contains(LONGVARCHAR)) {
			jdbcType = LONGVARCHAR;
		} else if(jdbcType.contains(VARCHAR)) {
			jdbcType = VARCHAR;
		}
		return jdbcType;
	}

}
