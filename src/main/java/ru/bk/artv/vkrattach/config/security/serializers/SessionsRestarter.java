package ru.bk.artv.vkrattach.config.security.serializers;

public interface SessionsRestarter {
    public void closeAllSessions()  throws Exception ;
}
