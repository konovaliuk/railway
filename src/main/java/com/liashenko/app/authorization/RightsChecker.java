package com.liashenko.app.authorization;


import java.lang.annotation.Annotation;
import java.util.Map;

import static com.liashenko.app.controller.utils.Asserts.assertIsNull;

public abstract class RightsChecker<T> {

    public static <T> T checkUserRightsAndGetCommand(String actualAction, String defaultAction, Long currentRole,
                                                     Map<String, T> commands) {
        T requestedCommand = commands.get(actualAction);
        if (requestedCommand == null) {
            return commands.get(defaultAction);
        }
        String resultAction = getAction(actualAction, requestedCommand, currentRole);
        return commands.get(resultAction);
    }

    private static <T> String getAction(String actualAction, T command, Long currentRole) {
        String resultAction = null;
        Class clazz = command.getClass();
        String restrictedAction = getRestrictedAction(clazz, currentRole);
        String allowedAction = getAllowedAction(actualAction, clazz, currentRole);
        if (assertIsNull(restrictedAction)) {
            resultAction = assertIsNull(allowedAction) ? actualAction : allowedAction;
        } else {
            resultAction = restrictedAction;
        }
        return resultAction;
    }

    private static String getAllowedAction(String actualAction, Class clazz, Long currentRole) {
        String resultAction = null;
        if (clazz.isAnnotationPresent(Authorization.Allowed.class)) {
            Annotation annotation = clazz.getAnnotation(Authorization.Allowed.class);
            Authorization.Allowed authAllowedAnnot = (Authorization.Allowed) annotation;

            String defActionValue = authAllowedAnnot.defAction();
            if (!defActionValue.isEmpty()) {
                resultAction = defActionValue;
            }
            long[] rolesValues = authAllowedAnnot.roles();
            if (rolesValues.length > 0) {
                for (long role : rolesValues) {
                    if (role == currentRole) {
                        resultAction = actualAction;
                        break;
                    }
                }
            }
        }
        return resultAction;
    }

    private static String getRestrictedAction(Class clazz, Long currentRole) {
        String resultAction = null;
        int priority = Integer.MIN_VALUE;
        if (clazz.isAnnotationPresent(Authorization.Restricted.class)) {
            Annotation[] annotations = clazz.getAnnotationsByType(Authorization.Restricted.class);
            for (Annotation annotation : annotations) {
                Authorization.Restricted authRestrictAnnot = (Authorization.Restricted) annotation;
                int currentActionPriority = authRestrictAnnot.priority();
                long[] rolesValues = authRestrictAnnot.roles();
                if (rolesValues.length > 0) {
                    for (long role : rolesValues) {
                        if (role == currentRole) {
                            resultAction = authRestrictAnnot.action();
                            if (currentActionPriority > priority) {
                                priority = currentActionPriority;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return resultAction;
    }
}
