/*
 * @(#)UUID.java 1.18 06/06/02
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.tfzzh.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * A class that represents an immutable universally unique identifier (UUID). A UUID represents a 128-bit value.
 * <p>
 * There exist different variants of these global identifiers. The methods of this class are for manipulating the Leach-Salz variant, although the constructors allow the creation of any variant of UUID (described below).
 * <p>
 * The layout of a variant 2 (Leach-Salz) UUID is as follows: The most significant long consists of the following unsigned fields:
 * 
 * <pre>
 * 0xFFFFFFFF00000000 time_low
 * 0x00000000FFFF0000 time_mid
 * 0x000000000000F000 version
 * 0x0000000000000FFF time_hi
 * </pre>
 * 
 * The least significant long consists of the following unsigned fields:
 * 
 * <pre>
 * 0xC000000000000000 variant
 * 0x3FFF000000000000 clock_seq
 * 0x0000FFFFFFFFFFFF node
 * </pre>
 * <p>
 * The variant field contains a value which identifies the layout of the <tt>UUID</tt>. The bit layout described above is valid only for a <tt>UUID</tt> with a variant value of 2, which indicates the Leach-Salz variant.
 * <p>
 * The version field holds a value that describes the type of this <tt>UUID</tt>. There are four different basic types of UUIDs: time-based, DCE security, name-based, and randomly generated UUIDs. These types have a version value of 1, 2, 3 and 4, respectively.
 * <p>
 * For more information including algorithms used to create <tt>UUID</tt>s, see <a href="http://www.ietf.org/rfc/rfc4122.txt"> <i>RFC&nbsp;4122: A Universally Unique IDentifier (UUID) URN Namespace</i></a>, section 4.2 &quot;Algorithms for Creating a Time-Based UUID&quot;.
 * 
 * @version 1.18, 06/02/06
 * @since 1.5
 * @model
 */
public final class TfzzhUUID implements java.io.Serializable, Comparable<TfzzhUUID> {

	/**
	 * Explicit serialVersionUID for interoperability.
	 */
	private static final long serialVersionUID = -1186386173136052152L;

	/**
	 * The most significant 64 bits of this UUID.
	 * 
	 * @serial mostSigBits
	 */
	private final long mostSigBits;

	/**
	 * The least significant 64 bits of this UUID.
	 * 
	 * @serial leastSigBits
	 */
	private final long leastSigBits;

	/**
	 * The version number associated with this UUID. Computed on demand.
	 */
	private transient int version = -1;

	/**
	 * The variant number associated with this UUID. Computed on demand.
	 */
	private transient int variant = -1;

	/**
	 * The timestamp associated with this UUID. Computed on demand.
	 */
	private transient volatile long timestamp = -1;

	/**
	 * The clock sequence associated with this UUID. Computed on demand.
	 */
	private transient int sequence = -1;

	/**
	 * The node number associated with this UUID. Computed on demand.
	 */
	private transient long node = -1;

	/**
	 * The hashcode of this UUID. Computed on demand.
	 */
	private transient int hashCode = -1;

	/**
	 * The random number generator used by this class to create random based UUIDs.
	 */
	private static volatile SecureRandom numberGenerator = null;

	// Constructors and Factories
	/**
	 * Private constructor which uses a byte array to construct the new UUID.
	 * 
	 * @param data data
	 */
	private TfzzhUUID(final byte[] data) {
		long msb = 0;
		long lsb = 0;
		assert data.length == 16;
		for (int i = 0; i < 8; i++) {
			msb = (msb << 8) | (data[i] & 0xff);
		}
		for (int i = 8; i < 16; i++) {
			lsb = (lsb << 8) | (data[i] & 0xff);
		}
		this.mostSigBits = msb;
		this.leastSigBits = lsb;
	}

	/**
	 * Constructs a new <tt>UUID</tt> using the specified data. <tt>mostSigBits</tt> is used for the most significant 64 bits of
	 * the <tt>UUID</tt> and <tt>leastSigBits</tt> becomes the least significant 64 bits of the <tt>UUID</tt>.
	 * 
	 * @param mostSigBits as long
	 * @param leastSigBits as long
	 */
	public TfzzhUUID(final long mostSigBits, final long leastSigBits) {
		this.mostSigBits = mostSigBits;
		this.leastSigBits = leastSigBits;
	}

	/**
	 * Static factory to retrieve a type 4 (pseudo randomly generated) UUID. The <code>UUID</code> is generated using a
	 * cryptographically strong pseudo random number generator.
	 * 
	 * @return a randomly generated <tt>UUID</tt>.
	 */
	public static TfzzhUUID randomUUID() {
		SecureRandom ng = TfzzhUUID.numberGenerator;
		if (ng == null) {
			TfzzhUUID.numberGenerator = ng = new SecureRandom();
		}
		final byte[] randomBytes = new byte[16];
		ng.nextBytes(randomBytes);
		randomBytes[6] &= 0x0f; /* clear version */
		randomBytes[6] |= 0x40; /* set to version 4 */
		randomBytes[8] &= 0x3f; /* clear variant */
		randomBytes[8] |= 0x80; /* set to IETF variant */
		return new TfzzhUUID(randomBytes);
	}

	/**
	 * Static factory to retrieve a type 3 (name based) <tt>UUID</tt> based on the specified byte array.
	 * 
	 * @param name a byte array to be used to construct a <tt>UUID</tt>.
	 * @return a <tt>UUID</tt> generated from the specified array.
	 */
	public static TfzzhUUID nameUUIDFromBytes(final byte[] name) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException nsae) {
			throw new InternalError("MD5 not supported");
		}
		final byte[] md5Bytes = md.digest(name);
		md5Bytes[6] &= 0x0f; /* clear version */
		md5Bytes[6] |= 0x30; /* set to version 3 */
		md5Bytes[8] &= 0x3f; /* clear variant */
		md5Bytes[8] |= 0x80; /* set to IETF variant */
		return new TfzzhUUID(md5Bytes);
	}

	/**
	 * Creates a <tt>UUID</tt> from the string standard representation as described in the {@link #toString} method.
	 * 
	 * @param name a string that specifies a <tt>UUID</tt>.
	 * @return a <tt>UUID</tt> with the specified value.
	 * @throws IllegalArgumentException if name does not conform to the string representation as described in {@link #toString}.
	 */
	public static TfzzhUUID fromString(final String name) {
		final String[] components = name.split("-");
		if (components.length != 5) {
			throw new IllegalArgumentException("Invalid UUID string: " + name);
		}
		for (int i = 0; i < 5; i++) {
			components[i] = "0x" + components[i];
		}
		long mostSigBits = Long.decode(components[0]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[1]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[2]).longValue();
		long leastSigBits = Long.decode(components[3]).longValue();
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(components[4]).longValue();
		return new TfzzhUUID(mostSigBits, leastSigBits);
	}

	// Field Accessor Methods
	/**
	 * Returns the least significant 64 bits of this UUID's 128 bit value.
	 * 
	 * @return the least significant 64 bits of this UUID's 128 bit value.
	 */
	public long getLeastSignificantBits() {
		return this.leastSigBits;
	}

	/**
	 * Returns the most significant 64 bits of this UUID's 128 bit value.
	 * 
	 * @return the most significant 64 bits of this UUID's 128 bit value.
	 */
	public long getMostSignificantBits() {
		return this.mostSigBits;
	}

	/**
	 * The version number associated with this <tt>UUID</tt>. The version number describes how this <tt>UUID</tt> was generated.
	 * The version number has the following meaning:
	 * <p>
	 * <ul>
	 * <li>1 Time-based UUID
	 * <li>2 DCE security UUID
	 * <li>3 Name-based UUID
	 * <li>4 Randomly generated UUID
	 * </ul>
	 * 
	 * @return the version number of this <tt>UUID</tt>.
	 */
	public int version() {
		if (this.version < 0) {
			// Version is bits masked by 0x000000000000F000 in MS long
			this.version = (int) ((this.mostSigBits >> 12) & 0x0f);
		}
		return this.version;
	}

	/**
	 * The variant number associated with this <tt>UUID</tt>. The variant number describes the layout of the <tt>UUID</tt>. The
	 * variant number has the following meaning:
	 * <p>
	 * <ul>
	 * <li>0 Reserved for NCS backward compatibility
	 * <li>2 The Leach-Salz variant (used by this class)
	 * <li>6 Reserved, Microsoft Corporation backward compatibility
	 * <li>7 Reserved for future definition
	 * </ul>
	 * 
	 * @return the variant number of this <tt>UUID</tt>.
	 */
	public int variant() {
		if (this.variant < 0) {
			// This field is composed of a varying number of bits
			if ((this.leastSigBits >>> 63) == 0) {
				this.variant = 0;
			} else if ((this.leastSigBits >>> 62) == 2) {
				this.variant = 2;
			} else {
				this.variant = (int) (this.leastSigBits >>> 61);
			}
		}
		return this.variant;
	}

	/**
	 * The timestamp value associated with this UUID.
	 * <p>
	 * The 60 bit timestamp value is constructed from the time_low, time_mid, and time_hi fields of this <tt>UUID</tt>. The resulting timestamp is measured in 100-nanosecond units since midnight, October 15, 1582 UTC.
	 * <p>
	 * The timestamp value is only meaningful in a time-based UUID, which has version type 1. If this <tt>UUID</tt> is not a time-based UUID then this method throws UnsupportedOperationException.
	 * 
	 * @return long
	 * @throws UnsupportedOperationException if this UUID is not a version 1 UUID.
	 */
	public long timestamp() {
		if (this.version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}
		long result = this.timestamp;
		if (result < 0) {
			result = (this.mostSigBits & 0x0000000000000FFFL) << 48;
			result |= ((this.mostSigBits >> 16) & 0xFFFFL) << 32;
			result |= this.mostSigBits >>> 32;
			this.timestamp = result;
		}
		return result;
	}

	/**
	 * The clock sequence value associated with this UUID.
	 * <p>
	 * The 14 bit clock sequence value is constructed from the clock sequence field of this UUID. The clock sequence field is used to guarantee temporal uniqueness in a time-based UUID.
	 * <p>
	 * The clockSequence value is only meaningful in a time-based UUID, which has version type 1. If this UUID is not a time-based UUID then this method throws UnsupportedOperationException.
	 * 
	 * @return the clock sequence of this <tt>UUID</tt>.
	 * @throws UnsupportedOperationException if this UUID is not a version 1 UUID.
	 */
	public int clockSequence() {
		if (this.version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}
		if (this.sequence < 0) {
			this.sequence = (int) ((this.leastSigBits & 0x3FFF000000000000L) >>> 48);
		}
		return this.sequence;
	}

	/**
	 * The node value associated with this UUID.
	 * <p>
	 * The 48 bit node value is constructed from the node field of this UUID. This field is intended to hold the IEEE 802 address of the machine that generated this UUID to guarantee spatial uniqueness.
	 * <p>
	 * The node value is only meaningful in a time-based UUID, which has version type 1. If this UUID is not a time-based UUID then this method throws UnsupportedOperationException.
	 * 
	 * @return the node value of this <tt>UUID</tt>.
	 * @throws UnsupportedOperationException if this UUID is not a version 1 UUID.
	 */
	public long node() {
		if (this.version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}
		if (this.node < 0) {
			this.node = this.leastSigBits & 0x0000FFFFFFFFFFFFL;
		}
		return this.node;
	}

	// Object Inherited Methods
	/**
	 * Returns a <code>String</code> object representing this <code>UUID</code>.
	 * <p>
	 * The UUID string representation is as described by this BNF : <blockquote>
	 * 
	 * <pre>
	 * {@code
	 * UUID                   = <time_low> "-" <time_mid> "-"
	 *                          <time_high_and_version> "-"
	 *                          <variant_and_sequence> "-"
	 *                          <node>
	 * time_low               = 4*<hexOctet>
	 * time_mid               = 2*<hexOctet>
	 * time_high_and_version  = 2*<hexOctet>
	 * variant_and_sequence   = 2*<hexOctet>
	 * node                   = 6*<hexOctet>
	 * hexOctet               = <hexDigit><hexDigit>
	 * hexDigit               =
	 *       "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
	 *       | "a" | "b" | "c" | "d" | "e" | "f"
	 *       | "A" | "B" | "C" | "D" | "E" | "F"
	 * }
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @return a string representation of this <tt>UUID</tt>.
	 */
	@Override
	public String toString() {
		return (TfzzhUUID.digits(this.mostSigBits >> 32, 8) + TfzzhUUID.digits(this.mostSigBits >> 16, 4) + TfzzhUUID.digits(this.mostSigBits, 4) + TfzzhUUID.digits(this.leastSigBits >> 48, 4) + TfzzhUUID.digits(this.leastSigBits, 12));
	}

	/**
	 * Returns val represented by the specified number of hex digits.
	 * 
	 * @param val as long
	 * @param digits as int
	 * @return as String
	 */
	private static String digits(final long val, final int digits) {
		final long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	/**
	 * Returns a hash code for this <code>UUID</code>.
	 * 
	 * @return a hash code value for this <tt>UUID</tt>.
	 */
	@Override
	public int hashCode() {
		if (this.hashCode == -1) {
			this.hashCode = (int) ((this.mostSigBits >> 32) ^ this.mostSigBits ^ (this.leastSigBits >> 32) ^ this.leastSigBits);
		}
		return this.hashCode;
	}

	/**
	 * Compares this object to the specified object. The result is <tt>true</tt> if and only if the argument is not <tt>null</tt> , is a <tt>UUID</tt> object, has the same variant, and contains the same value, bit for bit, as this <tt>UUID</tt>.
	 * 
	 * @param obj the object to compare with.
	 * @return <code>true</code> if the objects are the same; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof TfzzhUUID) || (((TfzzhUUID) obj).variant() != this.variant())) {
			return false;
		}
		final TfzzhUUID id = (TfzzhUUID) obj;
		return ((this.mostSigBits == id.mostSigBits) && (this.leastSigBits == id.leastSigBits));
	}

	// Comparison Operations
	/**
	 * Compares this UUID with the specified UUID.
	 * <p>
	 * The first of two UUIDs follows the second if the most significant field in which the UUIDs differ is greater for the first UUID.
	 * 
	 * @param val <tt>UUID</tt> to which this <tt>UUID</tt> is to be compared.
	 * @return -1, 0 or 1 as this <tt>UUID</tt> is less than, equal to, or greater than <tt>val</tt>.
	 */
	@Override
	public int compareTo(final TfzzhUUID val) {
		// The ordering is intentionally set up so that the UUIDs
		// can simply be numerically compared as two numbers
		return (this.mostSigBits < val.mostSigBits ? -1 : (this.mostSigBits > val.mostSigBits ? 1 : (this.leastSigBits < val.leastSigBits ? -1 : (this.leastSigBits > val.leastSigBits ? 1 : 0))));
	}

	/**
	 * Reconstitute the <tt>UUID</tt> instance from a stream (that is, deserialize it). This is necessary to set the transient
	 * fields to their correct uninitialized value so they will be recomputed on demand.
	 * 
	 * @param in as ObjectInputStream
	 * @throws java.io.IOException 抛
	 * @throws ClassNotFoundException 抛
	 */
	private void readObject(final java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
		in.defaultReadObject();
		// Set "cached computation" fields to their initial values
		this.version = -1;
		this.variant = -1;
		this.timestamp = -1;
		this.sequence = -1;
		this.node = -1;
		this.hashCode = -1;
	}
}
