## ADDED Requirements

### Requirement: Store diary images in Supabase Storage
The system SHALL allow users to upload photos to Supabase Storage when adding diary memories, instead of selecting from predefined Google-hosted URLs.

#### Scenario: Upload image for new memory
- WHEN user taps Add Memory and selects an image from the device gallery
- THEN the image SHALL be uploaded to Supabase Storage bucket `memories`
- THEN the returned public URL SHALL be saved in the `memories.image_url` field

#### Scenario: View memory with uploaded image
- WHEN user views a memory that has an uploaded image
- THEN the image SHALL be loaded from Supabase Storage public URL via Coil

### Requirement: Replace predefined image picker
The existing predefined image picker (4 Google-hosted image URLs) SHALL be removed.

#### Scenario: Image picker shows device gallery
- WHEN user taps to add an image to a diary note
- THEN the system SHALL open the device photo gallery instead of showing predefined image options

### Requirement: Partner avatars from storage
The system SHALL load partner avatars from Supabase Storage using URLs stored in `space_config.user1_avatar` and `space_config.user2_avatar`.

#### Scenario: Avatar displayed in chat header
- WHEN user opens the chat screen
- THEN the partner avatar SHALL be loaded from `space_config.user2_avatar` via Coil

### Requirement: Audio messages in storage
The system SHALL store recorded audio messages in Supabase Storage bucket `audio`.

#### Scenario: Audio message recorded and sent
- WHEN user records an audio message
- THEN the audio SHALL be uploaded to Supabase Storage bucket `audio`
- THEN the URL SHALL be saved in `messages.audio_url` with duration in `messages.audio_duration`