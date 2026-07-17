## ADDED Requirements

### Requirement: Sync messages in real-time
The system SHALL subscribe to Supabase Realtime changes on the `messages` table and update the chat UI immediately when the partner sends a message.

#### Scenario: Partner message appears instantly
- WHEN a new row is inserted into `messages` by another device
- THEN the message SHALL appear in the chat UI within 2 seconds without manual refresh

#### Scenario: Own message syncs to partner
- WHEN user sends a chat message
- THEN the message SHALL be inserted into Supabase `messages` table and propagated to the partner device via Realtime

### Requirement: Sync feelings in real-time
The system SHALL subscribe to Realtime changes on the `feelings` table and update the mood display when the partner logs a feeling.

#### Scenario: Partner mood appears on home screen
- WHEN partner inserts or updates a row in `feelings`
- THEN the home screen mood display SHALL update within 2 seconds

### Requirement: Sync events in real-time
The system SHALL subscribe to Realtime changes on the `events` table for calendar event updates.

#### Scenario: Partner adds event
- WHEN partner inserts a row in `events`
- THEN the agenda screen SHALL show the new event within 2 seconds

### Requirement: Sync memories in real-time
The system SHALL subscribe to Realtime changes on the `memories` table for diary note updates.

#### Scenario: Partner adds memory
- WHEN partner inserts a row in `memories`
- THEN the home screen SHALL show the new memory within 2 seconds

### Requirement: Offline write queue
When the device is offline, the system SHALL queue writes locally and flush them to Supabase when connectivity is restored.

#### Scenario: Message sent offline
- WHEN user sends a message while offline
- THEN the message SHALL appear immediately in the local UI (optimistic update)
- THEN the message SHALL be sent to Supabase when connectivity resumes
- THEN the partner SHALL receive the message via Realtime once flushed

#### Scenario: Connectivity restored
- WHEN device transitions from offline to online
- THEN the system SHALL flush all queued writes to Supabase in order