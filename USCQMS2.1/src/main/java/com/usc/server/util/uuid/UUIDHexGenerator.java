package com.usc.server.util.uuid;
import java.net.InetAddress;
import java.util.Map;


public class UUIDHexGenerator {

	private static String sep = "";

	private static final int IP;

	private static String formatedIP = "";

	private static final int JVM = (int) ( System.currentTimeMillis() >>> 8 );

	private static String formatedJVM = "";

	static {
		int ipadd;
		try {
			ipadd = toInt( InetAddress.getLocalHost().getAddress() );
		}
		catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
		formatedIP = format( getIP());
		formatedJVM = format( getJVM());
	}
	private static short counter = (short) 0;


	public static String generate() {
		return (formatedIP + sep
				+ formatedJVM + sep
				+ format( getHiTime() ) + sep
				+ format( getLoTime() ) + sep
				+ format( getCount() )).toUpperCase();
	}

	private static String format(int intValue) {
		String formatted = Integer.toHexString( intValue );
		StringBuilder buf = new StringBuilder( "00000000" );
		buf.replace( 8 - formatted.length(), 8, formatted );
		return buf.toString();
	}

	private static String format(short shortValue) {
		String formatted = Integer.toHexString( shortValue );
		StringBuilder buf = new StringBuilder( "0000" );
		buf.replace( 4 - formatted.length(), 4, formatted );
		return buf.toString();
	}


	private static int getJVM() {
		return JVM;
	}


	protected static short getCount() {
		synchronized(UUIDHexGenerator.class) {
			if (counter<0) counter=0;
			return counter++;
		}
	}


	private static int getIP() {
		return IP;
	}


	private static short getHiTime() {
		return (short) ( System.currentTimeMillis() >>> 32 );
	}
	private static int getLoTime() {
		return (int) System.currentTimeMillis();
	}


	private String getString(String name, Map values) {
		Object value = values.get( name );
		if ( value == null ) {
			return null;
		}
		if ( String.class.isInstance( value ) ) {
			return (String) value;
		}
		return value.toString();
	}

	private static int toInt(byte[] bytes) {
		int result = 0;
		for ( int i = 0; i < 4; i++ ) {
			result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}
}
