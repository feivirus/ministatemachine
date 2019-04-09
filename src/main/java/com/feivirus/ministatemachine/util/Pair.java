package com.feivirus.ministatemachine.util;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;


/***
 * 参考 org.apache.commons.lang3.tuple.Pair<L, R>实现
 * @author feivirus
 *
 * @param <K>
 * @param <V>
 */
public class Pair <K, V>{
	private final K key;
	
	private final V value;
	
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public K key() {
		return key;
	}
	
	public V value() {
		return value;
	}
	
	@Override
	public int hashCode() {
		 return (key() == null ? 0 : key().hashCode()) ^
	                (value() == null ? 0 : value().hashCode()); 
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry<?, ?>) {
            Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            return ObjectUtils.equals(key(), other.getKey())
                    && ObjectUtils.equals(value(), other.getValue());
        }
        return false;
	}

	@Override
	public String toString() {
        return new StringBuilder().append('(').append(key()).append(',').append(value()).append(')').toString();
	}	
}
