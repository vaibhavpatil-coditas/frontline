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
        public static final String AGENT_ID = "/{agent-id}";
    }

    public static final class Ticket {


        private Ticket(){}
        public static final String PRIORITY = "/priority";
        public static final String RATE = "/rate";
        public static final String ESCALATE = "/escalate";
        public static final String RESOLVE = "/resolve";
        public static final String TICKETS = "/tickets";
        public static final String TICKET_ID = "/{ticket-id}";
        public static final String ASSIGN = "/assign";
        public static final String CHAT = "/chat";
    }

    public static final class Team {
        private Team(){}
        public static final String TEAMS = "/teams";
        public static final String TEAM_ID = "/{team-id}";
        public static final String ADD_MEMBER = "/members";
    }
}
