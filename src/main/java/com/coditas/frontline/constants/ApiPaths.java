package com.coditas.frontline.constants;

public final class ApiPaths {
    private ApiPaths(){}
    public static final String BASE_PATH = "/api/v1";
    public static final String LOGIN = "/login";

    public static final class Customer {
        private Customer(){}
        public static final String CUSTOMERS = "/customers";
    }

    public static final class Manager {
        private Manager(){}
        public static final String MANAGERS = "/managers";
    }

    public static final class Agent {
        private Agent(){}
        public static final String AGENTS = "/agents";
    }
}
