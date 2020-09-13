package com.southwind.mock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MockDB {
    //记录token
    public static Set<String> tokenSet = new HashSet<>();
    //记录客户端URL
    public static Map<String,Set<String>> clientLogoutUrlMap = new HashMap<>();
}
