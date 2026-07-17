## ADDED Requirements

### Requirement: Push notification on new message
The system SHALL send a push notification to the partner when a new chat message is inserted into the `messages` table.

#### Scenario: Partner receives notification
- WHEN user sends a chat message
- THEN a Supabase Edge Function SHALL be triggered via database webhook
- THEN the function SHALL look up the partner push token from `push_tokens`
- THEN the function SHALL send a push notification via FCM or the registered platform

### Requirement: Register push token
The system SHALL register the device push token in the `push_tokens` table on login.

#### Scenario: Token registered on login
- WHEN user logs in successfully
- THEN the device push token SHALL be inserted into `push_tokens` with the user name and platform

#### Scenario: Token updated on change
- WHEN the device push token changes
- THEN the system SHALL update the corresponding row in `push_tokens`