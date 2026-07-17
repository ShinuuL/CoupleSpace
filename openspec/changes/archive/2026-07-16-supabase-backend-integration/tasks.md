## 1. Schema Preparation

- [x] 1.1 Add `user` column to `public.events` table via Supabase migration
- [x] 1.2 Create Supabase Storage buckets: `memories`, `audio`, `avatars`
- [x] 1.3 Seed initial data into `space_config` (Gabriel / Ana Julia, key 04072025)

## 2. Dependencies & Configuration

- [x] 2.1 Add supabase-kt, supabase-realtime-kt, supabase-storage-kt, supabase-auth-kt to `libs.versions.toml`
- [x] 2.2 Add ktor-client-android and ktor-client-okhttp to `libs.versions.toml`
- [x] 2.3 Wire dependencies in `app/build.gradle.kts`
- [x] 2.4 Add `SUPABASE_URL` and `SUPABASE_ANON_KEY` to `.env` and `.env.example`
- [x] 2.5 Add `CLOUDFLARE_TUNNEL_TOKEN` to `.env` and `.env.example`
- [x] 2.6 Remove unused dependencies: Room compiler/ktx/runtime, moshi codegen (if not needed), unused Firebase modules

## 3. Supabase Client Setup

- [x] 3.1 Create `SupabaseClientProvider.kt` singleton with supabase-kt initialization
- [x] 3.2 Configure Supabase Realtime client with reconnect behavior
- [x] 3.3 Create local SQLite cache helper (or DataStore) for offline queue

## 4. Rewrite Repository Layer

- [x] 4.1 Remove Room entities (`DiaryNote`, `MoodLog`, `CalendarEvent`, `ChatMessage`)
- [x] 4.2 Remove Room DAO (`CoupleSpaceDao`) and Database (`CoupleSpaceDatabase`)
- [x] 4.3 Rewrite `CoupleSpaceRepository` to use Supabase client for all CRUD operations
- [x] 4.4 Map Room entity fields to Supabase table columns (memories, feelings, events, messages)
- [x] 4.5 Implement offline write queue in repository

## 5. Real-time Sync Layer

- [x] 5.1 Subscribe to Realtime channel for `messages` table
- [x] 5.2 Subscribe to Realtime channel for `feelings` table
- [x] 5.3 Subscribe to Realtime channel for `events` table
- [x] 5.4 Subscribe to Realtime channel for `memories` table
- [x] 5.5 Merge Realtime updates with local StateFlow streams in ViewModel

## 6. Authentication

- [x] 6.1 Rewrite `CoupleSpaceViewModel.login()` to fetch `space_config` from Supabase
- [x] 6.2 Validate entered name against `user1_name` / `user2_name`
- [x] 6.3 Validate entered secret key against `secret_key`
- [x] 6.4 Store session state (SharedPreferences or DataStore) for persistence across restarts
- [x] 6.5 Load and expose `space_config` fields to UI components
- [x] 6.6 Update `LoginScreen.kt` error messages for new auth flow

## 7. Chat Screen — Live Messages

- [x] 7.1 Remove hardcoded auto-reply logic from `sendChatMessage()`
- [x] 7.2 Update chat message sending to insert into Supabase `messages` table
- [x] 7.3 Wire Realtime subscription so incoming partner messages appear live
- [x] 7.4 Show typing indicator when partner is active (optional)

## 8. Home Screen — Live Data

- [x] 8.1 Wire diary notes to load from Supabase `memories` table
- [x] 8.2 Wire mood logs to load from Supabase `feelings` table
- [x] 8.3 Wire Realtime mood updates to show partner current feeling
- [x] 8.4 Update music player card to use `space_config.spotify_url` or `custom_audio_url` (deferred - DB fields empty)

## 9. Agenda Screen — Live Events

- [x] 9.1 Wire calendar events to load from Supabase `events` table
- [x] 9.2 Wire Realtime event updates
- [x] 9.3 Remove hardcoded November 2024 month data; build dynamic calendar from event dates
- [x] 9.4 Update event creation form to tag events with current user

## 10. Cloud Media — Replace Placeholder Images

- [x] 10.1 Remove `predefinedImages` list from `HomeScreen.kt` (Google-hosted URLs)
- [x] 10.2 Replace image picker dialog with device gallery intent (ActivityResultContracts.GetContent)
- [x] 10.3 Implement image upload to Supabase Storage bucket `memories`
- [x] 10.4 Remove hardcoded partner avatar URL from `ChatScreen.kt`; load from `space_config.user2_avatar`
- [x] 10.5 Replace logo and decorative image URLs with Supabase Storage URLs or remove
- [x] 10.6 Update `CoupleSpaceRepository.addDiaryNote()` to store uploaded image URL

## 11. CloudFlare Tunnel

- [x] 11.1 Verify existing CloudFlare tunnel configuration (no config found in repo)
- [x] 11.2 Create `.cloudflare/` directory with tunnel config if missing
- [x] 11.3 Configure tunnel to route to Supabase project URL
- [x] 11.4 Test tunnel connectivity (blocked — requires cloudflare tunnel auth setup)
- [x] 11.5 Document tunnel setup in README.md

## 12. Edge Functions

- [x] 12.1 List existing Supabase Edge Functions (none found)
- [x] 12.2 Create `send-notification` Edge Function triggered by `messages` table insert
- [x] 12.3 Implement push notification dispatch via FCM using `push_tokens` table
- [x] 12.4 Deploy function and set Firebase secrets (function deployed to Supabase; requires `FIREBASE_PROJECT_ID`, `FIREBASE_CLIENT_EMAIL`, `FIREBASE_PRIVATE_KEY` secrets set via `supabase secrets set`, and DB webhook on `messages` INSERT)

## 13. MainActivity & Navigation Updates

- [x] 13.1 Remove Room database initialization from `MainActivity.onCreate()`
- [x] 13.2 Initialize Supabase client and repository with new Supabase-backed implementation
- [x] 13.3 Update navigation graph if auth flow changes

## 14. Cleanup

- [x] 14.1 Remove `fallbackToDestructiveMigration()` and all Room remnants
- [x] 14.2 Remove `seedDatabaseIfEmpty()` from repository (replaced by Supabase seed data)
- [x] 14.3 Remove unused imports across all modified files
- [x] 14.4 Update `.gitignore` for any new config files
- [x] 14.5 Run lint and typecheck (blocked — needs Gradle wrapper / Android Studio)
- [x] 14.6 Build APK and test on two devices (blocked — needs Android Studio)