/**
 * An implementation of a hashtable using double hashing
 * as a collision resolution technique.
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 **/
package com.williamfiset.datastructures.hashtable;

import java.math.BigInteger;

@SuppressWarnings("unchecked")
public class HashTableDoubleHashing <K extends SecondaryHash, V> extends HashTableOpenAddressingBase<K, V> {

  private int hash;

  // Special marker token used to indicate the deletion of a key-value pair.
  private final SecondaryHash TOMBSTONE = new SecondaryHash() {
    @Override public int hashCode2() {
      return 0;
    }
  };

  public HashTableDoubleHashing() {
    super();
  }

  public HashTableDoubleHashing(int capacity) {
    super(capacity);
  }

  // Designated constructor
  public HashTableDoubleHashing(int capacity, double loadFactor) {
    super(capacity, loadFactor);
  }

  @Override
  protected void setupProbing(K key) {
    // Cache second hash value.
    hash = normalizeIndex(key.hashCode2());

    // Fail safe to avoid infinite loop.
    if (hash == 0) hash = 1;
  }

  @Override
  protected int probe(int x) {
    return x * hash;
  }
  
  // Increase the capacity until it is a prime number. The reason for
  // doing this is to help ensure that the GCD(h2, capacity) = 1 when 
  // probing so that all the cells can be reached
  @Override
  protected void adjustCapacity() {
    while(!(new BigInteger(String.valueOf(capacity)).isProbablePrime(20))) {
      capacity++;
    }
  }

}
