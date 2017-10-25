package com.edusys.front.listener;

import com.edu.common.dao.model.EduStudent;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by Gary on 2017/9/6.
 */
public class SessionContext {
    private static SessionContext instance;
    private HashMap<String,HttpSession> sessionMap;

    private SessionContext() {
        sessionMap = new HashMap<String,HttpSession>();
    }

    public static SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    public synchronized void AddSession(HttpSession session) {
        if (session != null) {
            sessionMap.put(session.getId(), session);
        }
    }

    public synchronized void DelSession(HttpSession session) {
        if (session != null) {
            sessionMap.remove(session.getId());
            if(session.getAttribute("user")!=null){
                EduStudent student = (EduStudent)session.getAttribute("user");
                sessionMap.remove(student.getStuId().toString());
                //session.invalidate();
            }
        }
    }

    public synchronized HttpSession getSession(String session_id) {
        if (session_id == null) return null;
        return (HttpSession) sessionMap.get(session_id);
    }

    public HashMap getSessionMap() {
        return sessionMap;
    }

    public void setMymap(HashMap sessionMap) {
        this.sessionMap = sessionMap;
    }
}
