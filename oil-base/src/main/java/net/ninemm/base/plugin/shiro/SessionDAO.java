package net.ninemm.base.plugin.shiro;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import java.io.Serializable;

/***
 * 集成会话管理
 */
public class SessionDAO extends EnterpriseCacheSessionDAO {

	public static SessionDAO me;

	@Override
	public void setCacheManager(CacheManager cacheManager) {
		super.setCacheManager(cacheManager);
		me = this;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable id = super.doCreate(session);
		return id;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
        return super.doReadSession(sessionId);
	}

	@Override
	protected void doUpdate(Session session) {
		super.doUpdate(session);
	}

	@Override
	protected void doDelete(Session session) {
		super.doDelete(session);
	}

}
