<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://ai.google.dev/static/site-assets/images/share-ais-513315318.png" />
</div>

# CoupleSpace

App Android de casal com scrapbook, chat, agenda e sincronização em tempo real via Supabase.

## Como Rodar o Projeto

**Pré-requisitos:** Android Studio, Android SDK 24+, Java 17

1. **Clone o repositório**
   ```
   git clone <repo-url>
   cd CoupleSpace
   ```

2. **Configure as variáveis de ambiente**
   Copie `.env.example` para `.env` e preencha:
   ```
   GEMINI_API_KEY=your_gemini_key
   SUPABASE_URL=https://lzarbmfzskmwzcuudauv.supabase.co
   SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIs...
   CLOUDFLARE_TUNNEL_TOKEN=your_tunnel_token
   ```

3. **Abra no Android Studio**
   - File → Open → selecione a pasta do projeto
   - Deixe o Android Studio sincronizar as dependências do Gradle
   - Se necessário, remova a linha `signingConfig = signingConfigs.getByName("debugConfig")` do `app/build.gradle.kts`

4. **Execute o app**
   - Conecte um dispositivo Android ou inicie um emulador
   - Clique em **Run** (Shift+F10)

5. **Login**
   - Use o nome e a chave secreta cadastrados no Supabase (`space_config`)
   - Padrão: Gabriel / Ana Julia, chave `04072025`

## Comandos Úteis (Prompts)

### Supabase

```bash
# Deploy Edge Function
supabase functions deploy send-notification --no-verify-jwt

# Ver logs da função
supabase functions logs send-notification

# Definir secrets
supabase secrets set FIREBASE_PROJECT_ID="couplespace-32ab6"
supabase secrets set FIREBASE_CLIENT_EMAIL="firebase-adminsdk-fbsvc@couplespace-32ab6.iam.gserviceaccount.com"
supabase secrets set FIREBASE_PRIVATE_KEY="$(cat service-account.json)"

# Iniciar Supabase local (desenvolvimento)
supabase start
```

### CloudFlare Tunnel

```bash
# Autenticar
cloudflared tunnel login

# Criar um novo túnel
cloudflared tunnel create couplespace-api

# Listar túneis
cloudflared tunnel list

# Rodar o túnel (mantém o terminal aberto)
cloudflared tunnel run couplespace-api

# Rodar o túnel como serviço no Windows
cloudflared service install
```

### Android Build

```bash
# Build de debug
./gradlew assembleDebug

# Build de release
./gradlew assembleRelease

# Rodar lint
./gradlew lint

# Limpar build
./gradlew clean
```

## Push Notifications

Push notifications são enviadas via Edge Function do Supabase (`send-notification`) quando uma nova mensagem é inserida na tabela `messages`.

**Setup:**
1. Configure os secrets no Supabase (via `supabase secrets set` ou Dashboard > Edge Functions > Secrets):
   ```
   supabase secrets set FIREBASE_PROJECT_ID="couplespace-32ab6"
   supabase secrets set FIREBASE_CLIENT_EMAIL="firebase-adminsdk-fbsvc@couplespace-32ab6.iam.gserviceaccount.com"
   supabase secrets set FIREBASE_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\nMII...\n-----END PRIVATE KEY-----"
   ```
   > O `FIREBASE_PRIVATE_KEY` deve ter `\n` literais entre as linhas (escapados). O serviço role key não é necessário — a função usa a anon key como fallback.

2. Crie um webhook no banco:
   - **Supabase Dashboard → Database → Webhooks → Create**
   - Table: `messages`, Events: INSERT
   - Webhook URL: `https://lzarbmfzskmwzcuudauv.functions.supabase.co/send-notification`
   - HTTP Method: POST
   - Trigger type: Request
   
3. O app registra o push token automaticamente no login (tabela `push_tokens`)

## CloudFlare Tunnel

O túnel expõe as Edge Functions do Supabase publicamente. Configuração em `.cloudflare/config.yml`.

**Setup rápido:**
1. `cloudflared tunnel login`
2. `cloudflared tunnel create couplespace`
3. Configure o DNS: aponte `functions.couplespace.app` para o túnel no CloudFlare Dashboard
4. Defina `CLOUDFLARE_TUNNEL_TOKEN` no `.env`
5. Execute `cloudflared tunnel run couplespace`
