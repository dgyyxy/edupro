package com.edusys.manager.shiro.session;

import com.edu.common.util.RedisUtil;
import com.edusys.manager.utils.SerializableUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.Set;

/**
 * 基于redis的sessionDao,缓存共享session
 * Created by Gary on 2017/3/30.
 */
public class SessionDao extends CachingSessionDAO{

    private static Logger _log = LoggerFactory.getLogger(SessionDao.class);

    // 会话key
    private final static String SYS_SHIRO_SESSION_ID = "sys-shiro-session-id";
    // 全局会话key
    private final static String SYS_SERVER_SESSION_ID = "sys-server-session-id";
    // 全局会话列表key
    private final static String SYS_SERVER_SESSION_IDS = "sys-server-session-ids";
    // code key
    private final static String SYS_SERVER_CODE = "sys-server-code";


    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        RedisUtil.set(SYS_SHIRO_SESSION_ID + "_" + sessionId, SerializableUtil.serialize(session));
        _log.debug("doCreate >>>>> sessionId={}", sessionId);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String session = RedisUtil.get(SYS_SHIRO_SESSION_ID + "_" + sessionId);
        _log.debug("doReadSession >>>>> sessionId={}", sessionId);
        return SerializableUtil.deserialize(session);
    }

    @Override
    protected void doUpdate(Session session) {
        // 如果会话过期/停止 没必要更新
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()){
            return;
        }
        //更新session的最后一次访问时间
        ShiroSession shiroSession = (ShiroSession) session;
        ShiroSession cacheSession = (ShiroSession) doReadSession(session.getId());
        if(null != cacheSession){
            shiroSession.setStatus(cacheSession.getStatus());
            shiroSession.setAttribute("FORCE_LOGOUT", cacheSession.getAttribute("FORCE_LOGOUT"));
        }
        RedisUtil.set(SYS_SHIRO_SESSION_ID + "_" + session.getId(), SerializableUtil.serialize(session), (int)session.getTimeout() / 1000);
        _log.debug("doUpdate >>>>> sessionId={}", session.getId());
    }

    @Override
    protected void doDelete(Session session) {
        String sessionId = session.getId().toString();
        // 当前全局会话code
        String code = RedisUtil.get(SYS_SERVER_SESSION_ID + "_" + sessionId);
        // 清除全局会话
        RedisUtil.remove(SYS_SERVER_SESSION_ID + "_" + sessionId);
        // 清除code校验值
        RedisUtil.remove(SYS_SERVER_CODE + "_" + code);
        // 清除所有局部会话
        Jedis jedis = RedisUtil.getJedis();
        Set<String> serverSessionIds = jedis.smembers(SYS_SERVER_SESSION_IDS + "_" + code);
        for (String serverSessionId : serverSessionIds) {
            jedis.del(SYS_SERVER_SESSION_ID + "_" + serverSessionId);
            jedis.srem(SYS_SERVER_SESSION_IDS + "_" + code, serverSessionId);
        }
        _log.debug("当前code={}，对应的注册系统个数：{}个", code, jedis.scard(SYS_SERVER_SESSION_IDS + "_" + code));
        jedis.close();
        // 维护会话id列表，提供会话分页管理
        RedisUtil.lrem(SYS_SERVER_SESSION_IDS, 1, sessionId);
        RedisUtil.remove(SYS_SHIRO_SESSION_ID + "_" + sessionId);
        _log.debug("doUpdate >>>>> sessionId={}", sessionId);
    }

    /**
     * 更改在线状态
     *
     * @param sessionId
     * @param onlineStatus
     */
    public void updateStatus(Serializable sessionId, ShiroSession.OnlineStatus onlineStatus) {
        ShiroSession session = (ShiroSession) doReadSession(sessionId);
        if (null == session) {
            return;
        }
        session.setStatus(onlineStatus);
        RedisUtil.set(SYS_SHIRO_SESSION_ID + "_" + session.getId(), SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
    }

}
