package blog.config;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/6 16:50
 */
@Component
public class LocalCache {
	private static final ExpiringMap<String, Object> map = ExpiringMap.builder()
			.variableExpiration()
			.expiration(3600, TimeUnit.SECONDS)
			.expirationPolicy(ExpirationPolicy.ACCESSED)
			.maxSize(500)
			.build();

	public static void put(String key, Object value){
		map.put(key, value);
	}

	public static void put(String key, Object value,long expire){
		map.put(key, value, expire, TimeUnit.SECONDS);
	}

	public static Object get(String key){
	 	return map.get(key);
	}

	public static void remove(String key){
		map.remove(key);
	}

	public static void removeByPre(String pre) {
		Set<String > keys = map.keySet();
		String regex = "^"+pre+".*";
		keys.removeIf(key -> key.matches(regex));
	}
}
