## ADDED Requirements

### Requirement: Authenticate via shared secret key
The system SHALL authenticate users by validating their name and secret key against the `space_config` table in Supabase.

#### Scenario: Successful login with valid credentials
- WHEN user enters a name matching `user1_name` or `user2_name` in `space_config` with the matching `secret_key`
- THEN the system SHALL set `isLoggedIn = true` and store the user name

#### Scenario: Failed login with wrong name
- WHEN user enters a name that does not match `user1_name` or `user2_name` in `space_config`
- THEN the system SHALL display an error message "Identidade nao reconhecida"

#### Scenario: Failed login with wrong secret key
- WHEN user enters a valid name but the secret key does not match `space_config.secret_key`
- THEN the system SHALL display an error message "Chave Cosmica invalida"

#### Scenario: Login persists across app restarts
- WHEN user successfully logs in and restarts the app
- THEN the system SHALL restore the logged-in state without re-prompting for credentials

### Requirement: Fetch space configuration on login
The system SHALL fetch the full `space_config` row on successful login and expose all fields to the UI.

#### Scenario: Space config loaded after login
- WHEN login succeeds
- THEN the system SHALL load `space_config` and update the UI with the couples names, avatars, and customizations

### Requirement: Logout clears session
The system SHALL clear the authentication state on logout.

#### Scenario: Logout
- WHEN user clicks logout
- THEN the system SHALL set `isLoggedIn = false`, clear stored session, and return to login screen