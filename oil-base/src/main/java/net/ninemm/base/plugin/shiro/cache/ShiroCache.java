package net.ninemm.base.plugin.shiro.cache;

import io.jboot.Jboot;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;

import java.util.*;

/**
 * Shiro 缓存，使用jboot cache
 * @param <K>
 * @param <V>
 */
public class ShiroCache<K, V> implements Cache<K, V> {

	public ShiroCache(String cacheName) {
		this.cacheName = cacheName;
	}
	
	private String cacheName;
	
	@Override
    public V get(K key) throws CacheException {
		return Jboot.getCache().get(cacheName, key);
	}

	@Override
    public V put(K key, V value) throws CacheException {
		Jboot.getCache().put(cacheName, key, value);
		return value;
	}

	@Override
    public V remove(K key) throws CacheException {
		V value = Jboot.getCache().get(cacheName, key);
		Jboot.getCache().remove(cacheName, key);
		return value;
	}

	@Override
    public void clear() throws CacheException {
		Jboot.getCache().removeAll(cacheName);
	}

	@Override
    public int size() {
		return Jboot.getCache().getKeys(cacheName).size();
	}

	@SuppressWarnings("unchecked")
	@Override
    public Set<K> keys() {
		return (Set<K>) Jboot.getCache().getKeys(cacheName);
	}

	@SuppressWarnings("rawtypes")
	@Override
    public Collection<V> values() {
		Collection<V> values = Collections.emptyList();
		List keys = Jboot.getCache().getKeys(cacheName);

		if (!CollectionUtils.isEmpty(keys)) {
			values = new ArrayList<V>(keys.size());
			for (Object key : keys) {
				V value = Jboot.getCache().get(cacheName, key);
				if (value != null) {
					values.add(value);
				}
			}
		}

		return values;
	}

}
