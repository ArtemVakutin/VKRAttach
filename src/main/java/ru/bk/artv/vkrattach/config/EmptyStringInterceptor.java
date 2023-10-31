//package ru.bk.artv.vkrattach.config;
//
//import org.hibernate.EmptyInterceptor;
//import org.hibernate.type.Type;
//import org.hibernate.type.descriptor.java.StringJavaType;
//
//import java.io.Serializable;
//
///**
// * HibernateInterceptor, который в случае отсутствия данных в поле, возвращает при чтении/записи и т.д. не null,
// * а пустой String ""
// * регистрируется в application.yml
// */
//
//public class EmptyStringInterceptor extends EmptyInterceptor {
//
//    @Override
//    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
//        return convertEmptyStrings(currentState, types);
//    }
//
//    @Override
//    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
//        return convertEmptyStrings(state, types);
//    }
//
//    @Override
//    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
//        return convertEmptyStrings(state, types);
//    }
//
//    private boolean convertEmptyStrings(Object[] state, Type[] types) {
//        // convert nulls to empty strings
//        boolean modified = false;
//        for (int i=0; i<state.length; i++)
//            if ((types[i] instanceof StringJavaType) && state[i] == null) {
//                state[i] = "";
//                modified = true;
//            }
//        return modified;
//    }
//}
