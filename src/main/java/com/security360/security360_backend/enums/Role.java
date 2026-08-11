package com.security360.security360_backend.enums;

public enum Role {

	    ADMIN,       // Full access to everything (God mode)
	    SUPERVISOR,  // Can view dashboards, approve patrols, manage guards, view all reports
	    ROUNDER,     // Can view multiple site patrols, approve logs, but cannot edit employees
	    GUARD        // Basic access: Can only see their own attendance, patrol tasks, and profile

}