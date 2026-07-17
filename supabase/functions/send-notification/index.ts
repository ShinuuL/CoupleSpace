import "jsr:@supabase/functions-js/edge-runtime.d.ts"
import * as jose from "npm:jose@5"

interface MessagePayload {
  type: "INSERT"
  table: string
  schema: string
  record: {
    id: string
    sender: string
    text: string | null
    image_url: string | null
    created_at: string
  }
}

interface PushToken {
  token: string
  platform: string
}

Deno.serve(async (req) => {
  try {
    const payload: MessagePayload = await req.json()

    if (payload.type !== "INSERT" || payload.table !== "messages") {
      return new Response("Ignored", { status: 200 })
    }

    const { sender, text } = payload.record

    const {
      SUPABASE_URL,
      SUPABASE_SERVICE_ROLE_KEY,
      SUPABASE_ANON_KEY,
      FIREBASE_PROJECT_ID,
      FIREBASE_CLIENT_EMAIL,
      FIREBASE_PRIVATE_KEY,
    } = Deno.env.toObject()

    if (!FIREBASE_PROJECT_ID || !FIREBASE_CLIENT_EMAIL || !FIREBASE_PRIVATE_KEY) {
      return new Response(JSON.stringify({ error: "Firebase credentials not configured" }), {
        status: 500,
        headers: { "Content-Type": "application/json" },
      })
    }

    const apiKey = SUPABASE_SERVICE_ROLE_KEY || SUPABASE_ANON_KEY

    const configResp = await fetch(
      `${SUPABASE_URL}/rest/v1/space_config?id=eq.1&select=user1_name,user2_name`,
      {
        headers: {
          "apikey": apiKey,
          "Authorization": `Bearer ${apiKey}`,
        },
      }
    )
    const configs = await configResp.json()
    const config = configs[0]
    if (!config) return new Response("No config", { status: 200 })

    const partnerName = sender === config.user1_name ? config.user2_name : config.user1_name

    const tokenResp = await fetch(
      `${SUPABASE_URL}/rest/v1/push_tokens?user=eq.${encodeURIComponent(partnerName)}&select=token,platform`,
      {
        headers: {
          "apikey": apiKey,
          "Authorization": `Bearer ${apiKey}`,
        },
      }
    )
    const tokens: PushToken[] = await tokenResp.json()

    if (!tokens || tokens.length === 0) {
      return new Response("No push token found", { status: 200 })
    }

    const title = "CoupleSpace"
    const body = text
      ? `${sender}: ${text.substring(0, 100)}`
      : `Nova mensagem de ${sender}`

    const privateKey = FIREBASE_PRIVATE_KEY.replace(/\\n/g, "\n")
    const ecPrivateKey = await jose.importPKCS8(privateKey, "RS256")

    const assertion = await new jose.SignJWT({
      scope: "https://www.googleapis.com/auth/firebase.messaging",
    })
      .setProtectedHeader({ alg: "RS256", typ: "JWT" })
      .setIssuer(FIREBASE_CLIENT_EMAIL)
      .setAudience("https://oauth2.googleapis.com/token")
      .setIssuedAt()
      .setExpirationTime("1h")
      .sign(ecPrivateKey)

    const tokenResp2 = await fetch("https://oauth2.googleapis.com/token", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({
        grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
        assertion,
      }),
    })
    const tokenData = await tokenResp2.json()
    const accessToken = tokenData.access_token

    if (!accessToken) {
      return new Response(
        JSON.stringify({ error: "Failed to get access token", details: tokenData }),
        { status: 500, headers: { "Content-Type": "application/json" } }
      )
    }

    const results = []
    for (const token of tokens) {
      try {
        const fcmResp = await fetch(
          `https://fcm.googleapis.com/v1/projects/${FIREBASE_PROJECT_ID}/messages:send`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${accessToken}`,
            },
            body: JSON.stringify({
              message: {
                token: token.token,
                notification: { title, body },
                data: { sender, type: "new_message" },
              },
            }),
          }
        )
        const result = await fcmResp.json()
        results.push({ token: token.token.substring(0, 12) + "...", result })
      } catch (e) {
        results.push({ token: token.token.substring(0, 12) + "...", error: e.message })
      }
    }

    return new Response(JSON.stringify({ sent: results.length, results }), {
      headers: { "Content-Type": "application/json" },
    })
  } catch (e) {
    return new Response(JSON.stringify({ error: e.message }), {
      status: 500,
      headers: { "Content-Type": "application/json" },
    })
  }
})
