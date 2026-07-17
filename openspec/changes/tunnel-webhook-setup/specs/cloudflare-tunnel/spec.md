## ADDED Requirements

### Requirement: CloudFlare tunnel configured
The system SHALL have a CloudFlare tunnel configured to expose Supabase Edge Functions with HTTPS and a custom domain, using the existing tunnel infrastructure.

#### Scenario: Tunnel is active
- WHEN the tunnel configuration is applied
- THEN Edge Functions SHALL be reachable at the configured CloudFlare domain with valid HTTPS

#### Scenario: Tunnel routes to Supabase functions
- WHEN a request is made to the tunnel domain
- THEN the request SHALL be proxied to the corresponding Supabase Edge Function

### Requirement: Tunnel configuration documented
The tunnel configuration SHALL be documented in the project README or a cloudflare config file.

#### Scenario: Configuration available
- WHEN a developer needs to modify the tunnel
- THEN the tunnel configuration SHALL be findable in a `.cloudflare/` directory or equivalent
- THEN the README SHALL reference how to restart the tunnel